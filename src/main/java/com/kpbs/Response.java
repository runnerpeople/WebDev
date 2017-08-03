package com.kpbs;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private String zsi_pos_id;
    private String p_deliveryaddr;
    private String p_sign;

    public Response() { }

    public Response(String zsi_pos_id, String p_deliveryaddr, String p_sign) {
        this.zsi_pos_id = zsi_pos_id;
        this.p_deliveryaddr = p_deliveryaddr;
        this.p_sign = p_sign;
    }

    public String getZsi_pos_id() {
        return zsi_pos_id;
    }

    public void setZsi_pos_id(String zsi_pos_id) {
        this.zsi_pos_id = zsi_pos_id;
    }

    public String getP_deliveryaddr() {
        return p_deliveryaddr;
    }

    public void setP_deliveryaddr(String p_deliveryaddr) {
        this.p_deliveryaddr = p_deliveryaddr;
    }

    public String getP_sign() {
        return p_sign;
    }

    public void setP_sign(String p_sign) {
        this.p_sign = p_sign;
    }
}

class ResponseJSON {

    private List<Response> data = new ArrayList<>();

    public ResponseJSON() { }

    public ResponseJSON(List<DBData> data) {
        for (DBData d : data) {
            this.data.add(new Response(d.zsi_pos_id, d.p_deliveryaddr, d.p_sign));
        }
    }

    public ResponseJSON(List data,boolean partial) {
        List<Object[]> dd = data;
        for (Object[] d : dd) {
            String id = d[0] != null ? (String) d[0] : "";
            String delivery_addr = d[1] != null ? (String) d[1] : "";
            String sign = d[2] != null ? (String) d[2] : "";
            this.data.add(new Response(id, delivery_addr, sign));
        }
    }

    public List<Response> getData() {
        return data;
    }

    public void setData(List<Response> data) {
        this.data = data;
    }
}

class ResponseServer {
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