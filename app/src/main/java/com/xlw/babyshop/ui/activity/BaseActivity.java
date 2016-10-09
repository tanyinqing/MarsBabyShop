package com.xlw.babyshop.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xlw.babyshop.application.XlwApplication;
import com.xlw.babyshop.utils.Constant;
import com.xlw.marsbabyshop.R;

/**
 * Created by xinliwei on 2015/7/19.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private static final String TAG = "BaseActivity";

    public static final int NOT_LOGIN = 403;            // 登录请求码
    public static final int LOGIN_SUCCESS = 10000000;   // 登录结果码

    protected int activityCase;
    protected ProgressDialog progressDialog;

    private View inflate;           // ContentView

    private RelativeLayout head_layout;     // 自定义ActionBar中的视图布局
    private Button headLeftBtn;     // 左边的button
    private Button headRightBtn;    // 右边的button
    private TextView head_title;    // 标题

    // 底部栏公共组件
    protected ImageView home;
    protected ImageView classify;
    protected ImageView search;
    protected ImageView shopCar;
    protected TextView textShopCarNum;
    protected ImageView more;

    protected ButtonClickListener buttonClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        XlwApplication.getInstance().addActvity(this);
//        setContentView(R.layout.activity_base);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_title);

        head_layout = (RelativeLayout)actionBar.getCustomView().findViewById(R.id.head_layout);
        head_title = (TextView) actionBar.getCustomView().findViewById(R.id.head_title);
        headLeftBtn = (Button) actionBar.getCustomView().findViewById(R.id.head_left);
        headRightBtn = (Button) actionBar.getCustomView().findViewById(R.id.head_right);

//        head_layout.setBackgroundResource(R.drawable.head_bg_home);
        buttonClickListener = new ButtonClickListener();
        headLeftBtn.setOnClickListener(buttonClickListener);
        headRightBtn.setOnClickListener(buttonClickListener);

        // 初始化视图布局,包括加载BottomTab,并为BottomTab上的ImageView添加监听器
//        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home: // 单击了home按钮
                break;
            case R.id.menu_home:
                startActivity(new Intent(BaseActivity.this, HomeActivity.class));
                break;
            case R.id.menu_search:
                startActivity(new Intent(BaseActivity.this, SearchActivity.class));
                break;
            case R.id.menu_order:
                startActivity(new Intent(BaseActivity.this, OrderListActivity.class));
                break;
            case R.id.menu_favorites:
//                startActivity(new Intent(BaseActivity.this, MyFavoriteActivity.class));
                break;
            case R.id.menu_history:
//                startActivity(new Intent(BaseActivity.this, ProductHistoryActivity.class));
                break;
            case R.id.menu_exit:
                XlwApplication.getInstance().exit();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NOT_LOGIN) {
            if (resultCode == LOGIN_SUCCESS) {
                // 再次启动异步任务类post请求服务器端
                // ??????????????????????/
            } else {
                finish();
            }
        }
    }

    /**
     * 显示提示框
     */
    protected void showProgressDialog() {
        if ((!isFinishing()) && (this.progressDialog == null)) {
            this.progressDialog = new ProgressDialog(this);
        }
        this.progressDialog.setTitle(getString(R.string.loadTitle));
        this.progressDialog.setMessage(getString(R.string.LoadContent));
        this.progressDialog.show();
    }

    /**
     * 关闭提示框
     */
    protected void closeProgressDialog() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }

    /**
     * 是否加载底部tab
     *
     * @return
     */
    protected Boolean isLoadBottomTab() {
        return true;
    }

    /**
     * 加载底部导航栏
     */
    protected void loadBottomTab(View view) {
        this.home = (ImageView)view.findViewById(R.id.imgHome);
        this.classify = (ImageView)view.findViewById(R.id.imgClassify);
        this.search = (ImageView)view.findViewById(R.id.imgSearch);
        this.shopCar = (ImageView)view.findViewById(R.id.imgShoppingCar);
        this.more = (ImageView)view.findViewById(R.id.imgMore);
        this.textShopCarNum = (TextView) view.findViewById(R.id.textShopCarNum);

        this.buttonClickListener = new ButtonClickListener();
        this.home.setOnClickListener(buttonClickListener);
        this.classify.setOnClickListener(buttonClickListener);
        this.search.setOnClickListener(buttonClickListener);
        this.shopCar.setOnClickListener(buttonClickListener);
        this.more.setOnClickListener(buttonClickListener);
    }

    /**
     * 设置底部导航栏切换
     *
     * @param paramViewId
     */
    protected void selectedBottomTab(int paramViewId) {
        // 重新初始化所有的底部按钮为初始状态
        this.home.setBackgroundResource(R.drawable.bar_home_normal);
        this.classify.setBackgroundResource(R.drawable.bar_class_normal);
        this.search.setBackgroundResource(R.drawable.bar_search_normal);
        this.shopCar.setBackgroundResource(R.drawable.bar_shopping_normal);
        this.more.setBackgroundResource(R.drawable.bar_more_normal);

        switch (paramViewId) {
            case Constant.HOME:
                Constant.defaultIndex = Constant.HOME;
                this.home.setBackgroundResource(R.drawable.bar_home_selected);
                break;
            case Constant.CLASSIFY:
                this.classify.setBackgroundResource(R.drawable.bar_class_selected);
                Constant.defaultIndex = Constant.CLASSIFY;
                break;
            case Constant.SEARCH:
                this.search.setBackgroundResource(R.drawable.bar_search_selected);
                Constant.defaultIndex = Constant.SEARCH;
                break;
            case Constant.SHOPCAR:
                this.shopCar.setBackgroundResource(R.drawable.bar_shopping_selected);
                Constant.defaultIndex = Constant.SHOPCAR;
                break;
            case Constant.MORE:
                this.more.setBackgroundResource(R.drawable.bar_more_selected);
                Constant.defaultIndex = Constant.MORE;
                break;
        }
    }

    // 自定义的按钮单击事件监听器
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgHome:
//                    ToastUtil.showShortMsg(BaseActivity.this,"to HomeActivity");
                    startActivity(new Intent(BaseActivity.this, HomeActivity.class));
                    break;
                case R.id.imgClassify:  // 单击了"分类"按钮
