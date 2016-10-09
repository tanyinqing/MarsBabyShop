package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.BrandCategoryModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 解析器,解析返回的JSON格式数据
 */
public class BrandParser extends BaseJSONParser<List<BrandCategoryModel>>{

    @Override
    public List<BrandCategoryModel> parseJSON(String paramString) throws JSONException {
        if(super.checkResponse(paramString)!=null){
            JSONObject jsonObject = new JSONObject(paramString);
            String productlist = jsonObject.getString("brand");
            return JSON.parseArray(productlist, BrandCategoryModel.class);
        }else{
            return null;
        }
    }
}
