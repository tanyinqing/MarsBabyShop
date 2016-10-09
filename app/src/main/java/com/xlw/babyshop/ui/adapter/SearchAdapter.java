package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xlw.marsbabyshop.R;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class SearchAdapter extends BaseAdapter{

    private Context context;
    private String[] search;

    public SearchAdapter(Context context,String[] search){
        this.context = context;
        this.search = search;
    }

    @Override
    public int getCount() {
        return search.length+1;
    }

    @Override
    public Object getItem(int position) {
        return search[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position==0){
            TextView tv = (TextView) View.inflate(context, R.layout.search_item, null);
            tv.setTextSize(35);
            tv.setTextColor(Color.RED);
            tv.setText("热门搜索");
            return tv;
        }
        TextView tv_content =  (TextView) View.inflate(context, R.layout.search_item, null);
        tv_content.setTextColor(Color.BLACK);
        tv_content.setText("   " + search[position-1]);

        return tv_content;
    }
}
