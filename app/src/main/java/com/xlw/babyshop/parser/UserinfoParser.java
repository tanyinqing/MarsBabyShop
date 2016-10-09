package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.UserModel;
import com.xlw.babyshop.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class UserinfoParser extends BaseJSONParser<UserModel>{
    private static final String TAG = "UserinfoParser";

    @Override
    public UserModel parseJSON(String paramString) throws JSONException {
        if(super.checkResponse(paramString)!=null){
            Logger.d(TAG, "解析Userinfo数据");
            JSONObject json = new JSONObject(paramString);
            String useinfoObj = json.getString("userinfo");
            UserModel userInfoList = JSON.parseObject(useinfoObj, UserModel.class);
            return userInfoList;
        }
        return null;
    }
}
