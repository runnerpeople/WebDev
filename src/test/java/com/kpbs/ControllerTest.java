package com.kpbs;

import com.jayway.jsonpath.JsonPath;
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    private final String URL = "/users?continue={continue_param}&start={start_param}&count={count_param}";

    private final String CONTINUE_PARAM = "true";
    private final String START_OK_PARAM = "10";
    private final String START_ERROR_PARAM = "1000";
    private final String COUNT_OK_PARAM = "20";
    private final String COUNT_ERROR_PARAM = "-20";

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
                get("/users"))
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
}
