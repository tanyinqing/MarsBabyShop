package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xlw.babyshop.model.AddressDetailModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.AddressManageParser;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.ui.adapter.SelectAdressAdapter;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 地址选择，点击栏目返回<br>
 * data.getParcelableExtra("address") 获取数据 为AddressDetail 对象
 */
public class SelectAddressActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "SelectAddressActivity";

    private ListView addressItemlv;
    private SelectAdressAdapter mAdapter;

    RequestModel requestModel;
    List<AddressDetailModel> serverResponseDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manage_activity);

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
        addressItemlv = (ListView) findViewById(R.id.address_manage_list);

        mAdapter = new SelectAdressAdapter(this);
        addressItemlv.setAdapter(mAdapter);
    }

    private void loadViewLayout() {
        setTitle(R.string.select_address);
        setHeadLeftBackgroundResource(R.drawable.new_head_normal_selector);
        setHeadLeftText(R.string.check_out);
        setHeadRightText(R.string.address_manage);
        setHeadRightVisibility(View.VISIBLE);
    }

    private void processLogic() {
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回String[]类型
        BaseJSONParser<List<AddressDetailModel>> jsonParser = new AddressManageParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.url_addresslist);

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "addresslist"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    private void setListener() {
        addressItemlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("address", mAdapter.getItem(position));
                setResult(200, data);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {
            processLogic();
        }
    }

    @Override
    protected void onHeadRightButton(View v) {
        startActivityForResult(new Intent(this, AddressManageActivity.class), 200);
    }

    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseDataList = (List<AddressDetailModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataList.size() + "");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            mAdapter.addAll(serverResponseDataList);
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {

    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {

    }

    // 单击R.id.head_back_text:
//    finish();


}
