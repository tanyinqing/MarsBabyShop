package com.xlw.babyshop.model;

/**
 * Created by xinliwei on 2015/7/19.
 *
 * 首页栏目
 */
public class HomeCategoryModel {
    private int imgresid;       //  图像资源id
    private String title;       // 标题

    public HomeCategoryModel() {
    }

    public HomeCategoryModel(int imgresid, String title) {
        super();
        this.imgresid = imgresid;
        this.title = title;
    }

    public int getImgresid() {
        return imgresid;
    }

    public void setImgresid(int imgresid) {
        this.imgresid = imgresid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
