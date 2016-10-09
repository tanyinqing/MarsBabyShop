package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.model.UserModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.SuccessParser;
import com.xlw.babyshop.parser.UserinfoParser;
import com.xlw.babyshop.utils.Constant;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "AccountActivity";

    private TextView my_name_text; // 用户名
    private TextView my_bonus_text; // 积分
    private TextView my_level_text; // 等级
    private LinearLayout ll_account_myorder; // 我的订单
    private LinearLayout ll_account_address_manage; // 地址管理
    private LinearLayout ll_account_conservation; // 收藏夹
    private SharedPreferences sp;

    RequestModel requestModel;
    UserModel serverResponseData;  // 从服务器端返回的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_activity);

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
        my_name_text = (TextView) this.findViewById(R.id.my_name_text);
        my_bonus_text = (TextView) this.findViewById(R.id.my_bonus_text);
        my_level_text = (TextView) this.findViewById(R.id.my_level_text);
        ll_account_myorder = (LinearLayout) this.findViewById(R.id.ll_account_myorder);
        ll_account_address_manage = (LinearLayout) this.findViewById(R.id.ll_account_address_manage);
        ll_account_conservation = (LinearLayout) this.findViewById(R.id.ll_account_conservation);
    }

    private void loadViewLayout() {
        setHeadRightVisibility(View.VISIBLE);
        setHeadRightText("退出");
        selectedBottomTab(Constant.MORE);
        setTitle(R.string.my_account_title);
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
    }

    private void processLogic() {
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回UserModel类型
        BaseJSONParser<UserModel> jsonParser = new UserinfoParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.userinfo);

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "userinfo"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    private void setListener() {
        ll_account_myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "跳转我的订单activity");
                Intent orderIntent = new Intent(AccountActivity.this, OrderListActivity.class);
                orderIntent.putExtra("totoalOrderCount", serverResponseData.getOrdercount());
                startActivity(orderIntent);
            }
        });
        ll_account_address_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "跳转地址管理activity");
                Intent addressManagerIntent = new Intent(AccountActivity.this, AddressManageActivity.class);
                startActivity(addressManagerIntent);
            }
        });
        ll_account_conservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "跳转收藏夹activity");
                Intent myFavoriteIntent = new Intent(AccountActivity.this, MyFavoriteActivity.class);
                myFavoriteIntent.putExtra("totalFavoriteCount", serverResponseData.getFavoritescount());
                startActivity(myFavoriteIntent);
            }
        });
    }

    @Override
    protected void onHeadRightButton(View v) {  // 退出登录
        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回Boolean类型
        BaseJSONParser<Boolean> jsonParser = new SuccessParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.url_logout);

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "logout"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(new VolleyUtil.ResponseCallback() {
            @Override
            public void responseString(String responseText) {
                // 退出登录后,回到主目录
                startActivity(new Intent(AccountActivity.this, HomeActivity.class));
            }

            @Override
            public void responseJSONObject(JSONObject jsonObject) {

            }

            @Override
            public void responseJsonArray(JSONArray jsonArray) {

            }
        });       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseData = (UserModel)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseData.getUserId() + "-用户id");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseData != null) {
            my_bonus_text.setText(serverResponseData.getBonus() + "");
            my_level_text.setText(serverResponseData.getLevel() + "");
            String username = sp.getString("userName", "");
            Logger.d(TAG, "userName:"+username);
            my_name_text.setText(username);
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {

    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {

    }
}
