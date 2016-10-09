package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.OrderListModel;
import com.xlw.babyshop.model.PageModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.OrderListParser;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
import com.xlw.babyshop.ui.adapter.OrderListLvAdapter;
import com.xlw.babyshop.utils.Constant;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderListActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "OrderListActivity";

    private ListView my_order_list;
    private TextView my_order_month;
    private TextView my_order_all;
    private TextView my_order_notsend;
    private int flag = 0;
    private View pageView;

    private List<OrderListModel> orderlist = new ArrayList<>();
    private List<OrderListModel> orderlistInMonth = new ArrayList<>();
    private List<OrderListModel> ordercanceled = new ArrayList<>();
    private List<OrderListModel> cancelablelist;
    private List<OrderListModel> uncancelablelist;
    private OrderListLvAdapter adapter;
    private PageModel pageVo;

    TextView previousPage ;
    TextView nextPage ;
    TextView textPage ;

    private RequestModel requestModel;
    private List<OrderListModel> serverResponseDataList;  // 服务器返回的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_activity);

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
        my_order_list = (ListView) this.findViewById(R.id.my_order_list);
        my_order_month = (TextView) this.findViewById(R.id.my_order_month);
        my_order_all = (TextView) this.findViewById(R.id.my_order_all);
        my_order_notsend = (TextView) this.findViewById(R.id.my_order_notsend);
    }

    private void loadViewLayout() {
        setHeadLeftVisibility(View.VISIBLE);
        setTitle(R.string.my_ordr_title);
        setHeadBackgroundResource(R.drawable.head_bg);
        selectedBottomTab(Constant.HOME);

        int totoalOrderCount = getIntent().getIntExtra("totoalOrderCount",0);
        Log.i(TAG, totoalOrderCount + "");
        pageVo = new PageModel(0, totoalOrderCount, 1);
    }

    private void processLogic() {
        // 向服务器端请求数据
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
                    ToastUtil.showLongMsg(OrderListActivity.this, "无法连接互联网");
                }
            }
        });
        netCheckTask.execute();
    }

    private void requestDataFromServer(){
        showProgressDialog();

        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回List<ProductListModel>类型
        BaseJSONParser<List<OrderListModel>> jsonParser = new OrderListParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.orderlist);
        // 请求的分页数据
        HashMap<String, String> prodMap = new HashMap<String, String>();
        // listview内容的分页处理
        prodMap.put("type", flag + 1 + "");
        prodMap.put("page", pageVo.wantedPageNum+"");
        prodMap.put("pageNum",pageVo.pageLenth+"");

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置请求参数
        requestModel.setRequestDataMap(prodMap);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marsbaby/" + "orderlist"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
    }

    private void setListener() {
        my_order_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                my_order_month.setBackgroundResource(R.drawable.segment_selected_1_bg);
                my_order_all.setBackgroundResource(R.drawable.segment_normal_2_bg);
                my_order_notsend.setBackgroundResource(R.drawable.segment_normal_3_bg);
                requestDataFromServer();
            }
        });
        my_order_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                my_order_month.setBackgroundResource(R.drawable.segment_normal_1_bg);
                my_order_all.setBackgroundResource(R.drawable.segment_selected_2_bg);
                my_order_notsend.setBackgroundResource(R.drawable.segment_normal_3_bg);
                requestDataFromServer();
            }
        });
        my_order_notsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                my_order_month.setBackgroundResource(R.drawable.segment_normal_1_bg);
                my_order_all.setBackgroundResource(R.drawable.segment_normal_2_bg);
                my_order_notsend.setBackgroundResource(R.drawable.segment_selected_3_bg);
                requestDataFromServer();
            }
        });
        my_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(pageView!=null && view.equals(pageView)){
                    view.setClickable(false);
                    return;
                }
                TextView text = (TextView) view.findViewById(R.id.orderId_text);
                String orderId = text.getText().toString().trim();
                Log.i(TAG, "你点击了订单号"+orderId+"的订单，跳转订单详情页面");
                Intent orderDetailIntent = new Intent(OrderListActivity.this,OrderDetailActivity.class);
                orderDetailIntent.putExtra("orderId", orderId);
                startActivity(orderDetailIntent);
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
            serverResponseDataList = (List<OrderListModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataList.size() + "");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            divide(serverResponseDataList);
            if(pageView!=null){
                my_order_list.removeFooterView(pageView);
            }
            setAdapterForDivide();
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {
    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {
    }

    private void setAdapterForDivide() {
        switch (flag) {
            case 0:
                cancelableInit(orderlistInMonth);
                adapter = new OrderListLvAdapter(
                        OrderListActivity.this, orderlistInMonth,cancelablelist,cancelablelist);

                //分页处理
                if(orderlistInMonth.size()>=pageVo.pageLenth){
                    showPageBar();
                }
                my_order_list.setAdapter(adapter);
                break;
            case 1:
                cancelableInit(orderlist);
                adapter = new OrderListLvAdapter(
                        OrderListActivity.this, orderlist,cancelablelist,uncancelablelist);
                //分页处理
                if(orderlist.size()>=pageVo.pageLenth){
                    showPageBar();
                }
                my_order_list.setAdapter(adapter);
                break;
            case 2:
                adapter = new OrderListLvAdapter(
                        OrderListActivity.this, ordercanceled,cancelablelist,uncancelablelist);
                //分页处理
                if(ordercanceled.size()==pageVo.pageLenth){
                    showPageBar();
                }
                my_order_list.setAdapter(adapter);
                break;
        }
    }

    private void cancelableInit(List<OrderListModel> list) {
        cancelablelist = new ArrayList<OrderListModel>();
        uncancelablelist = new ArrayList<OrderListModel>();
        //对订单是否可以取消做不同的处理
        for (OrderListModel item : list) {
            if(item.getFlag()==1){
                cancelablelist.add(item);
                Log.i(TAG, item.getFlag()+"-可取消的上-"+item.getStatus());
            }else{
                uncancelablelist.add(item);
                Log.i(TAG, item.getFlag()+"-不可取消的上-"+item.getStatus());
            }
        }
        Log.i(TAG, "cancelablelist可以取消的数目为:" + cancelablelist.size());
        Log.i(TAG, "uncancelablelist不可以取消的数目为:" + uncancelablelist.size());
        //ADD BY LQT 20120421 -----END--------------
    }

    private void showPageBar() {
        pageView = View.inflate(OrderListActivity.this, R.layout.page, null);
        previousPage = (TextView) pageView.findViewById(R.id.my_page_previous);
        nextPage = (TextView) pageView.findViewById(R.id.my_page_next);
        textPage = (TextView) pageView.findViewById(R.id.my_page_text);
        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "显示前一页------1");
                Log.i(TAG,pageVo.wantedPageNum+"");
                if(pageVo.wantedPageNum<=1){
                    previousPage.setClickable(false);
                    return;
                }

                requestDataFromServer();    // 再次向服务器端请求数据

                pageVo.wantedPageNum -= 1;
                Log.i(TAG, "显示前一页------2");
                Log.i(TAG,pageVo.wantedPageNum+"");
                textPage.setText(pageVo.wantedPageNum+"/"+pageVo.totalPageNum);
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "显示下一页-------1");
                Log.i(TAG,pageVo.wantedPageNum+"");
                if(pageVo.wantedPageNum>=pageVo.totalPageNum){
                    nextPage.setClickable(false);
                    return;
                }

                requestDataFromServer();    // 再次向服务器端请求数据

                pageVo.wantedPageNum += 1;
                Log.i(TAG, "显示下一页-------2");
                Log.i(TAG,pageVo.wantedPageNum+"");
                textPage.setText(pageVo.wantedPageNum+"/"+pageVo.totalPageNum);
            }
        });
        textPage.setText(pageVo.wantedPageNum + "/" + pageVo.totalPageNum);

        my_order_list.addFooterView(pageView);
    }

    protected void divide(List<OrderListModel> paramObject) {
        this.orderlist = paramObject;
        for (OrderListModel item : orderlist) {
            // 将已取消的条目筛选出来
            if ("已取消".equals(item.getStatus())) {
                ordercanceled.add(item);
            }
            // 将订单中近一个月内的订单放到orderlistInMonth中
            try {
                Calendar now = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy/MM/dd HH:mm:ss");
                Date date = format.parse(item.getTime());
                Calendar ordertime = Calendar.getInstance();
                ordertime.setTime(date);
                ordertime.add(Calendar.MONTH, 1);
                if (ordertime.compareTo(now) >= 0) {
                    System.out.println("是一个月内的");
                    orderlistInMonth.add(item);
                } else {
                    System.out.println("不是一个月内的");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 删除订单操作
    public void processCancelOrder(String orderid) {
        RequestModel requestModel = new RequestModel();
        // ... 待实现
    }
}
