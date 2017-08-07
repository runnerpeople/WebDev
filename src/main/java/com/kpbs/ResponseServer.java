package com.kpbs;

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

class ResponseJSON {
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

class ResponseMessage {
    private String status;
    private String id;

    public ResponseMessage() { }

    public ResponseMessage(String status, String id) {
        this.status = status;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

