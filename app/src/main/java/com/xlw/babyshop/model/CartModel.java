package com.xlw.babyshop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 */
public class CartModel {

    public List<CartProductModel> productlist = new ArrayList<CartProductModel>();    // 商品列表
    public List<String> cart_prom = new ArrayList<String>();    // 享受促销信息
    public AddupModel cart_addup;        // 购物车总计

    public CartModel() {
    }

    public CartModel(List<CartProductModel> productlist, List<String> cart_prom, AddupModel cart_addup) {
        super();
        this.productlist = productlist;
        this.cart_prom = cart_prom;
        this.cart_addup = cart_addup;
    }

    public List<CartProductModel> getProductlist() {
        return productlist;
    }

    public void setProductlist(List<CartProductModel> productlist) {
        this.productlist = productlist;
    }

    public List<String> getCart_prom() {
        return cart_prom;
    }

    public void setCart_prom(List<String> cart_prom) {
        this.cart_prom = cart_prom;
    }

    public AddupModel getCart_addup() {
        return cart_addup;
    }

    public void setCart_addup(AddupModel cart_addup) {
        this.cart_addup = cart_addup;
    }
}
