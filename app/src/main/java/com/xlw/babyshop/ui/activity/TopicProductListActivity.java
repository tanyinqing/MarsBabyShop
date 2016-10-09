package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.model.TopicListModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.TopicListParser;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
import com.xlw.babyshop.ui.adapter.TopicLvAdapter;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class TopicProductListActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "TopicProductListActivity";

    private TextView tv_prodlist_ranksale;
    private TextView tv_prodlist_rankprice;
    private TextView tv_prodlist_rankgood;
    private TextView tv_prodlist_ranktime;
    private ListView lv_prodlist_listprod;
    private ImageView iv_prodlist_noresult;
    private TextView tv_prodlist_noresult;

//    private List<TopicListVo> topicList;
    private SharedPreferences sp;
    boolean rankUp;
    private String cId ;

    private RequestModel requestModel;
    private List<TopicListModel> serverResponseDataList;   // 从服务器返回的数据集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);
    }

    private void initView() {
//        if (isLoadBottomTab()) {
//            View currentView = getWindow().getDecorView();
//            loadBottomTab(currentView);    // 加载BottomTab,为BottomTab上的ImageView添加监听器
//            selectedBottomTab(DEFAULT_INDEX);   // 默认选中第一个选项
//        }
        loadViewLayout();   // 设置ActionBar view
        findViewById();     // 查找首页各个组件
        setListener();      // 为画廊和商品列表添加事件监听器
        processLogic();     // 向服务器端请求数据,并填充到Gallery和CategoryList的Adapter中
    }

    private void findViewById() {
        tv_prodlist_ranksale = (TextView) findViewById(R.id.textRankSale);
        tv_prodlist_rankprice = (TextView) findViewById(R.id.textRankPrice);
        tv_prodlist_rankgood = (TextView) findViewById(R.id.textRankGood);
        tv_prodlist_ranktime = (TextView) findViewById(R.id.textRankTime);
        lv_prodlist_listprod = (ListView) findViewById(R.id.productList);
        iv_prodlist_noresult = (ImageView) findViewById(R.id.listProduct);
        tv_prodlist_noresult = (TextView) findViewById(R.id.textNull);

        tv_prodlist_ranksale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderby = "sale_down";
                tv_prodlist_rankprice.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_ranksale.setBackgroundResource(R.drawable.segment_selected_1_bg);
                tv_prodlist_ranktime.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_rankgood.setBackgroundResource(R.drawable.segment_normal_2_bg);
                getRankSaleData(orderby);
            }
        });
        tv_prodlist_rankprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可以保持用户以前设置的排序方式，提高用户体验
                String orderby = "sale_down";
                if(rankUp){
                    orderby = "price_up";
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("topictrank", true);
                    editor.commit();
                }else{
                    orderby ="price_down";
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("topictrank", false);
                    editor.commit();
                }
                tv_prodlist_rankprice.setBackgroundResource(R.drawable.segment_selected_1_bg);
                tv_prodlist_ranksale.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_ranktime.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_rankgood.setBackgroundResource(R.drawable.segment_normal_2_bg);
                getRankSaleData(orderby);
            }
        });
        tv_prodlist_rankgood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderby = "comment_down";
                tv_prodlist_rankprice.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_ranksale.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_ranktime.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_rankgood.setBackgroundResource(R.drawable.segment_selected_1_bg);
                getRankSaleData(orderby);
            }
        });
        tv_prodlist_ranktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderby = "shelves_down";
                tv_prodlist_rankprice.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_ranksale.setBackgroundResource(R.drawable.segment_normal_2_bg);
                tv_prodlist_ranktime.setBackgroundResource(R.drawable.segment_selected_1_bg);
                tv_prodlist_rankgood.setBackgroundResource(R.drawable.segment_normal_2_bg);
                getRankSaleData(orderby);
            }
        });
//		bt_prodlist_sift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent siftIntent = new Intent(TopicProductListActivity.this,ProductFilterActivity.class);
//                siftIntent.putExtra("cId", cId);
//                startActivityForResult(siftIntent, 121);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getRankSaleData(String orderby) {
        requestDataFromServer(orderby);
    }

    private void loadViewLayout() {
        setTitle("主题列表");
        sp = getSharedPreferences("rank", MODE_PRIVATE);

        rankUp = sp.getBoolean("topicrank", false);
        if(rankUp == false){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("topictrank", false);
            editor.commit();
        }
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
                    requestDataFromServer(null);
                } else {
                    // 如果无法联上互联网
                    ToastUtil.showLongMsg(TopicProductListActivity.this, "无法连接互联网");
                }
            }
        });
        netCheckTask.execute();
    }

    private void requestDataFromServer(String orderby){
        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回HomeBannerModel类型
        BaseJSONParser<List<TopicListModel>> jsonParser = new TopicListParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.topic_plist);
        // 向服务器提交的分页数据
        HashMap<String, String> requestDataMap = new HashMap<String, String>();
        requestDataMap.put("page", "");
        requestDataMap.put("pageNum", "");
        requestDataMap.put("cId", getIntent().getStringExtra("cId"));
        if(null != orderby) {
            requestDataMap.put("orderby", orderby);
        }
        //requestDataMap.put("orderby", "sale_down");

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置提交的数据
        requestModel.setRequestDataMap(requestDataMap);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8080/MarsShop/" + "topic/plist"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
//        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求版本信息
        volleyUtil.postRequest(requestModel,reqDataUrl);       // 向服务器异步提交数据
    }

    private void setListener() {
        lv_prodlist_listprod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemID =Integer.parseInt(serverResponseDataList.get(position).getId());
                Logger.i(TAG, itemID + "");
                //ProductDetailActivity
                Intent detailIntent = new Intent(TopicProductListActivity.this,ProductDetailActivity.class);
                detailIntent.putExtra("id", itemID);
                startActivity(detailIntent);
            }

        });
    }

    @Override
    public void responseString(String responseText) {
        closeProgressDialog();
        Logger.d(TAG, responseText);
        try {
            // 解析
            serverResponseDataList = (List<TopicListModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG, serverResponseDataList.size() + "条记录");
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            //获得数据后进行listView数据的填充
            TopicLvAdapter adapter = new TopicLvAdapter(TopicProductListActivity.this,  serverResponseDataList);
            lv_prodlist_listprod.setAdapter(adapter);
        }else{
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
