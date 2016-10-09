package com.xlw.babyshop.model;

/**
 * Created by xinliwei on 2015/7/19.
 *
 * 首页baner图片
 */
public class HomeBannerModel {
    private int id;         // id
    private String title;   // 标题
    private String pic;     // 图片

    public HomeBannerModel() {
    }

    public HomeBannerModel(int id, String title, String pic) {
        super();
        this.id = id;
        this.title = title;
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
