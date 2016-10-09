package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class SearchRecommondParser extends BaseJSONParser<String[]>{
    @Override
    public String[] parseJSON(String paramString) throws JSONException {
        if(super.checkResponse(paramString)!=null){
            JSONObject jsonObject = new JSONObject(paramString);
            String search_keywords = jsonObject.getString("search_keywords");
            String[] search= JSON.parseObject(search_keywords, String[].class);
            return search;
        }
        return null;
    }
}