//                    ToastUtil.showShortMsg(BaseActivity.this,"to CategoryActivity");
                    startActivity(new Intent(BaseActivity.this, CategoryActivity.class));
                    break;
                case R.id.imgSearch:
                    startActivity(new Intent(BaseActivity.this, SearchActivity.class));
                    break;
                case R.id.imgShoppingCar:
                    startActivity(new Intent(BaseActivity.this, ShoppingCarActivity.class));
                    break;
                case R.id.imgMore:
//                    startActivity(new Intent(BaseActivity.this, MoreActivity.class));
                    break;
                case R.id.head_left:
                    onHeadLeftButton(v);
                    break;
                case R.id.head_right:
                    onHeadRightButton(v);
                    break;
            }
        }
    }

    protected void onHeadLeftButton(View v) {
        finish();
    }

    protected void onHeadRightButton(View v) {
    }

    protected void setHeadLeftText(CharSequence text) {
        headLeftBtn.setText(text);
    }

    protected void setHeadLeftText(int resid) {
        headLeftBtn.setText(resid);
    }

    protected void setHeadLeftBackgroundResource(int resid) {
        headLeftBtn.setBackgroundResource(resid);
    }

    protected void setHeadLeftVisibility(int visibility) {
        headLeftBtn.setVisibility(visibility);
    }

    protected void setHeadRightText(CharSequence text) {
        headRightBtn.setText(text);
    }

    protected void setHeadRightText(int resid) {
        headRightBtn.setText(resid);
    }

    protected void setHeadRightBackgroundResource(int resid) {
        headRightBtn.setBackgroundResource(resid);
    }

    protected void setHeadRightVisibility(int visibility) {
        headRightBtn.setVisibility(visibility);
    }

    protected void setHeadBackgroundResource(int resid) {
        head_layout.setBackgroundResource(resid);
    }

    protected void BackgroundDrawable(Drawable d) {
        head_layout.setBackgroundDrawable(d);
    }

    @Override
    public void setTitle(CharSequence title) {
        head_title.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        head_title.setText(titleId);
    }

    // 自定义回调接口
    public interface DataCallback<T> {
        void processData(T paramObject, boolean paramBoolean);
    }
}