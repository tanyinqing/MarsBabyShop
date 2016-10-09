package com.xlw.babyshop.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xlw.babyshop.model.AddressDetailModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.AddressManageParser;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.SuccessParser;
import com.xlw.babyshop.ui.adapter.AddressManageAdapter;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressManageActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "AddressManageActivity";

    private ListView addressItemlv;
    private AddressManageAdapter mAdapter;

    RequestModel requestModel;
    List<AddressDetailModel> serverResponseDataList;    // 从服务器端返回的数据集合

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
        mAdapter = new AddressManageAdapter(this);
        mAdapter.setListener(new AddressManageAdapter.OnItemButtonListener(){
            @Override
            public void onItemClick(View view, final int position) {
                switch (view.getId()) {
                    case R.id.address_manage_update_btn:// 修改
                        Intent intent = new Intent(AddressManageActivity.this, AddAddressActivity.class);
                        intent.putExtra("address", mAdapter.getItem(position));
                        startActivityForResult(intent, 200);
                        break;
                    case R.id.address_manage_delete_btn:// 删除
                        final AddressDetailModel item = mAdapter.getItem(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddressManageActivity.this);
                        builder.setTitle(R.string.toast).setMessage("确定删除吗?");
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDialog();

                                // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回String[]类型
                                BaseJSONParser<Boolean> jsonParser = new SuccessParser();
                                // 建立请求对象模型
                                String requestUrl = getResources().getString(R.string.url_addressdelete); // addressdelete
                                // 提交的数据
                                HashMap<String, String> requestDataMap = new HashMap<String, String>();
                                requestDataMap.put("id", item.getId() + "");

                                RequestModel requestModel2 = new RequestModel();
                                // 设置请求的url
                                requestModel2.setRequestUrl(requestUrl);
                                // 设置提交的数据
                                requestModel2.setRequestDataMap(requestDataMap);
                                // 设置模型的解析器
                                requestModel2.setJsonParser(jsonParser);

                                // 向服务器端请求版本信息
                                // "http://10.0.3.2:8084/marsbaby/" + "addressdelete"
                                String reqDataUrl = getResources().getString(R.string.app_host).concat(requestUrl);
                                VolleyUtil volleyUtil = new VolleyUtil();
                                volleyUtil.setResponseCallback(new VolleyUtil.ResponseCallback() {
                                    @Override
                                    public void responseString(String responseText) {
                                        closeProgressDialog();
                                        Logger.d(TAG, responseText);
                                        Boolean serverResponseData = null;
                                        try {
                                            // 解析
                                            serverResponseData = (Boolean) requestModel.jsonParser.parseJSON(responseText);
                                            Logger.i(TAG, serverResponseDataList.size() + "");
                                        } catch (JSONException e) {
                                            Logger.e(TAG, e.getLocalizedMessage(), e);
                                        }
                                        if (serverResponseData != null && serverResponseData) {
                                            mAdapter.remove(mAdapter.getItem(position));        // 删除
                                            ToastUtil.showLongMsg(AddressManageActivity.this, getResources().getString(R.string.delete_success));
                                        }
                                    }

                                    @Override
                                    public void responseJSONObject(JSONObject jsonObject) {
                                    }

                                    @Override
                                    public void responseJsonArray(JSONArray jsonArray) {
                                    }
                                });       // 设置回调监听器
//                                volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
                                volleyUtil.postRequest(requestModel2,reqDataUrl);       // 向服务器异步请求版本信息
                            }
                        });
                        builder.setNegativeButton(R.string.no, null);
                        builder.show();
                        break;
                }
            }
        });
    }

    private void loadViewLayout() {
        setHeadRightText(R.string.address_manager_add);
        setHeadRightVisibility(View.VISIBLE);
        setTitle(R.string.address_manage);
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
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求数据
    }

    private void setListener() {
        addressItemlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final AddressDetailModel item = mAdapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddressManageActivity.this);
                builder.setTitle(R.string.toast).setMessage("确定设置默认地址吗?");

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();

                        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回String[]类型
                        BaseJSONParser<Boolean> jsonParser = new SuccessParser();
                        // 建立请求对象模型
                        String requestUrl = getResources().getString(R.string.url_addressdefault); // addressdefault
                        // 提交的数据
                        HashMap<String, String> requestDataMap = new HashMap<String, String>();
                        requestDataMap.put("id", item.getId() + "");

                        RequestModel requestModel3 = new RequestModel();
                        // 设置请求的url
                        requestModel3.setRequestUrl(requestUrl);
                        // 设置提交的数据
                        requestModel3.setRequestDataMap(requestDataMap);
                        // 设置模型的解析器
                        requestModel3.setJsonParser(jsonParser);

                        // 向服务器端请求版本信息
                        // "http://10.0.3.2:8084/marsbaby/" + "addressdefault"
                        String reqDataUrl = getResources().getString(R.string.app_host).concat(requestUrl);
                        VolleyUtil volleyUtil = new VolleyUtil();
                        volleyUtil.setResponseCallback(new VolleyUtil.ResponseCallback() {
                            @Override
                            public void responseString(String responseText) {
                                closeProgressDialog();
                                Logger.d(TAG, responseText);
                                Boolean serverResponseData = null;
                                try {
                                    // 解析
                                    serverResponseData = (Boolean) requestModel.jsonParser.parseJSON(responseText);
                                    Logger.i(TAG, serverResponseDataList.size() + "");
                                } catch (JSONException e) {
                                    Logger.e(TAG, e.getLocalizedMessage(), e);
                                }
                                if (serverResponseData != null && serverResponseData) {
                                    ToastUtil.showLongMsg(AddressManageActivity.this, getResources().getString(R.string.set_success));
                                }
                            }

                            @Override
                            public void responseJSONObject(JSONObject jsonObject) {
                            }

                            @Override
                            public void responseJsonArray(JSONArray jsonArray) {
                            }
                        });       // 设置回调监听器
//                        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
                        volleyUtil.postRequest(requestModel3, reqDataUrl);       // 向服务器异步请求版本信息
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.show();
                return true;
            }
        });
    }

    @Override
    protected void onHeadRightButton(View v) {
        startActivityForResult(new Intent(this, AddAddressActivity.class), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {
            // 更新地址列表
            ArrayList<AddressDetailModel> addressList = data.getParcelableArrayListExtra("addressList");
            mAdapter.clear();
            mAdapter.addAll(addressList);
        }
    }

    @Override
    public void finish() {
        setResult(200);
        super.finish();
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
            addressItemlv.setAdapter(mAdapter);
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {

    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {

    }
}
