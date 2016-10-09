package com.xlw.babyshop.model;

/**
 * 推荐品牌
 */
public class BrandModel {

    private int id;         // id
    private String name;    // 名称
    private String pic;     // 图片路径

    public BrandModel() {
    }

    public BrandModel(int id, String name, String pic) {
        super();
        this.id = id;
        this.name = name;
        this.pic = pic;
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
}
