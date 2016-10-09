package com.xlw.babyshop.model;

/**
 * 订单详细信息
 */
public class OrderInfoModel {

    private String orderid;     // 订单编号
    private String status;      // 订单显示状态
    private String time;        // 下单时间
    private int flag;           // 订单标识，1=>可删除可修改 2=>不可修改 3=>已完成

    public OrderInfoModel() {
    }

    public OrderInfoModel(String orderid, String status, String time, int flag) {
        super();
        this.orderid = orderid;
        this.status = status;
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
