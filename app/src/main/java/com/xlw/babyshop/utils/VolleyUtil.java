package com.xlw.babyshop.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.xlw.babyshop.application.XlwApplication;
import com.xlw.babyshop.model.RequestModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinliwei on 2015/7/5.
 */
public class VolleyUtil {

    private static final String TAG = "VolleyUtil.class";

    ResponseCallback responseCallback;  // 异步请求返回时的回调接口

    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    JsonArrayRequest jsonArrayRequest;

    private static HashMap<String, String> headers = new HashMap<String, String>();
    static{
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("Appkey","");
        headers.put("Udid","");
        headers.put("Os","");
        headers.put("Osversion","");
        headers.put("Appversion","");
        headers.put("Sourceid","");
        headers.put("Ver","");
        headers.put("Userid","");
        headers.put("Usersession","");
        headers.put("Unique","");
        headers.put("Cookie","");
    }

    public VolleyUtil() {
    }

    public VolleyUtil(ResponseCallback callback){
        this.responseCallback = callback;
    }

    // 请求服务器端字符串
    public Request requestString(String url_str){
        Logger.i(TAG,"请求的url:" + url_str);
        // 创建一个StringRequest对象
        stringRequest = new StringRequest(Request.Method.GET,url_str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Logger.i(TAG,"服务器端响应:" + s);
                        if(responseCallback != null){
                            responseCallback.responseString(s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Logger.i(TAG,"服务器端响应错误");
                    }
                }
        ){
            //  Volley请求类提供了一个 getHeaders（）的方法，
            // 重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Response<String> superResponse = super.parseNetworkResponse(response);
                Map<String, String> responseHeaders = response.headers;
                setCookie(responseHeaders);
                Logger.i("VolleyUtil-requestString","处理返回的头部信息");
                return superResponse;
            }
        };

        // 将request请求对象添加到requestQueue请求队列
        XlwApplication.getInstance().requestQueue.add(stringRequest);

        return stringRequest;
    }

    // 请求JSON格式字符串
    public Request requestJSONObject(String url_str){
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url_str, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // 服务器端正确响应时,回调此函数
                        if(responseCallback != null){
                            responseCallback.responseJSONObject(jsonObject);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // 服务器端未正确响应时,回调此函数

                    }
                }
        ){
            //  Volley请求类提供了一个 getHeaders（）的方法，
            // 重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        // 将request请求对象添加到requestQueue请求队列
        XlwApplication.getInstance().requestQueue.add(jsonObjectRequest);

        return jsonObjectRequest;
    }

    // 请求JSON格式数组字符串
    public Request requestJsonArray(String url_str){
        // 创建一个StringRequest对象
        jsonArrayRequest = new JsonArrayRequest(url_str,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        // 服务器端正确响应时,回调此函数
                        responseCallback.responseJsonArray(jsonArray);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 服务器端未正确响应时,回调此函数
            }
        });

        // 第三步,将request请求对象添加到requestQueue请求队列
        XlwApplication.getInstance().requestQueue.add(jsonArrayRequest);

        return jsonArrayRequest;
    }

    public void cancelRequest(Request request){
        if(request != null){
            request.cancel();
        }
    }

    /**
     * post数据到服务器端.要将当前的版本\cookie等信息通过http协议的头部传到服务器端
     * @param requestModel
     * @return
     */
    public Request postRequest(final RequestModel requestModel, String url_str){
        Logger.d(TAG,"Post:" + url_str);

        // 第二步，注意第一个参数指定了POST方式提交
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url_str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseText) {
                        if(responseCallback != null){
                            responseCallback.responseString(responseText);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            // 携带参数
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = null;

                if(requestModel.requestDataMap != null) {
                    // 获得requestModel中封装的商品信息,填充到map集合中,作为参数上传到服务器端
                    map = requestModel.requestDataMap;
                    Logger.i("VolleyUtil_Map_id:", requestModel.requestDataMap.get("id"));
                    Logger.i("VolleyUtil_Map_count:",requestModel.requestDataMap.get("count"));
                }

                return map;
            }

            //  Volley请求类提供了一个 getHeaders（）的方法，
            // 重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> mHeaders = headers;
                mHeaders.put("Accept","*/*");
                mHeaders.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                return mHeaders;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Response<String> superResponse = super.parseNetworkResponse(response);
                Map<String, String> responseHeaders = response.headers;
                setCookie(responseHeaders);
                Logger.i("VolleyUtil.java","设置Cookie成功");
                return superResponse;
            }
        };

        // 第三步
        XlwApplication.getInstance().requestQueue.add(stringRequest);

        return stringRequest;
    }

    /**
     * 提取服务器返回的响应Response中的Cookie,并将Cookie保存在本地
     * @param responseHeaders
     */
    private void setCookie(Map<String, String> responseHeaders) {
        String rawcookie = responseHeaders.get("Set-Cookie");
        if (rawcookie != null && rawcookie.length() > 0) {
            Logger.d(TAG, rawcookie) ;
            String[] cookievalues = rawcookie.split(";");
            Logger.i("VolleyUtil.java - cookievalues", Arrays.toString(cookievalues));
            String[] keyPair = cookievalues[0].split("=");
            String key = keyPair[0].trim();
            Logger.i("VolleyUtil.java - keyPair[0]",key);
            String jsessionid = keyPair.length>1?keyPair[1].trim():"";
            Logger.i("VolleyUtil.java - JSESSIONID value", jsessionid);

//            headers.put("Cookie", jsessionid);  // 将jsessionid添加到cookie中
            headers.put("Cookie", cookievalues[0]);  // 将jsessionid添加到cookie中
        }else if(rawcookie == null){
            Logger.i(TAG, "rawcookie为null");
        }else {
            Logger.i(TAG, "rawcookie.length=" + rawcookie.length());
        }
    }

    public void setResponseCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    /**
     * Created by xinliwei on 2015/7/18.
     *
     * 回调接口,用于客户端异步请求服务器端时,处理服务器端响应异步返回时的回调处理
     * 比如,一个Activity中异步请求服务器端数据,那么它应该实现此接口,并委托给VolleyUtil
     * 当服务器返回响应数据时,VolleyUtil回调此接口中的方法
     */
    public interface ResponseCallback {

        void responseString(String responseText);

        void responseJSONObject(JSONObject jsonObject);

        void responseJsonArray(JSONArray jsonArray);

    }
}
