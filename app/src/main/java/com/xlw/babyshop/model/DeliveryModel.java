package com.xlw.babyshop.model;

/**
 * 送货时间
 *
 * 周一至周五送货 2=> 双休日及公众假期送货 3=> 时间不限，工作日双休日及公众假期均可送货
 */
public class DeliveryModel {

    private int type;

    public DeliveryModel() {

    }

    public DeliveryModel(int type) {
        super();
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
