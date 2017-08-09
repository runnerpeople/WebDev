package com.kpbs.controller;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class WebRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String URL = "/data?continue={continue_param}&start={start_param}&count={count_param}";

    private static final String CONTINUE_PARAM = "true";
    private static final String START_OK_PARAM = "10";
    private static final String START_ERROR_PARAM = "1000";
    private static final String COUNT_OK_PARAM = "20";
    private static final String COUNT_ERROR_PARAM = "-20";

    private String getUriString(String path, Map<String,String> params) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(path).build();
        uriComponents = uriComponents.expand(params);
        return uriComponents.toUriString();
    }

    @Test
    public void testGet404() throws Exception {
        mockMvc.perform(
                get("/hello"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDefaultParams() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/data"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pos", is(0)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        String data = JsonPath.read(content,"$.data").toString();
        assertNotNull(data);
    }

    @Test
    public void testErrorUserRequest() throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("continue_param",CONTINUE_PARAM);
        params.put("start_param","");
        params.put("count_param",COUNT_OK_PARAM);
        String uriString = getUriString(URL, params);
        mockMvc.perform(
                get(uriString))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data", is(new ArrayList<>())))
                .andReturn();
    }

    @Test
    public void testNegativeUserRequest() throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("continue_param",CONTINUE_PARAM);
        params.put("start_param",START_OK_PARAM);
        params.put("count_param",COUNT_ERROR_PARAM);
        String uriString = getUriString(URL, params);
        mockMvc.perform(
                get(uriString))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data", is(new ArrayList<>())))
                .andReturn();
    }

    @Test
    public void testLimitUserRequest() throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("continue_param",CONTINUE_PARAM);
        params.put("start_param",START_ERROR_PARAM);
        params.put("count_param",COUNT_OK_PARAM);
        String uriString = getUriString(URL, params);
        mockMvc.perform(
                get(uriString))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data", is(new ArrayList<>())))
                .andReturn();
    }

    @Test
    public void testChangeDefault() throws Exception {
        mockMvc.perform(
                get("/changeDefault?count=20"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data", is(new ArrayList<>())))
                .andReturn();
        assertEquals(WebRestController.DEFAULT_COUNT_PARAM,20);
    }

    @Test
    public void testFilterParams() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/data?filter[p_name]=ИП"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pos", is(0)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        JSONArray dataJSON = new JSONObject(content).getJSONArray("data");
        for(int i=0;i<dataJSON.length();i++) {
            JSONObject value = dataJSON.getJSONObject(i);
            String p_name = value.getString("p_name");
            assertThat(p_name,containsString("ИП"));
        }
    }

    @Test
    public void testSortedParams() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/data?sort[p_code]=asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pos", is(0)))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        JSONArray dataJSON = new JSONObject(content).getJSONArray("data");
        for(int i=1;i<dataJSON.length();i++) {
            JSONObject prev_value = dataJSON.getJSONObject(i-1);
            JSONObject next_value = dataJSON.getJSONObject(i);
            String prev_code = prev_value.getString("p_code");
            String next_code = next_value.getString("p_code");
            assertTrue(prev_code.compareTo(next_code) <= 0);
        }
    }

    @Test
    public void testUpdateData() throws Exception {
        MvcResult findDataResult = mockMvc.perform(
                get("/data?filter[p_code]=26121"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pos", is(0)))
                .andReturn();
        String contentOldData = findDataResult.getResponse().getContentAsString();
        String oldTransdataID = new JSONObject(contentOldData)
                .getJSONArray("data")
                .getJSONObject(0)
                .getString("zsi_transdata_id");
        mockMvc.perform(
                post("/data?operation=update")
                        .content("zsi_pos_id=7d4db768d8594140b9aa6dd0010c21d7&zsi_transdata_id=sakdjhflkas&ad_client_id=E5A6B128E46049D58BE951C4C980CACB&ad_org_id=0&isactive=Y&created=1500384874240&createdby=&updated=2017-07-18&updateby=&p_poscode=26121&p_name=%D0%98%D0%9F%20%D0%9A%D1%83%D1%82%D0%BB%D0%B8%D0%BC%D0%B5%D1%82%D0%BE%D0%B2-2&p_code=26121&p_inn=010200435012&p_addr=&p_deliveryaddr=%D1%81.%D0%95%D0%BB%D0%B5%D0%BD%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B5%20%D1%83%D0%BB.%20%D0%9A%D0%B0%D0%BB%D0%B8%D0%BD%D0%B8%D0%BD%D0%B0%203&p_sign=%D0%98%D0%9F%20%D0%9A%D1%83%D1%82%D0%BB%D0%B8%D0%BC%D0%B5%D1%82%D0%BE%D0%B2%20-2&p_posaddr=%D1%81.%D0%95%D0%BB%D0%B5%D0%BD%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B5%20%D1%83%D0%BB.%20%D0%9A%D0%B0%D0%BB%D0%B8%D0%BD%D0%B8%D0%BD%D0%B0%203&p_latitude=&p_longitude=&p_comment=&p_enriched_ts=2017-07-20&p_enriched=Y&p_region=&p_company_short=%D0%9A%D1%83%D1%82%D0%BB%D0%B8%D0%BC%D0%B5%D1%82%D0%BE%D0%B2-2&latitude=39.690785&longitude=45.107247&id=1502279439541&webix_operation=update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is("success")))
                .andReturn();
        findDataResult = mockMvc.perform(
                get("/data?filter[p_code]=26121"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pos", is(0)))
                .andReturn();
        String contentNewData = findDataResult.getResponse().getContentAsString();
        String newTransdataID = new JSONObject(contentNewData)
                .getJSONArray("data")
                .getJSONObject(0)
                .getString("zsi_transdata_id");
        System.out.println(oldTransdataID);
        System.out.println(newTransdataID);
        assertNotEquals(oldTransdataID,newTransdataID);
        assertEquals(oldTransdataID,"250d741580384b45a32b141cf73a386c");
        assertEquals(newTransdataID,"sakdjhflkas");
    }

    @Test
    public void testDeleteData() throws Exception {
        MvcResult findDataResult = mockMvc.perform(
                get("/data?filter[p_code]=26121"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pos", is(0)))
                .andReturn();
        String contentData = findDataResult.getResponse().getContentAsString();
        assertNotNull(contentData);
        JSONArray dataJSON = new JSONObject(contentData).getJSONArray("data");
        assertTrue(dataJSON.length() > 0);
        mockMvc.perform(
                post("/data?operation=delete")
                        .content("zsi_pos_id=7d4db768d8594140b9aa6dd0010c21d7&zsi_transdata_id=250d741580384b45a32b141cf73a386c&ad_client_id=E5A6B128E46049D58BE951C4C980CACB&ad_org_id=0&isactive=Y&created=1500384874240&createdby=&updated=2017-07-18&updateby=&p_poscode=26121&p_name=%D0%98%D0%9F%20%D0%9A%D1%83%D1%82%D0%BB%D0%B8%D0%BC%D0%B5%D1%82%D0%BE%D0%B2-2&p_code=26121&p_inn=010200435012&p_addr=&p_deliveryaddr=%D1%81.%D0%95%D0%BB%D0%B5%D0%BD%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B5%20%D1%83%D0%BB.%20%D0%9A%D0%B0%D0%BB%D0%B8%D0%BD%D0%B8%D0%BD%D0%B0%203&p_sign=%D0%98%D0%9F%20%D0%9A%D1%83%D1%82%D0%BB%D0%B8%D0%BC%D0%B5%D1%82%D0%BE%D0%B2%20-2&p_posaddr=%D1%81.%D0%95%D0%BB%D0%B5%D0%BD%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B5%20%D1%83%D0%BB.%20%D0%9A%D0%B0%D0%BB%D0%B8%D0%BD%D0%B8%D0%BD%D0%B0%203&p_latitude=&p_longitude=&p_comment=&p_enriched_ts=2017-07-20&p_enriched=Y&p_region=&p_company_short=%D0%9A%D1%83%D1%82%D0%BB%D0%B8%D0%BC%D0%B5%D1%82%D0%BE%D0%B2-2&latitude=39.690785&longitude=45.107247&id=1502279439541&webix_operation=delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is("success")))
                .andReturn();
        findDataResult = mockMvc.perform(
                get("/data?filter[p_code]=26121"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        String contentNewData = findDataResult.getResponse().getContentAsString();
        assertNotNull(contentNewData);
        JSONArray dataNewJSON = new JSONObject(contentNewData).getJSONArray("data");
        System.out.println(dataNewJSON);
        assertTrue(dataNewJSON.length() == 0);
    }
}
