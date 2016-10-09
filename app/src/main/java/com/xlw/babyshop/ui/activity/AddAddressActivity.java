package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.xlw.babyshop.dao.AreaDao;
import com.xlw.babyshop.model.AddressDetailModel;
import com.xlw.babyshop.model.AreaModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.AddressManageParser;
import com.xlw.babyshop.parser.BaseJSONParser;
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

/**
 * 地址添加、以及修改<br>
 * 返回结果为:添加成功，结果包含数据地址列表的最新信息 data.getPracelableArrayList("addressList");
 */
public class AddAddressActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "AddAddressActivity";

    private List<AreaModel> allProvince;
    private ArrayAdapter<AreaModel> mProvinceAdapter;

    private View cityLy;
    private View areaLy;
    private Spinner mProvinceSpinner;
    private Spinner mCitySpinner;
    private Spinner mAreaSpinner;

    private AreaDao areaDao;

    private ArrayAdapter<AreaModel> mCityeAdapter;
    private ArrayAdapter<AreaModel> mAreaAdapter;

    private TextView mNameEdit;
    private TextView mMobileEdit;
    private TextView mTelEdit;
    private TextView mDetailEdit;
    private TextView mZipcodeEdit;

    /** 是否是修改 */
    private boolean isEdit;
    private boolean isCityFirst = true;
    private boolean isAreaFirst = true;

    private AddressDetailModel address;
    RequestModel requestModel;
    ArrayList<AddressDetailModel> serverResponseDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_activity);

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
        mProvinceSpinner = (Spinner) findViewById(R.id.address_add_spinner_province);// 省
        mCitySpinner = (Spinner) findViewById(R.id.address_add_spinner_city);// 市
        mAreaSpinner = (Spinner) findViewById(R.id.address_add_spinner_area);// 区
        cityLy = findViewById(R.id.add_address_city_ly);
        areaLy = findViewById(R.id.add_address_area_ly);

        mNameEdit = (TextView) findViewById(R.id.add_address_name_edit);// 名称
        mMobileEdit = (TextView) findViewById(R.id.add_address_mobile_edit);// 手机
        mTelEdit = (TextView) findViewById(R.id.add_address_tel_edit);// 固定电话
        mDetailEdit = (TextView) findViewById(R.id.add_address_detail_edit);// 详细地址
        mZipcodeEdit = (TextView) findViewById(R.id.add_address_zipcode_edit);// 邮编

        findViewById(R.id.save_address_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 确定
                if (TextUtils.isEmpty(mNameEdit.getText())) {
                    ToastUtil.showLongMsg(AddAddressActivity.this, "请输入名字");
                    return;
                }
                if (TextUtils.isEmpty(mMobileEdit.getText())) {
                    ToastUtil.showLongMsg(AddAddressActivity.this, "请输入电话号码");
                    return;
                }

                if (TextUtils.isEmpty(mDetailEdit.getText())) {
                    ToastUtil.showLongMsg(AddAddressActivity.this, "请输入详细地址");
                    return;
                }
                if (TextUtils.isEmpty(mZipcodeEdit.getText())) {
                    ToastUtil.showLongMsg(AddAddressActivity.this, "请输入邮编");
                    return;
                }

                postDataToServer();
            }
        });
        findViewById(R.id.cancel_address_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 取消
                finish();
            }
        });
    }

    private void postDataToServer(){
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回String[]类型
        BaseJSONParser<List<AddressDetailModel>> jsonParser = new AddressManageParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.url_addresssave);

        // 向服务器提交的数据
        HashMap<String, String> requestDataMap = new HashMap<String, String>();
        requestDataMap.put("name", mNameEdit.getText().toString());
        requestDataMap.put("phonenumber", mMobileEdit.getText().toString());
        requestDataMap.put("fixedtel", mTelEdit.getText().toString());
        StringBuilder builder = new StringBuilder();
        AreaModel area = (AreaModel) mProvinceSpinner.getSelectedItem();
        builder.append(area.getId());
        builder.append(",");
        area = (AreaModel) mCitySpinner.getSelectedItem();
        builder.append(area.getId());
        builder.append(",");
        area = (AreaModel) mAreaSpinner.getSelectedItem();
        builder.append(area.getId());
        requestDataMap.put("areaid", builder.toString());
        requestDataMap.put("areadetail", mDetailEdit.getText().toString());
        requestDataMap.put("zipcode", mZipcodeEdit.getText().toString());
        if (isEdit) {
            requestDataMap.put("id", address.getId() + "");
        }

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置提交的数据
        requestModel.setRequestDataMap(requestDataMap);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "addresslist"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
