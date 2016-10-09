package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.AddressDetailModel;
import com.xlw.babyshop.model.AddressModel;
import com.xlw.babyshop.model.CartProductModel;
import com.xlw.babyshop.model.CheckoutAddupModel;
import com.xlw.babyshop.model.DeliveryModel;
import com.xlw.babyshop.model.InvoiceInfoModel;
import com.xlw.babyshop.model.PaymentModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.PaymentCenterParser;
import com.xlw.babyshop.ui.adapter.PaymentCentUpdateAdapter;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentCenterActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = null;
    private List<CartProductModel> productlistInfo;
    private ListView payment_product_list;

    //收货地址
    private TextView textAdress2;
    private TextView textAdress3;
    private TextView textAdress4;
    //支付方式
    private TextView payment_payValue_text;
    //送货时间
    private TextView payment_sendTimeValue_text;
    //发票
    private TextView payment_invoiceValue_text;
    //留言
    private TextView payment_remarkView_text;
    //数量总计
    private TextView shopcar_total_buycount_text;
    //赠送总积分
    private TextView shopcar_total_bonus_text;
    //金额总计
    private TextView shopcar_total_money_text;
    //提交按钮
    private TextView ordr_submit_bottom_text;

    RequestModel requestModel;
    Map<String,Object> serverResponseDataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_payment_center_activity);

        initView();
    }

    private void initView() {
//        if (isLoadBottomTab()) {
//            View currentView = getWindow().getDecorView();
//            loadBottomTab(currentView);    // 加载BottomTab,为BottomTab上的ImageView添加监听器
//            selectedBottomTab(Constant.CLASSIFY);   // 默认选中第二个选项
//        }
        findViewById();     // 查找各个组件
        loadViewLayout();   // 设置ActionBar视图
        setListener();      // 设置监听器材
        processLogic();     // 向服务器端请求数据,并处理返回的数据
    }

    private void findViewById() {
        payment_product_list = (ListView) this.findViewById(R.id.payment_product_list);
        ordr_submit_bottom_text = (TextView) this.findViewById(R.id.ordr_submit_bottom_text);
        // 收件人信息
        textAdress2 = (TextView) this.findViewById(R.id.textAdress2);//收件人
        textAdress3 = (TextView) this.findViewById(R.id.textAdress3);//收件人地址
        textAdress4 = (TextView) this.findViewById(R.id.textAdress4);//详细地址

        //支付方式
        payment_payValue_text = (TextView) this.findViewById(R.id.payment_payValue_text);
        //送货时间
        payment_sendTimeValue_text = (TextView) this.findViewById(R.id.payment_sendTimeValue_text);
        //发票
        payment_invoiceValue_text = (TextView) this.findViewById(R.id.payment_invoiceValue_text);
        //留言
        payment_remarkView_text = (TextView) this.findViewById(R.id.payment_remarkView_text);
        //数量总计
        shopcar_total_buycount_text = (TextView) this.findViewById(R.id.shopcar_total_buycount_text);
        //赠送总积分
        shopcar_total_bonus_text = (TextView) this.findViewById(R.id.shopcar_total_bonus_text);
        //金额总计
        shopcar_total_money_text = (TextView) this.findViewById(R.id.shopcar_total_money_text);

        findViewById(R.id.select_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳到选择地址页面去
                Intent intent = new Intent(PaymentCenterActivity.this,SelectAddressActivity.class);
                startActivityForResult(intent, 2000);
            }
        });
    }

    private void loadViewLayout() {
        setHeadRightVisibility(View.VISIBLE);
        setHeadRightText(R.string.uphandcheckout);
        setTitle(R.string.check_out);
    }

    private void processLogic() {
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回String[]类型
        BaseJSONParser<Map<String,Object>> jsonParser = new PaymentCenterParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.checkout);
        // 提交到服务器端的订单数据
        HashMap<String, String> datas = new HashMap<>();
        datas.put("sku", "1200001:3|1200004:2");

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置提交的订单数据
        requestModel.setRequestDataMap(datas);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "checkout"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
//        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
        volleyUtil.postRequest(requestModel,reqDataUrl);       // 向服务器异步提交数据
    }

    private void setListener() {
        // 提交订单
        ordr_submit_bottom_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PaymentCenterActivity.this,OrderSubmitOkActivity.class);
                startActivity(intent);
            }
        });
    }

    // 当异步请求信息从服务器端返回时,回调此方法
    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseDataMap = (Map<String,Object>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataMap.size() + "");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataMap != null && serverResponseDataMap.size()>0) {
            AddressModel address_info = (AddressModel) serverResponseDataMap.get("addressInfo");
            PaymentModel paymentInfo = (PaymentModel) serverResponseDataMap.get("paymentInfo");
            DeliveryModel deliveryInfo = (DeliveryModel) serverResponseDataMap.get("deliveryInfo");
            InvoiceInfoModel invoiceInfo = (InvoiceInfoModel) serverResponseDataMap.get("invoiceInfo");
            productlistInfo = (List<CartProductModel>) serverResponseDataMap.get("productlistInfo");
            List<String> checkout = (List<String>) serverResponseDataMap.get("checkout");
            CheckoutAddupModel checkoutAdd = (CheckoutAddupModel) serverResponseDataMap.get("checkoutAdd");
            // 收件人信息
            textAdress2.setText(address_info.getName()+"");
            textAdress3.setText(address_info.getAddress_area()+"");
            textAdress4.setText(address_info.getAddress_detail()+"");
            //支付方式
            if(paymentInfo.getType()==1){
                payment_payValue_text.setText("货到付款");
            }else if(paymentInfo.getType()==2){
                payment_payValue_text.setText("货到POS机");
            }else if(paymentInfo.getType()==3){
                payment_payValue_text.setText("支付宝");
            }
            //送货时间
            if(deliveryInfo.getType()==1){
                payment_sendTimeValue_text.setText("周一至周五送货");
            }else if(deliveryInfo.getType()==2){
                payment_sendTimeValue_text.setText("双休日及公众假期送货");
            }else if(deliveryInfo.getType()==3){
                payment_sendTimeValue_text.setText("时间不限，工作日双休日及公众假期均可送货");
            }
            //发票
            payment_invoiceValue_text.setText(invoiceInfo.getTitle()+"");
            //留言
            payment_remarkView_text.setText(invoiceInfo.getContent()+"");
            //数量总计
            shopcar_total_buycount_text.setText(checkoutAdd.getTotal_count()+"");
            //总积分
            shopcar_total_bonus_text.setText(checkoutAdd.getTotal_point()+"");
            //金额总计
            shopcar_total_money_text.setText(checkoutAdd.getTotal_price()+"");

            PaymentCentUpdateAdapter adapter = new PaymentCentUpdateAdapter(PaymentCenterActivity.this,productlistInfo);
            payment_product_list.setAdapter(adapter);
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {
    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == 200) {
            AddressDetailModel parcelableExtra = data.getParcelableExtra("address");
            Logger.d(TAG, "已选择地址" + parcelableExtra.toString());
        }

    }

    @Override
    protected void onHeadRightButton(View v) {
        finish();
        Intent intent = new Intent(this,OrderSubmitOkActivity.class);
        startActivity(intent);
    }

}
