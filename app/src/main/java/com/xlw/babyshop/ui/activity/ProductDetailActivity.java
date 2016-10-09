package com.xlw.babyshop.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xlw.babyshop.application.XlwApplication;
import com.xlw.babyshop.model.ProdcutHistory;
import com.xlw.babyshop.model.ProductDetailModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.ProductDetailParser;
import com.xlw.babyshop.parser.SuccessParser;
import com.xlw.babyshop.service.IShopManager;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
import com.xlw.babyshop.ui.adapter.ProductGalleryAdapter;
import com.xlw.babyshop.utils.Constant;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductDetailActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "ProductDetailActivity";
    private ProductDetailModel productDetail;
    private TextView titleBack;
    private Gallery productGallery;
    private ImageView imgPoint;
    private TextView textProductNameValue;
    private TextView textProductIdValue;
    private TextView textOriginalPriceValue;
    private TextView textProdGradeValue;
    private TextView textPriceValue;
    private EditText prodNumValue;
    private TextView textPutIntoShopcar;
    private ImageView imgServiceImg;
    private TextView textProdIsStock;
    private TextView textProductCommentNum;
    private TextView orderTelTv;
//    private ProductDetailModel currentProduct;
    private ProductDetailModel serverResponseData;

    private RequestModel requestModel;  // 请求数据模型对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_activity);

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
        productGallery = (Gallery) findViewById(R.id.productGallery);
        textProductIdValue = (TextView) findViewById(R.id.textProductIdValue);
        textProductNameValue = (TextView) findViewById(R.id.textProductNameValue);
        textOriginalPriceValue = (TextView) findViewById(R.id.textOriginalPriceValue);
        textPriceValue = (TextView) findViewById(R.id.textPriceValue);
        textProductCommentNum = (TextView) findViewById(R.id.textProductCommentNum);
        prodNumValue = (EditText) findViewById(R.id.prodNumValue);
        textPutIntoShopcar = (TextView) findViewById(R.id.textPutIntoShopcar);
        orderTelTv = (TextView) findViewById(R.id.orderTelTv);

        // 加入收藏夹
        findViewById(R.id.textProdToCollect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCollect();
            }
        });
    }

    private void loadViewLayout() {
        setTitle("商品详细");
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
                    ToastUtil.showLongMsg(ProductDetailActivity.this, "无法连接互联网");
                }
            }
        });
        netCheckTask.execute();
    }

    private void requestDataFromServer(){
        // 如果没有传过来商品id
        int id  = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Logger.d(TAG, "id is null");
            finish();
            return ;
        }

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回List<ProductListModel>类型
        BaseJSONParser<ProductDetailModel> jsonParser = new ProductDetailParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.product);
        // 请求的分页数据
        HashMap<String, String> requestDataMap = new HashMap<String, String>();
        requestDataMap.put("id", id + "");

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置请求参数
        requestModel.setRequestDataMap(requestDataMap);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "product"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    private void setListener() {
        productGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        textPutIntoShopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> requestDataMa = new HashMap<String, String>();
                requestDataMa.put("id", serverResponseData.getId() + "");
                requestDataMa.put("count", prodNumValue.getText().toString());
                Logger.i("id:", serverResponseData.getId() + "");
                Logger.i("count:",prodNumValue.getText().toString());

                final RequestModel requestModel2 = new RequestModel();
                // 设置请求的url
                requestModel2.setRequestUrl(getResources().getString(R.string.url_cartdatasession));
                // 设置请求参数
                requestModel2.setRequestDataMap(requestDataMa);
                // 设置模型的解析器
                requestModel2.setJsonParser(new SuccessParser());

                // 向服务器端请求版本信息
                // "http://10.0.3.2:8084/marsbaby/" + "cartdatasession"
                String reqDataUrl2 = getResources().getString(R.string.app_host).concat(getResources().getString(R.string.url_cartdatasession));
                VolleyUtil volleyUtil = new VolleyUtil();
                // 设置回调监听器
                volleyUtil.setResponseCallback(new VolleyUtil.ResponseCallback() {
                    @Override
                    public void responseString(String responseText) {
                        closeProgressDialog();
                        Logger.d(TAG, responseText);
                        Boolean responseData = null;
                        try {
                            // 解析
                            responseData = (Boolean) requestModel2.jsonParser.parseJSON(responseText);
                            Logger.i(TAG, "返回的数据:" + responseData);
                        } catch (JSONException e) {
                            Logger.e(TAG, e.getLocalizedMessage(), e);
                        }
                        if (responseData != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                            builder.setTitle("添加进购物车成功");
                            builder.setPositiveButton("继续购物", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.setNegativeButton("进入购物车", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ProductDetailActivity.this, ShoppingCarActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder.create().show();
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "加入购物车失败", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void responseJSONObject(JSONObject jsonObject) {
                    }

                    @Override
                    public void responseJsonArray(JSONArray jsonArray) {
                    }
                });
//                volleyUtil.requestString(reqDataUrl2);       // 向服务器异步请求版本信息
                volleyUtil.postRequest(requestModel2, reqDataUrl2);       // 向服务器异步请求版本信息
            }
        });

        orderTelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:01088499999"));
                startActivity(intent);
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
            serverResponseData = (ProductDetailModel)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, "返回的商品名称:" + serverResponseData.getName());
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseData != null) {
            // 将数据加入到购物历史记录中
            IShopManager shopManager = XlwApplication.getInstance().getShopManager();
            shopManager.addProductToHistory(new ProdcutHistory(serverResponseData.getId(),
                    serverResponseData.getName(),
                    serverResponseData.getPic().get(0),
                    serverResponseData.getMarketprice(),
                    serverResponseData.getPrice(),
                    serverResponseData.getComment_count(),
                    System.currentTimeMillis()
                )
            );

            ProductGalleryAdapter adapter = new ProductGalleryAdapter(ProductDetailActivity.this, serverResponseData);
            productGallery.setAdapter(adapter);

            textProductNameValue.setText(serverResponseData.getName());
            textProductIdValue.setText(serverResponseData.getId() + "");
            textOriginalPriceValue.setText(serverResponseData.getMarketprice() + "");
            textPriceValue.setText(serverResponseData.getPrice() + "");
            textProductCommentNum.setText(serverResponseData.getComment_count() + "");
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {
    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {
    }

    private void addToCollect(){
        if (serverResponseData == null) {
            return;
        }
        HashMap<String, String> requestDataMp = new HashMap<String, String>();
        requestDataMp.put("id", serverResponseData.getId() + "");

        final RequestModel requestModel3 = new RequestModel();
        // 设置请求的url
        requestModel3.setRequestUrl(getResources().getString(R.string.url_productcollect));
        // 设置请求参数
        requestModel3.setRequestDataMap(requestDataMp);
        // 设置模型的解析器
        requestModel3.setJsonParser(new SuccessParser());

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "product/collect"
        String reqDataUrl3 = getResources().getString(R.string.app_host).concat(getResources().getString(R.string.url_productcollect));
        VolleyUtil volleyUtil = new VolleyUtil();
        // 设置回调监听器
        volleyUtil.setResponseCallback(new VolleyUtil.ResponseCallback() {
            @Override
            public void responseString(String responseText) {
                closeProgressDialog();
                Logger.d(TAG, responseText);
                Boolean responseData = null;
                try {
                    // 解析
                    responseData = (Boolean) requestModel3.jsonParser.parseJSON(responseText);
                    Logger.i(TAG, "返回的数据:" + responseData);
                } catch (JSONException e) {
                    Logger.e(TAG, e.getLocalizedMessage(), e);
                }
                if (responseData != null && responseData) {
                    ToastUtil.showLongMsg(ProductDetailActivity.this, "加入收藏夹成功");
                } else {
                    ToastUtil.showLongMsg(ProductDetailActivity.this, "加入收藏夹失败");
                }
            }

            @Override
            public void responseJSONObject(JSONObject jsonObject) {
            }

            @Override
            public void responseJsonArray(JSONArray jsonArray) {
            }
        });
        volleyUtil.requestString(reqDataUrl3);       // 向服务器异步请求版本信息
    }
}
