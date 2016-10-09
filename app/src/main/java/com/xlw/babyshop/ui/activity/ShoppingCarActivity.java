package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.AddupModel;
import com.xlw.babyshop.model.CartModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.ShoppingCarParser;
import com.xlw.babyshop.ui.adapter.ShoppingCarAdapter;
import com.xlw.babyshop.utils.Constant;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingCarActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    protected static final String TAG = "ShoppingCarActivity";

    private ListView shopcar_product_list;

    private TextView shopcar_total_buycount_text_1; //数量总计
    private TextView shopcar_total_bonus_text_1;    //赠送积分总计
    private TextView shopcar_total_money_text_1;    //金额总计(不含运费)
    private String productId;

    RequestModel requestModel;
    CartModel serverResponseData;       // 服务器端返回的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_car_activity);

        initView();
    }

    private void initView() {
        if (isLoadBottomTab()) {
            View currentView = getWindow().getDecorView();
            loadBottomTab(currentView);    // 加载BottomTab,为BottomTab上的ImageView添加监听器
            selectedBottomTab(Constant.SHOPCAR);   // 默认选中第二个选项
        }
        findViewById();     // 查找各个组件
        loadViewLayout();   // 设置ActionBar视图
        setListener();      // 设置监听器材
        processLogic();     // 向服务器端请求数据,并处理返回的数据
    }

    private void findViewById() {
        shopcar_product_list = (ListView) findViewById(R.id.shopcar_product_list);
        shopcar_total_buycount_text_1= (TextView) findViewById(R.id.shopcar_total_buycount_text_1);
        shopcar_total_bonus_text_1= (TextView) findViewById(R.id.shopcar_total_bonus_text_1);
        shopcar_total_money_text_1= (TextView) findViewById(R.id.shopcar_total_money_text_1);
        findViewById(R.id.shopcar_bottom_toPay_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳到支付中心去
                startActivity(new Intent(ShoppingCarActivity.this, PaymentCenterActivity.class));
                finish();
            }
        });
    }

    private void loadViewLayout() {
        setTitle("购物车");
        selectedBottomTab(Constant.SHOPCAR);
    }

    private void processLogic() {
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回String[]类型
        BaseJSONParser<CartModel> jsonParser = new ShoppingCarParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.cart);
        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "cart"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    private void setListener() {
    }

    // 当异步请求信息从服务器端返回时,回调此方法
    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseData = (CartModel)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseData.toString());
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseData != null && serverResponseData.productlist.size()>0) {
            ShoppingCarAdapter adapter = new ShoppingCarAdapter(ShoppingCarActivity.this, serverResponseData);
            shopcar_product_list.setAdapter(adapter);
            AddupModel addup = serverResponseData.cart_addup ;
            shopcar_total_buycount_text_1.setText(addup.total_count + "");
            shopcar_total_bonus_text_1.setText(addup.total_point + "");
            shopcar_total_money_text_1.setText(addup.total_price + "");
        } else {
            ToastUtil.showShortMsg(this, "暂无数据");
            setContentView(R.layout.shopping_none_car_activity);
            View currentView = getWindow().getDecorView();
            loadBottomTab(currentView);    // 加载BottomTab,为BottomTab上的ImageView添加监听器
            selectedBottomTab(Constant.SHOPCAR);
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {
    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {
    }

}
