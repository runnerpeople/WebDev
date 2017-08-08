package com.kpbs.service;

import com.kpbs.response.ResponseServer;
import com.kpbs.service.DBService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DBServiceTest {

    @Autowired
    private DBService dataService;

    @Test
    public void testServiceGetAllUsers() {
        ResponseServer users = dataService.getUsers(0, 10,null,null);
        assertEquals(users.getData().size(), 10);
        assertEquals(users.getPos(), new Long(0L));
    }
}