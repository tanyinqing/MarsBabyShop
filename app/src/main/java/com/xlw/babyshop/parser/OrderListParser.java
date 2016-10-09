package com.xlw.babyshop.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xlw.babyshop.model.OrderListModel;
import com.xlw.babyshop.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/23.
 */
public class OrderListParser extends BaseJSONParser<List<OrderListModel>>{

    private static final String TAG = "OrderListParser";

    @Override
    public List<OrderListModel> parseJSON(String paramString) throws JSONException {
        Logger.d(TAG, "解析OrderList订单列表数据");
        if(paramString == null){
            return null;
        }else{
            JSONObject json = new JSONObject(paramString);
            String result = json.getString("response");
            if(result!=null && !result.equals("error")){
                String orderlist = json.getString("orderlist");
                // 将json转换成List,使用Gson
                Gson gson = new Gson();
                List<OrderListModel> list = gson.fromJson(orderlist, new TypeToken<List<OrderListModel>>(){}.getType());
                // 将json转换成List,使用fastJson
//              List<OrderListModel> list = JSON.parseArray(orderlist, OrderListModel.class);
                return list;
            }else{
                return null;
            }
        }
    }
}
