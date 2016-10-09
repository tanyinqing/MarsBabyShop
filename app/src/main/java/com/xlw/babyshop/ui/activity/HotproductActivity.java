package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xlw.babyshop.model.ProductListModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.ProductListParser;
import com.xlw.babyshop.ui.adapter.ProductAdapter;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class HotproductActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "HotproductActivity";

    private ListView listView;

    private RequestModel requestModel;
    private List<ProductListModel> serverResponseDataList;   // 从服务器返回的数据集合
//    private List<ProductListModel> List;

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
        setListener();      // 添加事件监听器
        processLogic();     // 向服务器端请求数据,并填充到Adapter中
    }

    private void findViewById() {
        listView = (ListView) findViewById(R.id.promBulldtinLv);
        //listView = (ListView) findViewById(R.id.productList);
    }

    //加载布局文件
    private void loadViewLayout() {
        setTitle("热卖单品");
    }

    //执行逻辑
    private void processLogic() {
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回HomeBannerModel类型
        BaseJSONParser<List<ProductListModel>> jsonParser = new ProductListParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.url_hotproduct);
        // 设置向服务器端传递的数据
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

        // 向服务器端请求数据,并设置到Galley的适配器中
        // "http://10.0.3.2:8084/redbaby/" + "hotproduct"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
//        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
        volleyUtil.postRequest(requestModel, reqDataUrl);       // 向服务器异步提交数据
    }

    //设置监听事件
    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductListModel vo = (ProductListModel) listView.getItemAtPosition(position);
                //String prodId = vo.getId()+"";
                Intent producutlistIntent = new Intent(HotproductActivity.this,ProductDetailActivity.class);
                //将ID传递到商品分类显示中，显示相关内容
                producutlistIntent.putExtra("id",  vo.getId());
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
            serverResponseDataList = (List<ProductListModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataList.size() + "条记录");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            //获得数据后进行listView数据的填充
            ProductAdapter newproductAdapter = new ProductAdapter(HotproductActivity.this, serverResponseDataList);
            listView.setAdapter(newproductAdapter);
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {

    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {

    }

}
