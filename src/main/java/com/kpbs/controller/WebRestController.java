package com.kpbs.controller;

import com.kpbs.request.RequestServer;
import com.kpbs.response.ResponseErrorMessage;
import com.kpbs.response.ResponseJSON;
import com.kpbs.response.ResponseServer;
import com.kpbs.response.ResponseStatus;
import com.kpbs.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class WebRestController {

    @Autowired
    private DBService dataService;

    private final Logger log = Logger.getLogger(WebRestController.class.getName());

    public static final int DEFAULT_START_PARAM = 0;
    public static int DEFAULT_COUNT_PARAM = 15;

    private String getURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    private SortedMap<String, String[]> getByPrefix(NavigableMap<String, String[]> myMap, String prefix) {
        return myMap.subMap( prefix, prefix + Character.MAX_VALUE);
    }

    private HashMap<String,String[]> getParams(HttpServletRequest request,String prefix){
        TreeMap<String,String[]> all_params = new TreeMap<>(request.getParameterMap());
        SortedMap<String,String[]> find_sorted_params = getByPrefix(all_params,prefix);
        HashMap<String,String[]> params = new HashMap<>();
        if (find_sorted_params.size() != 0) {
            for(Map.Entry<String,String[]> param: find_sorted_params.entrySet()) {
                String[] params_array = new String[5];
                if (!(param.getValue()[0].equals(""))) {
                    String filter_param;
                    if (prefix.equals("filter")) {
                        try {
                            filter_param = URLDecoder.decode(param.getValue()[0],"UTF-8");
                            params_array[0] = filter_param;
                        }
                        catch (UnsupportedEncodingException ex) {
                            log.log(Level.WARNING,"Can't decode string" + param.getValue()[0]);
                            continue;
                        }
                    }
                    else {
                        params_array[0] = param.getValue()[0];
                    }
                    String key = param.getKey().replace(prefix, "");
                    key = key.substring(1, key.length() - 1);
                    params.put(key, params_array);
                }
            }
        }
        return params;
    }


    @RequestMapping(value = "/changeDefault", method = RequestMethod.GET)
    public ResponseEntity<?> changeDefaultCount(@RequestParam(value = "count")Integer count_param) {
        DEFAULT_COUNT_PARAM = count_param;
        log.log(Level.INFO,"Changing default value to count param");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseJSON(new ArrayList<>()));
    }


    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public ResponseEntity<?> getData(@RequestParam(value = "continue",required = false)Boolean continue_param,
                                         @RequestParam(value = "count",required = false)Integer count_param,
                                         @RequestParam(value = "start",required = false)Integer start_param,
                                         HttpServletRequest request) {
        HashMap<String,String[]> sorted_params = getParams(request,"sort");
        HashMap<String,String[]> filter_params = getParams(request,"filter");
        if (continue_param == null && count_param == null && start_param == null) {
            count_param = DEFAULT_COUNT_PARAM;
            start_param = DEFAULT_START_PARAM;
        }
        if (count_param == null || start_param == null) {
            log.log(Level.WARNING,"User can't make this type of request (" + getURL(request) + ")");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseJSON(new ArrayList<>()));
        }
        else if (count_param < 0 || start_param < 0) {
            log.log(Level.WARNING,"Error in request parameters");
            return ResponseEntity.badRequest().body(new ResponseJSON(new ArrayList<>()));
        }
        else {
            ResponseServer response = dataService.getData(start_param,count_param,sorted_params,filter_params);
            if (response == null || response.getData() == null || response.getData().size() == 0) {
                log.log(Level.WARNING,"No data");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseJSON(new ArrayList<>()));
            }
            else {
                log.log(Level.INFO,"Request of getting data: (" + getURL(request) + ")");
                return ResponseEntity.ok().body(response);
            }
        }
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> changeData(@RequestParam(value = "operation")String operation,
                                        @RequestBody MultiValueMap<String,Object> body,
                                        HttpServletRequest request) {
        if (!operation.equals("insert") && !(operation.equals("update")) && !(operation.equals("delete"))) {
            log.log(Level.WARNING,"User can't make this type of request (" + getURL(request) + ")");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseErrorMessage("error",
                    "Don't have this type of operation with data - \"" + operation + "\""));
        }
        RequestServer newData = new RequestServer(body);
        if (!newData.isValid())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseErrorMessage("error",
                    "Возникла ошибка при парсинге данных"));
        if (operation.equals("update")) {
            ResponseStatus responseMessage = dataService.updateData(newData.getData());
            return ResponseEntity.ok(responseMessage);
        }
        else if (operation.equals("delete")) {
            ResponseStatus responseMessage = dataService.deleteData(newData.getData());
            if (responseMessage.getStatus().equals("error")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseErrorMessage("error",
                        "Internal Server Error"));
            }
            else
                return ResponseEntity.ok(responseMessage);
        }
        else
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseJSON(new ArrayList<>()));
    }
}