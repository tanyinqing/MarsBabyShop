package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xlw.babyshop.model.LimitbuyModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.LimitbuyParser;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
import com.xlw.babyshop.ui.adapter.LimitbuyAdapter;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class LimitbuyActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "LimitbuyActivity";

    private ListView listView;
    LimitbuyAdapter limitbuyAdapter;

    private RequestModel requestModel;
    private List<LimitbuyModel> serverResponseDataList;   // 从服务器返回的数据集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prom_bulletin_activity);

        initView();
    }

    private void initView() {
//        if (isLoadBottomTab()) {
//            View currentView = getWindow().getDecorView();
//            loadBottomTab(currentView);    // 加载BottomTab,为BottomTab上的ImageView添加监听器
//            selectedBottomTab(DEFAULT_INDEX);   // 默认选中第一个选项
//        }
        loadViewLayout();   // 设置ActionBar view
        findViewById();     // 查找首页各个组件
        setListener();      // 为画廊和商品列表添加事件监听器
        processLogic();     // 向服务器端请求数据,并填充到Gallery和CategoryList的Adapter中
    }

    private void findViewById() {
        listView = (ListView) findViewById(R.id.promBulldtinLv);
    }

    private void loadViewLayout() {
        setTitle("限时抢购");
    }

    private void processLogic() {
        // 向服务器端请求商品分类数据
        getDataFromServer();        // 向服务器端请求数据
    }

    private void getDataFromServer(){
        showProgressDialog();

        // 这里执行异步任务类
        NetCheckAsyncTask netCheckTask = new NetCheckAsyncTask(this);
        netCheckTask.setCallBack(new NetCheckAsyncTask.CallBack() {
            @Override
            public void handleNetCheckResponse(Boolean aBoolean) {
                if (aBoolean) {
                    // 如果可以联上互联网,就请求数据
                    requestDataFromServer();
                } else {
                    // 如果无法联上互联网
                    ToastUtil.showLongMsg(LimitbuyActivity.this, "无法连接互联网");
                }
            }
        });
        netCheckTask.execute();
    }

    private void requestDataFromServer(){
        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回HomeBannerModel类型
        BaseJSONParser<List<LimitbuyModel>> jsonParser = new LimitbuyParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.url_limitbuy);
        // 向服务器提交的分页数据
        HashMap<String, String> requestDataMap = new HashMap<String, String>();
        requestDataMap.put("page", "");
        requestDataMap.put("pageNum", "");
        //requestDataMap.put("orderby", "sale_down");

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置提交的数据
        requestModel.setRequestDataMap(requestDataMap);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8080/MarsShop/" + "limitbuy"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
//        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
        volleyUtil.postRequest(requestModel,reqDataUrl);       // 向服务器异步提交数据
    }

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LimitbuyModel vo = (LimitbuyModel) listView.getItemAtPosition(position);
                Intent producutlistIntent = new Intent(LimitbuyActivity.this,ProductDetailActivity.class);
                //将ID传递到商品分类显示中，显示相关内容
                producutlistIntent.putExtra("id", vo.getId());
                //跳转到新的activity
                startActivity(producutlistIntent);
            }
        });
    }

    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseDataList = (List<LimitbuyModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataList.size() + "条记录");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            limitbuyAdapter = new LimitbuyAdapter(LimitbuyActivity.this, serverResponseDataList);
            listView.setAdapter(limitbuyAdapter);
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {

    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {

    }
}
