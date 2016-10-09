package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
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
import com.xlw.babyshop.model.LimitbuyModel;
import com.xlw.marsbabyshop.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xinliwei on 2015/7/21.
 */
public class LimitbuyAdapter extends BaseAdapter{

    private static final String TAG = "LimitbuyAdapter";

    private List<LimitbuyModel> list;
    private Context context;
    private boolean isPlay;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    private SimpleDateFormat simpleDateFormat;

    public LimitbuyAdapter(Context context, List<LimitbuyModel> list) {
        this.context = context;
        this.list = list;

        simpleDateFormat = new SimpleDateFormat("dd 天  HH:mm:ss");

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
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        View view;
        if (convertView != null) {
            view = convertView;
            holderView = (HolderView) convertView.getTag(R.layout.product_list_timelimit_items);
        } else {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.product_list_timelimit_items, null);
            holderView = new HolderView();
            // 建立对应
            holderView.goodsIconIv = (ImageView) view.findViewById(R.id.goodsIconIv);
            holderView.textClothesName = (TextView) view.findViewById(R.id.textClothesName);
            holderView.textClothesPrice = (TextView) view.findViewById(R.id.textClothesPrice);
            holderView.textMarketPrice = (TextView) view.findViewById(R.id.textMarketPrice);
            // holderView.textProductComment = (TextView)convertView.findViewById(R.id.textProductComment);
            holderView.textProductCommentNum = (TextView) view.findViewById(R.id.textProductCommentNum);
            view.setTag(R.layout.product_list_timelimit_items, holderView);
        }

        holderView.textClothesName.setText(list.get(position).getName() + "");
        holderView.textClothesPrice.setText("￥" + String.valueOf(list.get(position).getLimitprice() + ""));
        holderView.textMarketPrice.setText("原价：￥" + String.valueOf(list.get(position).getPrice()));

        // 定时器
        LimitbuyModel item = (LimitbuyModel)getItem(position);
        long v = item.getLefttime() - System.currentTimeMillis();
        Date date = new Date(v);
        holderView.textProductCommentNum.setText(simpleDateFormat.format(date));

        String imgUrl = item.getPic();
        imageLoader.displayImage(imgUrl, holderView.goodsIconIv, options);

        return view;
    }

    public class HolderView {
        public ImageView goodsIconIv;
        public TextView textClothesName;
        public TextView textClothesPrice;
        public TextView textMarketPrice;
        // public TextView textProductComment;
        public TextView textProductCommentNum;
    }
}
