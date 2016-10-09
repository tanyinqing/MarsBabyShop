package com.xlw.babyshop.model;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class CartProductModel {

    public int id;              // ID
    public String name;         // 商品名称
    public double price;        // 会员价
    public String pic;          // 图片
    public int prodNum;         // 商品数量
    public double subtotal;     // 商品金额小计
    public int number;          // 商品库存数量，0为缺货或下架
    public int uplimit;         // 商品购买数量上限
    public boolean isgift;      // 是否赠品

    public CartProductModel() {
    }

    public CartProductModel(int id, String name, double price, String pic, int prodNum, double subtotal, int number,
                       int uplimit, boolean isgift) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.pic = pic;
        this.prodNum = prodNum;
        this.subtotal = subtotal;
        this.number = number;
        this.uplimit = uplimit;
        this.isgift = isgift;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getProdNum() {
        return prodNum;
    }

    public void setProdNum(int prodNum) {
        this.prodNum = prodNum;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isIsgift() {
        return isgift;
    }

    public void setIsgift(boolean isgift) {
        this.isgift = isgift;
    }

    public int getUplimit() {
        return uplimit;
    }

    public void setUplimit(int uplimit) {
        this.uplimit = uplimit;
    }
}
