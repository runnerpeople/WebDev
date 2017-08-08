package com.kpbs.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "zsi_pos")
public class DBData implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "zsi_pos_id",nullable = false,length = 32)
    private String zsi_pos_id;

    @Column(name = "zsi_transdata_id",nullable = false,length = 32)
    private String zsi_transdata_id;

    @Column(name = "ad_client_id",length = 32)
    private String ad_client_id;

    @Column(name = "ad_org_id",length = 32)
    private String ad_org_id;

    @Column(name = "isactive",length = 1)
    private Character isactive;

    @Column(name = "created",columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "createdby",length = 32)
    private String createdby;

    @Column(name = "updated",columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.DATE)
    private Date updated;

    @Column(name = "updatedby",length = 32)
    private String updateby;

    @Column(name = "p_poscode",length = 255)
    private String p_poscode;

    @Column(name = "p_name",length = 500)
    private String p_name;

    @Column(name = "p_code",length = 255)
    private String p_code;

    @Column(name = "p_inn",length = 64)
    private String p_inn;

    @Column(name = "p_addr",length = 5000)
    private String p_addr;

    @Column(name = "p_deliveryaddr",length = 5000)
    private String p_deliveryaddr;

    @Column(name = "p_sign",length = 500)
    private String p_sign;

    @Column(name = "p_posaddr",length = 5000)
    private String p_posaddr;

    @Column(name = "p_latitude",length = 255)
    private String p_latitude;

    @Column(name = "p_longitude",length = 255)
    private String p_longitude;

    @Column(name = "p_comment",length = 5000)
    private String p_comment;

    @Column(name = "p_enriched_ts",columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.DATE)
    private Date p_enriched_ts;

    @Column(name = "p_enriched",length = 1,columnDefinition = "character varying(1) DEFAULT 'N'::character varying")
    private String p_enriched;

    @Column(name = "p_region",length = 255,columnDefinition = "character varying(255) DEFAULT ''::character varying")
    private String p_region;

    @Column(name = "p_company_short",length = 255,columnDefinition = "character varying(255) DEFAULT ''::character varying")
    private String p_company_short;

    @Column(name = "latitude",precision=10, scale=6)
    private BigDecimal latitude;

    @Column(name = "longitude",precision=10, scale=6)
    private BigDecimal longitude;

    public DBData() {}

    public DBData(String zsi_pos_id, String zsi_transdata_id, String ad_client_id, String ad_org_id, Character isactive, Date created, String createdby, Date updated, String updateby, String p_poscode, String p_name, String p_code, String p_inn, String p_addr, String p_deliveryaddr, String p_sign, String p_posaddr, String p_latitude, String p_longitude, String p_comment, Date p_enriched_ts, String p_enriched, String p_region, String p_company_short, BigDecimal latitude, BigDecimal longitude) {
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

    public void setZsi_pos_id(String zsi_pos_id) {
        this.zsi_pos_id = zsi_pos_id;
    }

    public String getZsi_transdata_id() {
        return zsi_transdata_id;
    }

    public void setZsi_transdata_id(String zsi_transdata_id) {
        this.zsi_transdata_id = zsi_transdata_id;
    }

    public String getAd_client_id() {
        return ad_client_id;
    }

    public void setAd_client_id(String ad_client_id) {
        this.ad_client_id = ad_client_id;
    }

    public String getAd_org_id() {
        return ad_org_id;
    }

    public void setAd_org_id(String ad_org_id) {
        this.ad_org_id = ad_org_id;
    }

    public Character getIsactive() {
        return isactive;
    }

    public void setIsactive(Character isactive) {
        this.isactive = isactive;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    public String getP_poscode() {
        return p_poscode;
    }

    public void setP_poscode(String p_poscode) {
        this.p_poscode = p_poscode;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_code() {
        return p_code;
    }

    public void setP_code(String p_code) {
        this.p_code = p_code;
    }

    public String getP_inn() {
        return p_inn;
    }

    public void setP_inn(String p_inn) {
        this.p_inn = p_inn;
    }

    public String getP_addr() {
        return p_addr;
    }

    public void setP_addr(String p_addr) {
        this.p_addr = p_addr;
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

    public String getP_posaddr() {
        return p_posaddr;
    }

    public void setP_posaddr(String p_posaddr) {
        this.p_posaddr = p_posaddr;
    }

    public String getP_latitude() {
        return p_latitude;
    }

    public void setP_latitude(String p_latitude) {
        this.p_latitude = p_latitude;
    }

    public String getP_longitude() {
        return p_longitude;
    }

    public void setP_longitude(String p_longitude) {
        this.p_longitude = p_longitude;
    }

    public String getP_comment() {
        return p_comment;
    }

    public void setP_comment(String p_comment) {
        this.p_comment = p_comment;
    }

    public Date getP_enriched_ts() {
        return p_enriched_ts;
    }

    public void setP_enriched_ts(Date p_enriched_ts) {
        this.p_enriched_ts = p_enriched_ts;
    }

    public String getP_enriched() {
        return p_enriched;
    }

    public void setP_enriched(String p_enriched) {
        this.p_enriched = p_enriched;
    }

    public String getP_region() {
        return p_region;
    }

    public void setP_region(String p_region) {
        this.p_region = p_region;
    }

    public String getP_company_short() {
        return p_company_short;
    }

    public void setP_company_short(String p_company_short) {
        this.p_company_short = p_company_short;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
