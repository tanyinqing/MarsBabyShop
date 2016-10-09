package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.ProductDetailModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class ProductDetailParser extends BaseJSONParser<ProductDetailModel> {

    @Override
    public ProductDetailModel parseJSON(String paramString) throws JSONException {
        if(super.checkResponse(paramString)!=null){
            JSONObject jsonObject = new JSONObject(paramString);
            String product = jsonObject.getString("product");
            ProductDetailModel productDetail = JSON.parseObject(product,ProductDetailModel.class);
            return productDetail;
        }
        return null;
    }
}
