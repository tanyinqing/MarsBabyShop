package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tandong.sa.zUImageLoader.core.DisplayImageOptions;
import com.tandong.sa.zUImageLoader.core.ImageLoader;
import com.tandong.sa.zUImageLoader.core.assist.ImageScaleType;
import com.tandong.sa.zUImageLoader.core.display.FadeInBitmapDisplayer;
import com.xlw.babyshop.application.XlwApplication;
import com.xlw.babyshop.model.BrandCategoryModel;
import com.xlw.marsbabyshop.R;

import java.util.List;

/**
 * 推荐品牌ListView适配器
 */
public class BrandAdapter extends BaseExpandableListAdapter {

    private List<BrandCategoryModel> list;
    private Context context;

    public BrandAdapter(Context context, List<BrandCategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.brand_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.textParent);
        tv.setText(list.get(groupPosition).getKey());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.brand_child_list, null);
        GridView gv = (GridView) convertView.findViewById(R.id.nineGv);

        gv.setAdapter(new MyGridViewAdapter(context,list.get(groupPosition)));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

class MyGridViewAdapter extends BaseAdapter{

    private Context context;
    private BrandCategoryModel brandCategory;
    private String id;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    public MyGridViewAdapter(Context context, BrandCategoryModel brandCategory) {
        this.context = context;
        this.brandCategory = brandCategory;

        imageLoader = XlwApplication.getInstance().getImageLoader();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.product_loading)         // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.category_diaper01)     // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.category_diaper01)          // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)            // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)              // 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)   // 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)    // 设置图片的解码类型
                .resetViewBeforeLoading(true)   // 设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    @Override
    public int getCount() {
        return brandCategory.getValue().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        final View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.brand_child_item, null);
        } else {
            view = convertView;
        }

        holder.iv = (ImageView) view.findViewById(R.id.brandIconIv);
        //iv.setImageResource(brandCategory.getValue().get(position));

        id = brandCategory.getValue().get(position).getId() + "";
        view.setTag(position);

        String imageUrl = brandCategory.getValue().get(position).getPic();
        imageLoader.displayImage(imageUrl, holder.iv, options);

        return convertView;
    }

    static class Holder {
        ImageView iv;
    }

}