package com.xlw.babyshop.model;

/**
 * 订单列表
 */
public class OrderListModel {

    private String orderid;     // 订单编号
    private String status;      // 订单显示状态
    private double price;       // 订单金额
    private String time;        // 下单时间
    private int flag;           // 订单标识，1=>可删除可修改 2=>不可修改 3=>已完成

    public OrderListModel() {
    }

    public OrderListModel(String orderid, String status, double price, String time, int flag) {
        super();
        this.orderid = orderid;
        this.status = status;
        this.price = price;
        this.time = time;
        this.flag = flag;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
