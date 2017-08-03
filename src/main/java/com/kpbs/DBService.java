package com.kpbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;


@Transactional
@Service
public class DBService {

    @Autowired
    private DBUtils dataDao;

    public ResponseServer getUsers(int start, int count, HashMap<String,String[]> sorted_params,
                                                         HashMap<String,String[]> filter_params) {
        return dataDao.getUsers(start,count,sorted_params,filter_params);
    }

}
