package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.BulletinModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/21.
 */
public class BulletinParser extends BaseJSONParser<List<BulletinModel>>{
    @Override
    public List<BulletinModel> parseJSON(String paramString) throws JSONException {
        if (super.checkResponse(paramString) != null) {
            JSONObject jsonObject = new JSONObject(paramString);
            String topic = jsonObject.getString("topic");
            return JSON.parseArray(topic, BulletinModel.class);
        }else{

            return null;
        }
    }
}