//        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求数据
        volleyUtil.postRequest(requestModel, reqDataUrl);       // 向服务器异步提交数据

    }

    private void loadViewLayout() {

    }

    private void processLogic() {
        address = getIntent().getParcelableExtra("address");
		if (address != null) {
			isEdit = true;
			setTitle("地址修改");
			Logger.d("TAG", address.toString());
		} else {
			setTitle("地址添加");
		}

		areaDao = new AreaDao(this);
		allProvince = areaDao.getAllProvince();

		int provinceSelectId = -1;
		int citySelectId = -1;
		int areaSelectId = -1;
		if (isEdit) { // 修改回显数据
			provinceSelectId = address.getProvinceid();
			citySelectId = address.getCityid();
			areaSelectId = address.getAreaid();
			mNameEdit.setText(address.getName());
			mMobileEdit.setText(address.getPhonenumber());
			mTelEdit.setText(address.getFixedtel());
			mDetailEdit.setText(address.getAreadetail());
			mZipcodeEdit.setText(address.getZipcode());
		}

		updateProvince(allProvince);
		int areaId;
		AreaModel area;
		if (isEdit) {
			area = selectedSpinner(allProvince, provinceSelectId, mProvinceSpinner);
			areaId = address.getProvinceid();
		} else {
			area = allProvince.get(0);
			areaId = area.getId();
		}
		List<AreaModel> citys = areaDao.findBySuperCode(areaId);
		updateCity(citys);
		area.setSub_area(citys);

		if (isEdit) {
			area = selectedSpinner(citys, citySelectId, mCitySpinner);
			areaId = area.getId();
		} else {
			area = citys.get(0);
		}

		List<AreaModel> areas = areaDao.findBySuperCode(area.getId());
		updateArea(areas);
		if (isEdit)
			selectedSpinner(areas, areaSelectId, mAreaSpinner);
		if (areas != null && areas.size() > 0) {
			area.setSub_area(areas);
		}

    }

    private void setListener() {
        mProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AreaModel item;
                List<AreaModel> sub_area;
                if (isEdit && isCityFirst) {
                    isCityFirst = false;
                    return;
                }
                item = (AreaModel) mProvinceAdapter.getItem(position);
                sub_area = item.getSub_area();
                if (sub_area == null) {
                    sub_area = areaDao.findBySuperCode(item.getId());
                }
                updateCity(sub_area);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AreaModel item;
                List<AreaModel> sub_area;
                if (isEdit && isAreaFirst) {
                    isAreaFirst = false;
                    return;
                }

                item = (AreaModel) mCityeAdapter.getItem(position);
                sub_area = item.getSub_area();
                if (sub_area == null) {
                    sub_area = areaDao.findBySuperCode(item.getId());
                }
                updateArea(sub_area);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseDataList = (ArrayList<AddressDetailModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataList.size() + "");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            Intent data = new Intent();

            ToastUtil.showLongMsg(AddAddressActivity.this, isEdit ? getResources().getString(R.string.edit_success) : getResources().getString(R.string.add_succuess));
            data.putParcelableArrayListExtra("addressList", serverResponseDataList);
            setResult(200, data);
            finish();
        }

    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {

    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {

    }

    public void updateProvince(List<AreaModel> areas) {
        mProvinceAdapter = new ArrayAdapter<AreaModel>(this, android.R.layout.simple_spinner_item, areas);
        mProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(mProvinceAdapter);

    }

    public void updateCity(List<AreaModel> areas) {
        if (areas != null && areas.size() > 0) {
            mCityeAdapter = new ArrayAdapter<AreaModel>(this, android.R.layout.simple_spinner_item, areas);
            mCityeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCitySpinner.setAdapter(mCityeAdapter);
            cityLy.setVisibility(View.VISIBLE);
        } else
            cityLy.setVisibility(View.GONE);
    }

    public void updateArea(List<AreaModel> areas) {
        if (areas != null && areas.size() > 0) {
            mAreaAdapter = new ArrayAdapter<AreaModel>(this, android.R.layout.simple_spinner_item, areas);
            mAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mAreaSpinner.setAdapter(mAreaAdapter);
            areaLy.setVisibility(View.VISIBLE);
        } else
            areaLy.setVisibility(View.GONE);
    }

    private AreaModel selectedSpinner(List<AreaModel> areas, int select, Spinner spinner) {

        if (select != -1) {
            int i = 0;
            for (AreaModel area : areas) {
                if (area.getId() == select) {
                    spinner.setSelection(i);
                    Logger.d(TAG, "select " + select);
                    return area;
                }
                i++;
            }
        }
        return null;
    }
}
