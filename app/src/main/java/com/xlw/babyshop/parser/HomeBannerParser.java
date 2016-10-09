package com.xlw.babyshop.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.HomeBannerModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/19.
 *
 * 首页banner解析
 */
public class HomeBannerParser extends BaseJSONParser<List<HomeBannerModel>> {

    @Override
    public List<HomeBannerModel> parseJSON(String paramString) throws JSONException {
        if (!TextUtils.isEmpty(checkResponse(paramString))) {
            JSONObject j = new JSONObject(paramString);
            return JSON.parseArray(j.getString("home_banner"), HomeBannerModel.class);
        }
        return null;
    }
}
