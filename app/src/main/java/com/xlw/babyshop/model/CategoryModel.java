package com.xlw.babyshop.model;

import java.io.Serializable;

/**
 * Created by xinliwei on 2015/7/19.
 */
public class CategoryModel implements Serializable {

    private static final long serialVersionUID = 683674791963215621L;

    private String id;          // id
    private String name;        // 类别名称
    private String parent_id;   // 父类别id
    private String pic;         // 图片
    private String tag;         // 标签
    private boolean isleafnode; // 是否是叶子节点

    public CategoryModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isIsleafnode() {
        return isleafnode;
    }

    public void setIsleafnode(boolean isleafnode) {
        this.isleafnode = isleafnode;
    }
}
