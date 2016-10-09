package com.xlw.babyshop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐品牌栏目
 */
public class BrandCategoryModel {

    private String key;     // 分区名称

    /** */
    private List<BrandModel> value = new ArrayList<BrandModel>();     // 栏目下所有的 Brands

    public BrandCategoryModel() {

    }
    public BrandCategoryModel(String key, List<BrandModel> value) {
        super();
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<BrandModel> getValue() {
        return value;
    }

    public void setValue(List<BrandModel> value) {
        this.value = value;
    }
}
