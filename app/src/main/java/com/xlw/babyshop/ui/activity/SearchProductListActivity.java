package com.xlw.babyshop.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xlw.marsbabyshop.R;

public class SearchProductListActivity extends BaseActivity {

    private RelativeLayout head_layout;     // 自定义ActionBar中的视图布局

    private ButtonClickListener buttonClickListener;
    private Button headLeftBtn;     // 左边的button
    private Button headRightBtn;    // 右边的button
    private TextView head_title;    // 标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_product_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_title);

        head_layout = (RelativeLayout)actionBar.getCustomView().findViewById(R.id.head_layout);
        head_title = (TextView) actionBar.getCustomView().findViewById(R.id.head_title);
        headLeftBtn = (Button) actionBar.getCustomView().findViewById(R.id.head_left);
        headRightBtn = (Button) actionBar.getCustomView().findViewById(R.id.head_right);

        buttonClickListener = new ButtonClickListener();
        headLeftBtn.setOnClickListener(buttonClickListener);
        headRightBtn.setOnClickListener(buttonClickListener);
    }

    // 自定义的按钮单击事件监听器
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
}
