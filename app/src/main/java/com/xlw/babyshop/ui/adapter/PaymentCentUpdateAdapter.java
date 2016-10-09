package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.xlw.babyshop.model.CartProductModel;
import com.xlw.marsbabyshop.R;

import java.util.List;

public class PaymentCentUpdateAdapter extends BaseAdapter{

    private static final String TAG = "PaymentCentUpdateAdapter";

    private Context mContext;
    List<CartProductModel> cartProductModels;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    public PaymentCentUpdateAdapter(Context context) {
        this.mContext = context;
    }

    public PaymentCentUpdateAdapter(Context context, List<CartProductModel> list) {
        this.mContext = context;
        this.cartProductModels = list;

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
        return cartProductModels.size();
    }

    @Override
    public Object getItem(int position) {
        return cartProductModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.payment_center_items, null);

            holder = new Holder();
            holder.shopcar_item_prodImage_img = (ImageView) view.findViewById(R.id.shopcar_item_prodImage_img);
            holder.shopcar_item_prodName_text = (TextView) view.findViewById(R.id.shopcar_item_prodName_text);
            holder.shopcar_item_prodId_text = (TextView) view.findViewById(R.id.shopcar_item_prodId_text);
            holder.shopcar_item_prodPrice_text = (TextView) view.findViewById(R.id.shopcar_item_prodPrice_text);
            view.setTag(R.layout.payment_center_items, holder);
        } else {
            view = convertView;
            holder = (Holder) view.getTag(R.layout.payment_center_items);

        }
        CartProductModel item = (CartProductModel)getItem(position);
        holder.shopcar_item_prodImage_img.setBackgroundResource(R.drawable.product_loading);
        holder.shopcar_item_prodName_text.setText(item.getName() + "");
        holder.shopcar_item_prodPrice_text.setText(item.getPrice() + "");
        holder.shopcar_item_prodId_text.setText(item.getId() + "");
        Log.i(TAG, item.getName() + "");
        Log.i(TAG, item.getPrice()+"");
        Log.i(TAG, item.getId()+"");
        view.setTag(position);

        String imgUrl = item.getPic();
        imageLoader.displayImage(imgUrl, holder.shopcar_item_prodImage_img, options);

        return view;
    }

    public static class Holder{
        ImageView shopcar_item_prodImage_img;
        TextView shopcar_item_prodName_text;    //prodName
        TextView shopcar_item_prodId_text;      //编码：
        TextView shopcar_item_prodPrice_text;   //单价：
    }

}
