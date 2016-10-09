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
import com.xlw.babyshop.model.BulletinModel;
import com.xlw.marsbabyshop.R;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/21.
 */
public class BulletinLvAdapter extends BaseAdapter{

    private Context context;
    private List<BulletinModel> bulletinInfos;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    public BulletinLvAdapter(Context context, List<BulletinModel> bulletinInfos) {
        this.context = context;
        this.bulletinInfos = bulletinInfos;

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
        return bulletinInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return bulletinInfos.get(position);
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
            view = View.inflate(context, R.layout.prom_bulletin_item, null);
        } else {
            view = convertView;
        }
        view.setTag(position);

        BulletinModel info = bulletinInfos.get(position);
        holder.tvContext = (TextView) view.findViewById(R.id.textContent);
        holder.tvContext.setText(info.getName());
        holder.imIcon = (ImageView) view.findViewById(R.id.imgIcon);
        holder.imIcon.setImageResource(R.drawable.image5);

        String imgUrl = info.getPic();
        imageLoader.displayImage(imgUrl, holder.imIcon, options);

        return view;
    }

    static class Holder {
        TextView tvContext;
        ImageView imIcon;
    }

}
