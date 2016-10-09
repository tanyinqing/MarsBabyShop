package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlw.babyshop.model.HomeCategoryModel;
import com.xlw.marsbabyshop.R;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/19.
 *
 * 首页栏目Adapter
 */
public class HomeCategoryAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<HomeCategoryModel> categroy;

    public HomeCategoryAdapter(Context context, List<HomeCategoryModel> categroy) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categroy = categroy;
    }

    @Override
    public int getCount() {
        return categroy.size();
    }

    @Override
    public Object getItem(int position) {
        return categroy.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        HomeCategoryModel item = categroy.get(position);
        if (convertView == null) {
            view = inflater.inflate(R.layout.home_category_item, null);
        } else {
            view = convertView;
        }
        ((TextView) view.findViewById(R.id.textContent)).setText(item.getTitle());
        ((ImageView)view.findViewById(R.id.imgIcon)).setBackgroundResource(item.getImgresid());

        return view;
    }
}
