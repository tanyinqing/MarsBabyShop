package com.xlw.babyshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.HomeBannerModel;
import com.xlw.babyshop.model.HomeCategoryModel;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.HomeBannerParser;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
import com.xlw.babyshop.ui.adapter.HomeBannerAdapter;
import com.xlw.babyshop.ui.adapter.HomeCategoryAdapter;
import com.xlw.babyshop.utils.Constant;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 *
 * 搜索<br>
 * 点击搜索按钮激活SearchActivity.数据存在 "key_words" 通过Intent.getStringExtra("key_words") 获取
 *
 */
public class HomeActivity extends BaseActivity implements VolleyUtil.ResponseCallback{

    private static final String TAG = "HomeActivity";

    private final static Integer DEFAULT_INDEX = 1;

    private TextView searchEdit;        // 搜索框

    private Gallery mGallery;           // 图片轮播画廊
    private List<ImageView> mSlideViews;// 被轮播的图片
    private boolean isPlay;             // 标志变量,判断当前是否正在播放轮播图片

    private ListView mCategoryListView; // 显示商品类别信息

    private RequestModel requestModel;  // 客户端请求数据模型
    private List<HomeBannerModel> serverResponseDataList;   // 从服务器返回的数据集合

    private Handler handler = new Handler();
    // 图片轮播线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if  (!isPlay)
                return ;
            mGallery.setSelection((mGallery.getSelectedItemPosition() + 1) % 5);
            handler.postDelayed(this, 4000);
            Logger.d(TAG, "下一张");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);         // 会先调用超类的onCreate()方法
        setContentView(R.layout.activity_home);

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
            selectedBottomTab(DEFAULT_INDEX);   // 默认选中第一个选项
        }
        loadViewLayout();   //
        findViewById();     // 查找首页各个组件
        setListener();      // 为画廊和商品列表添加事件监听器
        processLogic();     // 向服务器端请求数据,并填充到Gallery和CategoryList的Adapter中
    }

    private void loadViewLayout() {
        // 设置BottomTab中被选中按钮的背景为HOME
        selectedBottomTab(Constant.HOME);
    }

    private void findViewById() {
        // 商品显示的ListView
        mCategoryListView = (ListView) findViewById(R.id.custonInfoListView);

        // 图片轮播的画廊
        mGallery = (Gallery) findViewById(R.id.gallery);

        // 图片轮播的控制小圆点
        mSlideViews = new ArrayList<ImageView>();
        mSlideViews.add((ImageView) findViewById(R.id.imgPoint0));
        mSlideViews.add((ImageView) findViewById(R.id.imgPoint1));
        mSlideViews.add((ImageView) findViewById(R.id.imgPoint2));
        mSlideViews.add((ImageView) findViewById(R.id.imgPoint3));
        mSlideViews.add((ImageView) findViewById(R.id.imgPoint4));

        // 顶部的搜索框
        searchEdit = (TextView) findViewById(R.id.editSearchInfo);
        // 为搜索按钮添加单击事件监听器
        findViewById(R.id.home_searchok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    // 单击搜索确定按钮
    private void search() {
        String words = searchEdit.getText().toString(); // 获得搜索关键字
        if (TextUtils.isEmpty(words)) {
            ToastUtil.showLongMsg(this, "请输入关键词");
            return ;
        }
        Intent Intent = new Intent(this, SearchProductListActivity.class);
        Intent.putExtra("keyWord", words);
        startActivity(Intent);
    }

    private void processLogic() {
        // 准备商品信息
        List<HomeCategoryModel> categroy = new ArrayList<>();
        categroy.add(new HomeCategoryModel(R.drawable.home_classify_01, "限时抢购"));
        categroy.add(new HomeCategoryModel(R.drawable.home_classify_02, "促销快报"));
        categroy.add(new HomeCategoryModel(R.drawable.home_classify_03, "新品上架"));
        categroy.add(new HomeCategoryModel(R.drawable.home_classify_04, "热卖单品"));
        categroy.add(new HomeCategoryModel(R.drawable.home_classify_05, "推荐品牌"));

        // 将商品分类信息添加到适配器中,并将适配器设置给ListView
        mCategoryListView.setAdapter(new HomeCategoryAdapter(this, categroy));

        getDataFromServer();        // 向服务器端请求数据-首页轮播的图片
    }

    private void getDataFromServer(){
        showProgressDialog();   // 显示一个进度条

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
                    ToastUtil.showLongMsg(HomeActivity.this, "无法连接互联网");
                }
            }
        });
        netCheckTask.execute();
    }

    private void requestDataFromServer(){
        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据// 应返回HomeBannerModel类型
        BaseJSONParser<List<HomeBannerModel>> jsonParser = new HomeBannerParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.url_home);
        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求图片轮播数据,并设置到Galley的适配器中
        // "http://10.0.3.2:8084/redbaby/" + "home"
        String reqDataUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);
        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(reqDataUrl);       // 向服务器异步请求轮播的图片路径信息
    }

    private void setListener() {
        mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doGalleyItemSelected(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doCategoryItemClick(parent, view, position, id);
            }
        });
    }

    /**
     * 首页栏图片轮换
     */
    private void doGalleyItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int size = mSlideViews.size();
        for (int i = 0; i < size; i++) {
            int j = position % size;
            ImageView imageView = mSlideViews.get(i);
            if (j == i)
                imageView.setBackgroundResource(R.drawable.slide_adv_selected);
            else
                imageView.setBackgroundResource(R.drawable.slide_adv_normal);
        }
    }

    /**
     * 首页栏分类点击
     */
    private void doCategoryItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0://限时抢购
                startActivity(new Intent(this,LimitbuyActivity.class));
                break;
            case 1://促销快报
                startActivity(new Intent(this, BulletinActivity.class));
                break;
            case 2://新品上架
                startActivity(new Intent(this,NewproductActivity.class));
                break;
            case 3://热卖单品
                startActivity(new Intent(this,HotproductActivity.class));
                break;
            case 4://推荐品牌
                startActivity(new Intent(this,BrandActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPlay = true;
        runnable.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPlay = false;
    }

    // 当异步请求信息从服务器端返回时,回调此方法
    @Override
    public void responseString(String responseText) {
        closeProgressDialog();  // 关掉进度条
        Logger.d(TAG, responseText);
        try {
            if (invilidateLogin(responseText)) {
//                return Status.Login;
                ToastUtil.showShortMsg(this,"请先登录");
                return;
            }
            // 解析
            serverResponseDataList = (List<HomeBannerModel>)requestModel.jsonParser.parseJSON(responseText);
            Logger.i(TAG,"返回banner图片数量:" + serverResponseDataList.size());
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverResponseDataList != null && serverResponseDataList.size()>0) {
            HomeBannerAdapter adapter = new HomeBannerAdapter(HomeActivity.this, serverResponseDataList);
            mGallery.setAdapter(adapter);
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

    /**
     * 验证是否需要登录
     * @param result
     * @return
     * @throws JSONException
     */
    private boolean invilidateLogin(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        String responseCode = jsonObject.getString("response");
        // 如果从服务器端返回的是"notlogin",说明用户未登录,返回true
        if ("notlogin".equals(responseCode)) {
            return true;
        }
        return false;
    }

}