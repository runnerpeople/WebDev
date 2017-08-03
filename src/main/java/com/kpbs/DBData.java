package com.kpbs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "zsi_pos")
public class DBData  implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "zsi_pos_id")
    public String zsi_pos_id;

    @Column(name = "p_deliveryaddr")
    public String p_deliveryaddr;

    @Column(name = "p_sign")
    public String p_sign;

    public DBData() {}

    public DBData(String id, String delivery_addr, String sign) {
        this.zsi_pos_id = id;
        this.p_deliveryaddr = delivery_addr;
        this.p_sign = sign;
    }

    public String getId() {
        return zsi_pos_id;
    }

    public String getDelivery_addr() {
        return p_deliveryaddr;
    }

    public String getSign() {
        return p_sign;
    }

    public void setId(String id) {
        this.zsi_pos_id = id;
    }

    public void setDelivery_addr(String delivery_addr) {
        this.p_deliveryaddr = delivery_addr;
    }

    public void setSign(String sign) {
        this.p_sign = sign;
    }
}
