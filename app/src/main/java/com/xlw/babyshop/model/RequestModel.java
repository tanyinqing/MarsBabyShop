package com.xlw.babyshop.model;


import com.xlw.babyshop.parser.BaseJSONParser;

import java.util.HashMap;

/**
 * Created by xinliwei on 2015/7/17.
 *
 * 代表请求对象
 */
public class RequestModel {

    public String requestUrl;      // 请求的url
    public HashMap<String, String> requestDataMap;  // 向服务器端提交的数据
    public BaseJSONParser<?> jsonParser;    // 对从服务器端返回的JSON数据进行解析的解析器

    public RequestModel() {
    }

    public RequestModel(String requestUrl) {
        super();
        this.requestUrl = requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setRequestDataMap(HashMap<String, String> requestDataMap) {
        this.requestDataMap = requestDataMap;
    }

    public void setJsonParser(BaseJSONParser<?> jsonParser) {
        this.jsonParser = jsonParser;
    }
}
