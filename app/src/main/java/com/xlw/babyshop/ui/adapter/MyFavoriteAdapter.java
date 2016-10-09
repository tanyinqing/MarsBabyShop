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
import com.xlw.babyshop.model.ProductModel;
import com.xlw.marsbabyshop.R;

import java.util.List;

public class MyFavoriteAdapter extends BaseAdapter{

    Context mContext;
    List<ProductModel> productModels;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    public MyFavoriteAdapter(Context context, List<ProductModel> list) {
        this.mContext = context;
        this.productModels = list;

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
        return productModels.size();
    }

    @Override
    public Object getItem(int position) {
        return productModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ProductViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.my_favorite_listitem, null);
            holder = new ProductViewHolder();
            holder.goodsIv = (ImageView) view.findViewById(R.id.myfavorite_product_img);
            holder.tvName = (TextView) view.findViewById(R.id.myfavorite_title_text);
            holder.tvPrice = (TextView) view.findViewById(R.id.myfavorite_price_text);
            holder.tvMkPrice = (TextView) view.findViewById(R.id.myfavorite_nostock_text);
            view.setTag(R.layout.my_favorite_listitem, holder);
        } else {
            view = convertView;
            holder = (ProductViewHolder) view.getTag(R.layout.my_favorite_listitem);
        }
        view.setTag(position);

        ProductModel item = (ProductModel)getItem(position);
        holder.goodsIv.setBackgroundResource(R.drawable.product_loading);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice() + "");
        holder.tvMkPrice.setText(item.getMarketprice() + "");

        String imgUrl = item.getPic();
        imageLoader.displayImage(imgUrl, holder.goodsIv, options);

        return view;
    }

    public class ProductViewHolder {
        ImageView goodsIv;
        TextView tvName;
        TextView tvPrice;
        TextView tvMkPrice;
    }
}
