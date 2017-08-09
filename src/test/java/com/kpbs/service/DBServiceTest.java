package com.kpbs.service;

import com.kpbs.model.DBData;
import com.kpbs.response.ResponseServer;
import com.kpbs.response.ResponseStatus;
import com.kpbs.service.DBService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DBServiceTest {

    @Autowired
    private DBService dataService;

    private HashMap<String,String[]> testFilterParams = new HashMap<>();

    @Test
    public void testServiceGetData() {
        ResponseServer users = dataService.getData(0, 10,null,null);
        assertEquals(users.getData().size(), 10);
        assertEquals(users.getPos(), new Long(0L));
    }

    @Test
    public void testServiceUpdateData() throws ParseException {
        testFilterParams.put("p_code", new String[]{"25766", "", "", "",""});
        DBData oldData = dataService.getData(0,1,null,testFilterParams).getData().get(0);
        assertEquals(oldData.getP_code(),"25766");
        oldData.setP_code("25769");
        ResponseStatus response = dataService.updateData(oldData);
        assertEquals(response.getStatus(),"success");
        List<DBData> newData = dataService.getData(0,1,null,testFilterParams).getData();
        assertEquals(newData.size(),0);
    }

    @Test
    public void testServiceDeleteData() throws ParseException {
        testFilterParams.put("p_code", new String[]{"25766", "", "", "",""});
        DBData oldData = dataService.getData(0,1,null,testFilterParams).getData().get(0);
        assertEquals(oldData.getP_code(),"25766");
        ResponseStatus response = dataService.deleteData(oldData);
        assertEquals(response.getStatus(),"success");
        List<DBData> newData = dataService.getData(0,1,null,testFilterParams).getData();
        assertEquals(newData.size(),0);
    }
}