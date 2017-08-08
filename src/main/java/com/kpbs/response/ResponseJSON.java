package com.kpbs.response;

import com.kpbs.model.DBData;

import java.util.List;

public class ResponseJSON {
    private List<DBData> data;

    public ResponseJSON() { }

    public ResponseJSON(List<DBData> data) {
        this.data = data;
    }

    public List<DBData> getData() {
        return data;
    }

    public void setData(List<DBData> data) {
        this.data = data;
    }
}