package com.xlw.babyshop.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xinliwei on 2015/7/17.
 */
public abstract class BaseJSONParser<T> {

    /**
     * 将JSON格式字符串(传入的参数)解析为一个对象
     * @param paramString
     * @return
     * @throws JSONException
     */
    public abstract T parseJSON(String paramString) throws JSONException ;

    /**
     * 解析服务器端返回的JSON响应字符串
     * @param paramString
     * @throws JSONException
     */
    public String checkResponse(String paramString) throws JSONException {
        if (paramString == null) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject(paramString);
            String result = jsonObject.getString("response");
            if (result != null && !result.equals("error")) {
                return result;
            } else {
                return null;
            }

        }
    }
}
