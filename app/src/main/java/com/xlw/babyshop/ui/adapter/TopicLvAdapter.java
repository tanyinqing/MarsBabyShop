package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tandong.sa.zUImageLoader.core.DisplayImageOptions;
import com.tandong.sa.zUImageLoader.core.ImageLoader;
import com.tandong.sa.zUImageLoader.core.assist.ImageScaleType;
import com.tandong.sa.zUImageLoader.core.display.FadeInBitmapDisplayer;
import com.xlw.babyshop.application.XlwApplication;
import com.xlw.babyshop.model.TopicListModel;
import com.xlw.marsbabyshop.R;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/21.
 */
public class TopicLvAdapter extends BaseAdapter{

    private Context context;
    private List<TopicListModel> topicList;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    public TopicLvAdapter(Context context, List<TopicListModel> topicList) {
        this.context = context;
        this.topicList = topicList;

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
        return topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        if (convertView == null) {
            convertView = View.inflate(context,R.layout.topicproduct_list_items, null);
        }
        holder.goodsIv = (ImageView) convertView.findViewById(R.id.goodsIconIv);
        holder.tvName = (TextView) convertView.findViewById(R.id.textClothesName);
        holder.tvPrice = (TextView) convertView.findViewById(R.id.textClothesPrice);
        holder.tvMkPrice = (TextView) convertView.findViewById(R.id.textMarketPrice);
        //convertView.setTag("holder",holder);
        convertView.setTag(position);//给图片打标识

        TopicListModel info = topicList.get(position);
        holder.tvName.setText(info.getName());
        holder.tvPrice.setText(info.getPrice() + "");
        holder.tvMkPrice.setText(info.getMarketprice() + "");

        String imageUrl = info.getPic();

        String imgUrl = info.getPic();
        imageLoader.displayImage(imgUrl, holder.goodsIv, options);

        return convertView;
    }

    static class Holder {
        ImageView goodsIv;
        TextView tvName;
        TextView tvPrice;
        TextView tvMkPrice;
        TextView commNum;
    }
}
