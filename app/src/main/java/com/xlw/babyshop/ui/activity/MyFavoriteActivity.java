package com.xlw.babyshop.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xlw.babyshop.model.PageModel;
import com.xlw.babyshop.model.ProductModel;
import com.xlw.babyshop.ui.adapter.MyFavoriteAdapter;
import com.xlw.marsbabyshop.R;

import java.util.List;

public class MyFavoriteActivity extends BaseActivity {
    private static final String TAG = "MyFavoriteActivity";

    private ListView myfavorite_product_list;
    private PageModel pageVo;
    private View pageView;
    TextView previousPage;
    TextView nextPage;
    TextView textPage;

    MyFavoriteAdapter adapter;
    private List<ProductModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorite_activity);
    }

}
