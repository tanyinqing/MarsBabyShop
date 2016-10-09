package com.xlw.babyshop.ui.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by xinliwei on 2015/7/24.
 * 使用 SparseArray来存储view的引用，代替了原本的ViewHolder，不用声明一大堆View，简洁明了
 */
public class ViewHolder {

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}

/* 在适配器中使用
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_lsv_main, null);
		}
		TextView nameTxtv = ViewHolder.get(convertView, R.id.item_name_txtv);
		nameTxtv.setText(mTestList.get(position));
		return convertView;
	}
 */
