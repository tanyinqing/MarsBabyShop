package com.xlw.babyshop.model;

import java.io.Serializable;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class ProductListModel implements Serializable {

    private static final long serialVersionUID = -3329730909431080072L;

    private int id;                 // id
    private String name;            // 商品名称
    private String pic;             // 商品图片
    private double marketprice;     // 市场价格
    private double price;           // 会员价格
    private int comment_count;      // 评论数量

    public ProductListModel() {
    }

    public ProductListModel(int id, String name, String pic, double marketprice, double price, int comment_count) {
        super();
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.marketprice = marketprice;
        this.price = price;
        this.comment_count = comment_count;
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

    public double getMarketprice() {
        return marketprice;
    }
    public void setMarketprice(double marketprice) {
        this.marketprice = marketprice;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    @Override
    public String toString() {
        return "ProductListVo [id=" + id + ", name=" + name + ", pic=" + pic + ", marketprice=" + marketprice
                + ", price=" + price + ", comment_count=" + comment_count + "]";
    }
}
