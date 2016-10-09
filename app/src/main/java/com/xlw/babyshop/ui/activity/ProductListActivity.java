package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.ProductListModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.ProductListParser;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
import com.xlw.babyshop.ui.adapter.ProductLvAdapter;
import com.xlw.babyshop.utils.Constant;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "ProductListActivity";

    private TextView tv_prodlist_ranksale;
    private TextView tv_prodlist_rankprice;
    private TextView tv_prodlist_rankgood;
    private TextView tv_prodlist_ranktime;
    private ListView lv_prodlist_listprod;
    private ImageView iv_prodlist_noresult;
    private TextView tv_prodlist_noresult;

    private SharedPreferences sp;
    boolean rankUp;
    private String cId ;
    private String siftsize;
    private String filterData;
    private String orderby;

    ////////////////////
    private Map<String, Object> mapAllResult;
    /////////////////

    private RequestModel requestModel;
    private List<ProductListModel> serverResponseDataList;  // 服务器返回的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);

        /*
         * 调用超类的方法,查找BottomTab中的各图片按钮,并添加事件
         * 然后依次调用loadViewLayout()、findViewById()、processLogic()、setListener()
         */
        initView();
    }

    private void initView() {
        if (isLoadBottomTab()) {
            View currentView = getWindow().getDecorView();
            loadBottomTab(currentView);    // 加载BottomTab,为BottomTab上的ImageView添加监听器
            selectedBottomTab(Constant.CLASSIFY);   // 默认选中第二个选项
        }
        findViewById();     // 查找首页各个组件
        loadViewLayout();   // 设置ActionBar视图
        setListener();      // 为组件添加事件监听器
        processLogic();     // 向服务器端请求数据,并填充到相应的组件中
    }

    private void findViewById() {
        tv_prodlist_ranksale = (TextView) findViewById(R.id.textRankSale);
        tv_prodlist_rankprice = (TextView) findViewById(R.id.textRankPrice);
        tv_prodlist_rankgood = (TextView) findViewById(R.id.textRankGood);
        tv_prodlist_ranktime = (TextView) findViewById(R.id.textRankTime);
        lv_prodlist_listprod = (ListView) findViewById(R.id.productList);
        iv_prodlist_noresult = (ImageView) findViewById(R.id.listProduct);
        tv_prodlist_noresult = (TextView) findViewById(R.id.textNull);
    }

    private void loadViewLayout() {
        setTitle("产品列表");
        setHeadRightText("筛选");
        setHeadRightVisibility(View.VISIBLE);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        rankUp = sp.getBoolean("productrank", false);
        if(rankUp == false){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("productrank", false);
            editor.commit();
        }

        selectedBottomTab(Constant.CLASSIFY);
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
                    ToastUtil.showLongMsg(ProductListActivity.this, "无法连接互联网");
                }
            }
        });
        netCheckTask.execute();
    }

    private void requestDataFromServer(){
        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回List<ProductListModel>类型
        BaseJSONParser<List<ProductListModel>> jsonParser = new ProductListParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.prodList);
        // 请求的分页数据
        HashMap<String, String> prodMap = new HashMap<String, String>();
        prodMap.put("page", "1");
        prodMap.put("pageNum", "8");
        prodMap.put("cId", getIntent().getStringExtra("cId"));

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置请求参数
        requestModel.setRequestDataMap(prodMap);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "productlist"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    private void setListener() {
        lv_prodlist_listprod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemID = serverResponseDataList.get(position).getId();
                //ProductDetailActivity
                Intent detailIntent = new Intent(ProductListActivity.this,ProductDetailActivity.class);
                detailIntent.putExtra("id", itemID);
                Logger.i(TAG, itemID+"");
                startActivity(detailIntent);
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
            serverResponseDataList = (List<ProductListModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataList.size() + "");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            ProductLvAdapter adapter = new ProductLvAdapter(ProductListActivity.this, serverResponseDataList);
            lv_prodlist_listprod.setAdapter(adapter);
        } else {
            ToastUtil.showShortMsg(this, "暂无数据");
            tv_prodlist_noresult.setVisibility(View.VISIBLE);
            iv_prodlist_noresult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {
    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {
    }
}
