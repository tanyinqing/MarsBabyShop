package com.xlw.babyshop.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.AddressDetailModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 地址详细信息
 */
public class AddressManageParser extends BaseJSONParser<List<AddressDetailModel>>{
    @Override
    public List<AddressDetailModel> parseJSON(String paramString) throws JSONException {
        if (!TextUtils.isEmpty(checkResponse(paramString))) {
            JSONObject j = new JSONObject(paramString);
            String addresslist = j.getString("addresslist");
            return JSON.parseArray(addresslist, AddressDetailModel.class);
        }
        return null;
    }
}
