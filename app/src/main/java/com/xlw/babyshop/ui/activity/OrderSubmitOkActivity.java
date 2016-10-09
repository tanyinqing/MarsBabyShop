package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xlw.babyshop.model.OrderForSubmitModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.OrderForSubmitParser;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderSubmitOkActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "OrderSubmitOKActivity";

    private TextView orderid_value_text;
    private TextView paymoney_value_text;
    private TextView paytype_value_text;
    private TextView continue_shoping_text;
    private TextView to_ordr_detail_text;

    RequestModel requestModel;
    OrderForSubmitModel serverResponseData;  // 从服务器端返回的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordr_submit_ok_activity);

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
        orderid_value_text = (TextView) this.findViewById(R.id.orderid_value_text);
        paymoney_value_text = (TextView) this.findViewById(R.id.paymoney_value_text);
        paytype_value_text = (TextView) this.findViewById(R.id.paytype_value_text);
        continue_shoping_text = (TextView) this.findViewById(R.id.continue_shoping_text);
        to_ordr_detail_text = (TextView) this.findViewById(R.id.to_ordr_detail_text);

        continue_shoping_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        to_ordr_detail_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(OrderSubmitOkActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadViewLayout() {
        setTitle(R.string.uphandsuccess);
        setHeadLeftVisibility(View.INVISIBLE);
    }

    private void processLogic() {
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回String[]类型
        BaseJSONParser<OrderForSubmitModel> jsonParser = new OrderForSubmitParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.ordersumbit);

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "ordersumbit"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    private void setListener() {
    }

    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseData = (OrderForSubmitModel)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseData.getOrderid() + "-订单id");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseData != null) {
            orderid_value_text.setText(serverResponseData.orderid);
            paymoney_value_text.setText(serverResponseData.price);
			//支付方式
            String type = serverResponseData.getPaymenttype().trim();
            if("1".equals(type)){
                paytype_value_text.setText("货到付款");
            }else if("2".equals(type)){
                paytype_value_text.setText("货到POS机");
            }else if("3".equals(type)){
                paytype_value_text.setText("支付宝");
            }
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {

    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {

    }
}
