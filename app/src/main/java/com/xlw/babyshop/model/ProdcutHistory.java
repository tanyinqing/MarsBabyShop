package com.xlw.babyshop.model;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class ProdcutHistory extends ProductListModel implements Comparable<ProdcutHistory>{
    /** 浏览时间 */
    private long time;

    public ProdcutHistory() {
    }

    public ProdcutHistory(int id, String name, String pic, double marketprice, double price, int comment_count,
                          long time) {
        super(id, name, pic, marketprice, price, comment_count);
        this.time = time;
    }

    public ProdcutHistory(ProductListModel productListModel) {
        super(productListModel.getId(), productListModel.getName(), productListModel.getPic(), productListModel.getMarketprice(),
                productListModel.getPrice(), productListModel.getComment_count());
        this.time = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int compareTo(ProdcutHistory another) {
        return time > another.time ? 1 : -1;
    }
}
