package com.kpbs;

import java.math.BigDecimal;
import java.util.Date;

public class RequestData {

    private String zsi_pos_id;
    private String zsi_transdata_id;
    private String ad_client_id;
    private String ad_org_id;
    private Character isactive;
    private Date created;
    private String createdby;
    private Date updated;
    private String updateby;
    private String p_poscode;
    private String p_name;
    private String p_code;
    private String p_inn;
    private String p_addr;
    private String p_deliveryaddr;
    private String p_sign;
    private String p_posaddr;
    private String p_latitude;
    private String p_longitude;
    private String p_comment;
    private Date p_enriched_ts;
    private String p_enriched;
    private String p_region;
    private String p_company_short;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public RequestData() {}

    public RequestData(String zsi_pos_id, String zsi_transdata_id, String ad_client_id, String ad_org_id, Character isactive, Date created, String createdby, Date updated, String updateby, String p_poscode, String p_name, String p_code, String p_inn, String p_addr, String p_deliveryaddr, String p_sign, String p_posaddr, String p_latitude, String p_longitude, String p_comment, Date p_enriched_ts, String p_enriched, String p_region, String p_company_short, BigDecimal latitude, BigDecimal longitude) {
        this.zsi_pos_id = zsi_pos_id;
        this.zsi_transdata_id = zsi_transdata_id;
        this.ad_client_id = ad_client_id;
        this.ad_org_id = ad_org_id;
        this.isactive = isactive;
        this.created = created;
        this.createdby = createdby;
        this.updated = updated;
        this.updateby = updateby;
        this.p_poscode = p_poscode;
        this.p_name = p_name;
        this.p_code = p_code;
        this.p_inn = p_inn;
        this.p_addr = p_addr;
        this.p_deliveryaddr = p_deliveryaddr;
        this.p_sign = p_sign;
        this.p_posaddr = p_posaddr;
        this.p_latitude = p_latitude;
        this.p_longitude = p_longitude;
        this.p_comment = p_comment;
        this.p_enriched_ts = p_enriched_ts;
        this.p_enriched = p_enriched;
        this.p_region = p_region;
        this.p_company_short = p_company_short;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getZsi_pos_id() {
        return zsi_pos_id;
    }

    public String getZsi_transdata_id() {
        return zsi_transdata_id;
    }

    public String getAd_client_id() {
        return ad_client_id;
    }

    public String getAd_org_id() {
        return ad_org_id;
    }

    public Character getIsactive() {
        return isactive;
    }

    public Date getCreated() {
        return created;
    }

    public String getCreatedby() {
        return createdby;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getUpdateby() {
        return updateby;
    }

    public String getP_poscode() {
        return p_poscode;
    }

    public String getP_name() {
        return p_name;
    }

    public String getP_code() {
        return p_code;
    }

    public String getP_inn() {
        return p_inn;
    }

    public String getP_addr() {
        return p_addr;
    }

    public String getP_deliveryaddr() {
        return p_deliveryaddr;
    }

    public String getP_sign() {
        return p_sign;
    }

    public String getP_posaddr() {
        return p_posaddr;
    }

    public String getP_latitude() {
        return p_latitude;
    }

    public String getP_longitude() {
        return p_longitude;
    }

    public String getP_comment() {
        return p_comment;
    }

    public Date getP_enriched_ts() {
        return p_enriched_ts;
    }

    public String getP_enriched() {
        return p_enriched;
    }

    public String getP_region() {
        return p_region;
    }

    public String getP_company_short() {
        return p_company_short;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
