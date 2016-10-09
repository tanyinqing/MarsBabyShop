package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.CartModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class ShoppingCarParser extends BaseJSONParser<CartModel>{
    @Override
    public CartModel parseJSON(String paramString) throws JSONException {
        if(checkResponse(paramString)!=null){
            CartModel cart= new CartModel();


            JSONObject jsonObject = new JSONObject(paramString);

            String cartstr = jsonObject.getString("cart");
            cart = JSON.parseObject(cartstr, CartModel.class);

            return cart;
        }
        return null;
    }
}
