package com.xlw.babyshop.parser;

import android.text.TextUtils;

import org.json.JSONException;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class SuccessParser extends BaseJSONParser<Boolean>{
    @Override
    public Boolean parseJSON(String paramString) throws JSONException {
        if (!TextUtils.isEmpty(checkResponse(paramString))) {
            return true;
        }
        return false;
    }
}
