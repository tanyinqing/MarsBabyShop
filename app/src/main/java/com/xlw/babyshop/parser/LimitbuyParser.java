package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.LimitbuyModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/21.
 */
public class LimitbuyParser extends BaseJSONParser<List<LimitbuyModel>>{

    @Override
    public List<LimitbuyModel> parseJSON(String paramString) throws JSONException {
        if(super.checkResponse(paramString)!=null){
            JSONObject jsonObject = new JSONObject(paramString);
            String productlist = jsonObject.getString("productlist");
            return JSON.parseArray(productlist, LimitbuyModel.class);
        }else{
            return null;
        }
    }
}
