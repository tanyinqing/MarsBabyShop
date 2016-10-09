package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.OrderForSubmitModel;
import com.xlw.babyshop.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class OrderForSubmitParser extends BaseJSONParser<OrderForSubmitModel>{

    private static final String TAG = "OrderForSubmitParser";

    @Override
    public OrderForSubmitModel parseJSON(String paramString) throws JSONException {
        JSONObject json = new JSONObject(paramString);
        Logger.d(TAG, "解析OrderList订单列表数据");
        String response = json.getString("response");
        if(response==null){
            Logger.d(TAG, "获取数据失败");
            return null;
        }else{
            String orderinfo = json.getString("orderinfo");
            if(orderinfo!=null){
                OrderForSubmitModel order = JSON.parseObject(orderinfo, OrderForSubmitModel.class);
                System.out.println(order.getOrderid());
                System.out.println(order.getPaymenttype());
                System.out.println(order.getPrice());
                return order;
            }else{
                Logger.d(TAG, "orderinfo解析失败");
            }
        }
        return null;
    }
}
