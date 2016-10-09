package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.CategoryModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.CategoryParser;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
import com.xlw.babyshop.ui.adapter.CategoryAdapter;
import com.xlw.babyshop.utils.Constant;
import com.xlw.babyshop.utils.DivideCategoryList;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CategoryActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "CategoryActivity";
    private final static Integer DEFAULT_INDEX = 2;

    private ListView lv_category_list;
    private TextView tv_category_empty;

    private RequestModel requestModel;      // 数据请求模型
    private List<CategoryModel> serverResponseDataList;   // 从服务器返回的数据集合
    List<CategoryModel> oneInfos ;

    // 获得指定子类别的工具类
    private DivideCategoryList divide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        initView();
    }

    private void initView() {
        if (isLoadBottomTab()) {
            View currentView = getWindow().getDecorView();
            loadBottomTab(currentView);    // 加载BottomTab,为BottomTab上的ImageView添加监听器
            selectedBottomTab(Constant.CLASSIFY);   // 默认选中第二个选项
        }
        findViewById();     // 查找各个组件
        loadViewLayout();   // 设置
        setListener();      // 添加事件监听器
        processLogic();     // 向服务器端请求数据,并填充到Adapter中
    }

    private void findViewById() {
        // 商品显示的ListView
        lv_category_list = (ListView) findViewById(R.id.categoryList);
        tv_category_empty = (TextView) findViewById(R.id.categoryEmptyListTv);
    }

    private void loadViewLayout() {
        setTitle(R.string.category_view);
        setHeadLeftVisibility(View.INVISIBLE);
        selectedBottomTab(Constant.CLASSIFY);
    }

    private void setListener() {
        // 为ListView添加item单击选择事件监听器
        lv_category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryModel item = (CategoryModel) parent.getItemAtPosition(position);
                String oneLevelID = item.getId();
                ToastUtil.showLongMsg(CategoryActivity.this, "oneLevelID:" + oneLevelID);
                if (oneLevelID != null) {
                    // 当单击一个item时
                    if (item.isIsleafnode()) {
                        // 如果是叶子节点,则转向该叶子类别的产品列表界面
                        Intent prodIntent = new Intent(CategoryActivity.this, ProductListActivity.class);
                        prodIntent.putExtra("cId", oneLevelID);
                        startActivity(prodIntent);
                        ToastUtil.showLongMsg(CategoryActivity.this, "转向ProductListActivity.class");
                    } else {
                        // 如果不是叶子节点,则转向二级类别界面
//                        Intent twoLevelIntent = new Intent(CategoryActivity.this, CategoryTwoLevelActivity.class);
//                        twoLevelIntent.putExtra("oneLevelID", oneLevelID);
//                        startActivity(twoLevelIntent);
                        ToastUtil.showLongMsg(CategoryActivity.this, "CategoryTwoLevelActivity.class");
                    }

                } else {
                    ToastUtil.showLongMsg(getApplicationContext(), "数值没有传递成功");
                }
            }
        });
    }

    private void processLogic() {
        // 向服务器端请求商品分类数据
        getDataFromServer();        // 向服务器端请求数据
    }

    private void getDataFromServer(){
        showProgressDialog();   // 显示进度条

        // 这里执行异步任务类
        NetCheckAsyncTask netCheckTask = new NetCheckAsyncTask(this);
        netCheckTask.setCallBack(new NetCheckAsyncTask.CallBack() {
            @Override
            public void handleNetCheckResponse(Boolean aBoolean) {
                closeProgressDialog();  // 关闭进度条
                if (aBoolean) {
                    // 如果可以联上互联网,就请求数据
                    requestDataFromServer();
                } else {
                    // 如果无法联上互联网
                    ToastUtil.showLongMsg(CategoryActivity.this, "无法连接互联网");
                }
            }
        });
        netCheckTask.execute();
    }

    /*
    准备的东西:
        有服务器的URL地址;
        对返回的数据进行解析的JSON解析器 - List<商品分类>
        建立一个"商品分类"的数据模型类-域对象
        构建一个适配器(Adapter),将返回的List<商品分类>填充到适配器中
     */
    private void requestDataFromServer(){
        showProgressDialog();   // 显示一个进度条

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回HomeBannerModel类型
        BaseJSONParser<List<CategoryModel>> jsonParser = new CategoryParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.category);
        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "category"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    // 当异步请求信息从服务器端返回时,回调此方法
    @Override
    public void responseString(String responseText) {
        closeProgressDialog();  // 关闭进度条
        Logger.d(TAG, responseText);

        try {
            // 解析
            serverResponseDataList = (List<CategoryModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataList.size() + "");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            divide = new DivideCategoryList(serverResponseDataList);
            oneInfos = divide.getOneLevel();        // 获得一级商品分类

            CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, oneInfos);
            tv_category_empty.setVisibility(View.INVISIBLE);
            lv_category_list.setAdapter(adapter);   // 显示一级商品分类
        } else {
            ToastUtil.showShortMsg(this, "暂无数据");
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {
    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {
    }
}
