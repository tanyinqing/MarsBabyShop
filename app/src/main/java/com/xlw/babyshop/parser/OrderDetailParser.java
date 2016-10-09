package com.xlw.babyshop.parser;

import com.alibaba.fastjson.JSON;
import com.xlw.babyshop.model.AddressModel;
import com.xlw.babyshop.model.CartProductModel;
import com.xlw.babyshop.model.CheckoutAddupModel;
import com.xlw.babyshop.model.DeliveryModel;
import com.xlw.babyshop.model.InvoiceInfoModel;
import com.xlw.babyshop.model.OrderInfoModel;
import com.xlw.babyshop.model.PaymentModel;
import com.xlw.babyshop.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinliwei on 2015/7/23.
 */
public class OrderDetailParser extends BaseJSONParser<Map<String,Object>>{

    private static final String TAG = "OrderDetailParser";

    @Override
    public Map<String,Object> parseJSON(String paramString) throws JSONException {
        Logger.d(TAG, "解析orderdetail订单详细信息");
        Map<String,Object> map = new HashMap<String, Object>();

        JSONObject json = new JSONObject(paramString);
        String order = json.getString("order_info");
        OrderInfoModel orderInfo = JSON.parseObject(order,OrderInfoModel.class);
        map.put("orderInfo", orderInfo);
        Logger.d(TAG, "解析orderInfo 成功");

        String address = json.getString("address_info");
        AddressModel addressInfo = JSON.parseObject(address, AddressModel.class);
        map.put("addressInfo", addressInfo);
        Logger.d(TAG, "解析addreInfo 成功");

        String payment = json.getString("payment_info");
        PaymentModel paymentInfo = JSON.parseObject(payment, PaymentModel.class);
        map.put("paymentInfo", paymentInfo);
        Logger.d(TAG, "解析paymentInfo 成功");

        String delivery = json.getString("delivery_info");
        DeliveryModel deliveryInfo = JSON.parseObject(delivery, DeliveryModel.class);
        map.put("deliveryInfo", deliveryInfo);
        Logger.d(TAG, "解析deliveryInfo 成功");

        String invoice = json.getString("invoice_info");
        InvoiceInfoModel invoiceInfo = JSON.parseObject(invoice, InvoiceInfoModel.class);
        map.put("invoiceInfo", invoiceInfo);
        Logger.d(TAG, "解析invoiceInfo 成功");

        String productlist = json.getString("productlist");
        List<CartProductModel> productlistInfo = JSON.parseArray(productlist, CartProductModel.class);
        map.put("productlistInfo", productlistInfo);
        Logger.d(TAG, "解析productlistInfo 成功");

        String checkout_prom = json.getString("checkout_prom");
        List<String> checkoutList = JSON.parseArray(checkout_prom, String.class);
        map.put("checkout", checkoutList);
        Logger.d(TAG, "解析checkout 成功");

        String checkout_addup = json.getString("checkout _addup");
        CheckoutAddupModel checkoutAdd = JSON.parseObject(checkout_addup,CheckoutAddupModel.class);
        map.put("checkoutAdd", checkoutAdd);
        Logger.d(TAG, "解析checkoutAdd 成功");

        return map;
    }
}
