package com.xlw.babyshop.model;

public class OrderForSubmitModel {

    public String orderid;      // 订单id
    public String price;        // 价格
    public String paymenttype;  // 支付类型

    public OrderForSubmitModel() {
        super();
    }

    public OrderForSubmitModel(String orderid, String price, String paymenttype) {
        super();
        this.orderid = orderid;
        this.price = price;
        this.paymenttype = paymenttype;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

}
