package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.SearchRecommondParser;
import com.xlw.babyshop.ui.adapter.SearchAdapter;
import com.xlw.babyshop.utils.CommonUtil;
import com.xlw.babyshop.utils.Constant;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "SearchActivity";

    private EditText keyWordEdit;
    private ListView hotWordsLv;
//    private String[] search;
    private String[] serverResponseData;    // 从服务器端返回的数据

    private RequestModel requestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initView();
    }

    private void initView() {
        if (isLoadBottomTab()) {
            View currentView = getWindow().getDecorView();
            loadBottomTab(currentView);    // 加载BottomTab,为BottomTab上的ImageView添加监听器
            selectedBottomTab(Constant.CLASSIFY);   // 默认选中第二个选项
        }
        findViewById();     // 查找各个组件
        loadViewLayout();   // 设置ActionBar视图
        setListener();      // 设置监听器材
        processLogic();     // 向服务器端请求数据,并处理返回的数据
    }

    private void findViewById() {
        hotWordsLv = (ListView) findViewById(R.id.hotWordsLv);
        keyWordEdit = (EditText) findViewById(R.id.keyWordEdit);
    }

    private void loadViewLayout() {
        setHeadLeftVisibility(View.INVISIBLE);
        setTitle("搜索");
        setHeadRightText("搜索");
        setHeadRightVisibility(View.VISIBLE);
        selectedBottomTab(Constant.SEARCH);
    }

    private void processLogic() {
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回String[]类型
        BaseJSONParser<String[]> jsonParser = new SearchRecommondParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.searchRecommend);
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

    private void setListener() {
        hotWordsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    protected void onHeadRightButton(View v) {
        String keyWord = keyWordEdit.getText().toString();
        if(keyWord==null||"".equals(keyWord)){
            CommonUtil.showInfoDialog(SearchActivity.this, "请输入关键字");
            return;
        }
        Intent intent = new Intent(SearchActivity.this,SearchProductListActivity.class);
        intent.putExtra("keyword", keyWord);
        startActivity(intent);
    }

    // 当异步请求信息从服务器端返回时,回调此方法
    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseData = (String[])requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseData.toString());
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseData != null) {
            SearchAdapter adapter = new SearchAdapter(SearchActivity.this, serverResponseData);
            hotWordsLv.setAdapter(adapter);
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
