package com.kpbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class Controller {

    @Autowired
    private DBService dataDAO;

    private final Logger log = Logger.getLogger(Controller.class.getName());

    private static final int DEFAULT_START_PARAM = 0;
    private static int DEFAULT_COUNT_PARAM = 15;

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
        String[] params_array = new String[5];
        if (find_sorted_params.size() != 0) {
            for(Map.Entry<String,String[]> param: find_sorted_params.entrySet()) {
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


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "continue",required = false)Boolean continue_param,
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
            ResponseServer response = dataDAO.getUsers(start_param,count_param,sorted_params,filter_params);
            if (response.getData() == null || response.getData().size() == 0) {
                log.log(Level.WARNING,"Start position of data exceed limit of data");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseJSON(new ArrayList<>()));
            }
            else {
                log.log(Level.INFO,"Request of getting data: (" + getURL(request) + ")");
                return ResponseEntity.ok().body(response);
            }
        }
    }

}