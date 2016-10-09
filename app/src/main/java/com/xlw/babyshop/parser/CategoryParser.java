package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.CategoryModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/19.
 */
public class CategoryParser extends BaseJSONParser<List<CategoryModel>> {

    /**
     *
     * @param paramString       是从服务器端返回的JSON格式的字符串
     * @return
     * @throws JSONException
     */
    @Override
    public List<CategoryModel> parseJSON(String paramString) throws JSONException {
        JSONObject obj = new JSONObject(paramString);   // 将字符串转换为JSON对象
        String str = obj.getString("category");         // 获取JSON对象中name为"category"所对应的value值
        return JSON.parseArray(str, CategoryModel.class);
    }

}
