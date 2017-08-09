package com.kpbs.service;

import com.kpbs.data.DBUtils;
import com.kpbs.model.DBData;
import com.kpbs.response.ResponseServer;
import com.kpbs.response.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Transactional
@Service
public class DBService {

    @Autowired
    private DBUtils dataDao;

    public ResponseServer getData(int start, int count, HashMap<String,String[]> sorted_params,
                                   HashMap<String,String[]> filter_params) {
        return dataDao.getData(start,count,sorted_params,filter_params);
    }

    public ResponseStatus updateData(DBData newData) {
        return dataDao.updateData(newData);
    }

    public ResponseStatus deleteData(DBData oldData) {
        return dataDao.deleteData(oldData);
    }

}
