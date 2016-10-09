package com.xlw.babyshop.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.AddressModel;
import com.xlw.babyshop.model.CartProductModel;
import com.xlw.babyshop.model.CheckoutAddupModel;
import com.xlw.babyshop.model.DeliveryModel;
import com.xlw.babyshop.model.InvoiceInfoModel;
import com.xlw.babyshop.model.OrderInfoModel;
import com.xlw.babyshop.model.PaymentModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.OrderDetailParser;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
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

public class OrderDetailActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "OrderDetailActivity";

    private String orderId;

    private TextView textAdress1;
    private TextView textAdress2;
    private TextView textAdress3;
    private TextView textAdress4;
    private TextView textDetail1;
    private TextView textDetail2;
    private TextView textDetail3;
    private TextView textDetail4;
    private TextView textDetail5;
    private TextView textDetail6;
    private TextView textDetail7;
    private TextView textDetail8;
    private TextView textDetail9;

    private ListView listdetail;//商品清单
    private TextView textPrice2;//商品金额总计：
    private TextView textPrice3;//优惠金额：
    private TextView textPrice4;//运费金额：
    private TextView textPrice5;//已支付金额：
    private TextView textPrice6;//获得积分：
    private TextView textPrice7;//应收款金额：

    private RequestModel requestModel;
    private List<CartProductModel> productlistInfo;
    private Map<String,Object> serverResponseDataMap;  // 服务器端返回的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_detail_activity);

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
// 收件人信息
        textAdress1 = (TextView) this.findViewById(R.id.textAdress1);//编号
        textAdress2 = (TextView) this.findViewById(R.id.textAdress2);//收件人
        textAdress3 = (TextView) this.findViewById(R.id.textAdress3);//收件人地址
        textAdress4 = (TextView) this.findViewById(R.id.textAdress4);//详细地址
        //订单详情
        textDetail1 = (TextView) this.findViewById(R.id.textDetail1);//订单状态
        textDetail2 = (TextView) this.findViewById(R.id.textDetail2);//送货方式
        textDetail3 = (TextView) this.findViewById(R.id.textDetail3);//支付方式
        textDetail4 = (TextView) this.findViewById(R.id.textDetail4);//订单生成时间
        textDetail5 = (TextView) this.findViewById(R.id.textDetail5);//发货时间
        textDetail6 = (TextView) this.findViewById(R.id.textDetail6);//是否开发票
        textDetail7 = (TextView) this.findViewById(R.id.textDetail7);//发票抬头
        textDetail8 = (TextView) this.findViewById(R.id.textDetail8);//送货要求
        textDetail9 = (TextView) this.findViewById(R.id.textDetail9);//备注
        //商品清单
        listdetail = (ListView) this.findViewById(R.id.listdetail);
        textPrice2 = (TextView) this.findViewById(R.id.textPrice2);
        textPrice3 = (TextView) this.findViewById(R.id.textPrice3);
        textPrice4 = (TextView) this.findViewById(R.id.textPrice4);
        textPrice5 = (TextView) this.findViewById(R.id.textPrice5);
        textPrice6 = (TextView) this.findViewById(R.id.textPrice6);
        textPrice7 = (TextView) this.findViewById(R.id.textPrice7);
    }

    private void loadViewLayout() {
        setHeadLeftVisibility(View.VISIBLE);
        setHeadBackgroundResource(R.drawable.head_bg);
        selectedBottomTab(Constant.HOME);
        orderId = getIntent().getStringExtra("orderId");
        Logger.d(TAG, orderId);
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
                closeProgressDialog();
                if (aBoolean) {
                    // 如果可以联上互联网,就请求数据
                    requestDataFromServer();
                } else {
                    // 如果无法联上互联网
                    ToastUtil.showLongMsg(OrderDetailActivity.this, "无法连接互联网");
                }
            }
        });
        netCheckTask.execute();
    }

    private void requestDataFromServer(){
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回List<ProductListModel>类型
        BaseJSONParser<Map<String,Object>> jsonParser = new OrderDetailParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.orderdetail);
        // 设置请求参数为：订单号码
        HashMap<String, String> prodMap = new HashMap<String, String>();
        prodMap.put("orderId", orderId);

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置请求参数
        requestModel.setRequestDataMap(prodMap);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "orderdetail"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求数据
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
            serverResponseDataMap = (Map<String,Object>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataMap.size() + "");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataMap != null) {
            AddressModel address_info = (AddressModel) serverResponseDataMap.get("addressInfo");
            OrderInfoModel orderInfo = (OrderInfoModel) serverResponseDataMap.get("orderInfo");
            PaymentModel paymentInfo = (PaymentModel) serverResponseDataMap.get("paymentInfo");
            DeliveryModel deliveryInfo = (DeliveryModel) serverResponseDataMap.get("deliveryInfo");
            InvoiceInfoModel invoiceInfo = (InvoiceInfoModel) serverResponseDataMap.get("invoiceInfo");
            productlistInfo = (List<CartProductModel>) serverResponseDataMap.get("productlistInfo");
            List<String> checkout = (List<String>)serverResponseDataMap.get("checkout");
            CheckoutAddupModel checkoutAdd = (CheckoutAddupModel) serverResponseDataMap.get("checkoutAdd");

            textAdress1.setText(address_info.getId()+"");
            textAdress2.setText(address_info.getName()+"");
            textAdress3.setText(address_info.getAddress_area()+"");
            textAdress4.setText(address_info.getAddress_detail()+"");

            textDetail1.setText(orderInfo.getStatus()+"");
            textDetail2.setText(deliveryInfo.getType()+"");
            textDetail3.setText(paymentInfo.getType()+"");
            textDetail4.setText(orderInfo.getTime()+"");
            textDetail5.setText(deliveryInfo.getType()+"");
            textDetail7.setText(invoiceInfo.getTitle()+"");

            textPrice2.setText(checkoutAdd.getTotal_price()+"");
            textPrice3.setText(checkoutAdd.getProm_cut()+"");
            textPrice4.setText(checkoutAdd.getFreight()+"");
            textPrice6.setText(checkoutAdd.getTotal_point()+"");
            textPrice7.setText(checkoutAdd.getTotal_price()-checkoutAdd.getProm_cut()+"");
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {
    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {
    }
}
