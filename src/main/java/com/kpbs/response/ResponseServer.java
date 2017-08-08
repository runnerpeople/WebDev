package com.kpbs.response;

import com.kpbs.model.DBData;

import java.util.List;

public class ResponseServer {
    private List<DBData> data;
    private Long pos;
    private Long total_count;

    public ResponseServer() { }

    public ResponseServer(List<DBData> data, Long pos, Long total_count) {
        this.data = data;
        this.pos = pos;
        this.total_count = total_count;
    }

    public List<DBData> getData() {
        return data;
    }

    public void setData(List<DBData> data) {
        this.data = data;
    }

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    public Long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Long total_count) {
        this.total_count = total_count;
    }
}

