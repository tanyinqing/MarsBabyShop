package com.xlw.babyshop.model;

/**
 * 限时抢购商品
 */
public class LimitbuyModel {

    private int id;
    private String name;        // 商品名称
    private String pic;         // 商品图片
    private double price;       // 会员价
    private double limitprice;  // 限时特价
    private long lefttime;      // 剩余时间，单位为秒

    public LimitbuyModel() {
        // TODO Auto-generated constructor stub
    }

    public LimitbuyModel(int id, String name, String pic, double price, double limitprice, long lefttime) {
        super();
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.price = price;
        this.limitprice = limitprice;
        this.lefttime = lefttime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLimitprice() {
        return limitprice;
    }

    public void setLimitprice(double limitprice) {
        this.limitprice = limitprice;
    }

    public long getLefttime() {
        return lefttime;
    }

    public void setLefttime(long lefttime) {
        this.lefttime = lefttime;
    }
}
