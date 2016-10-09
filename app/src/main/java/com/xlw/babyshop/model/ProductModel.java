package com.xlw.babyshop.model;

public class ProductModel {

    public int id;              // 商品id
    public String name;         // 商品名称
    public String pic;          // 商品图片
    public double marketprice;  // 市场价
    public double price;        // 会员价

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
}
