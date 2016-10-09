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
import com.xlw.babyshop.model.ProductListModel;
import com.xlw.marsbabyshop.R;

import java.util.List;

/**
 * 适配器
 */
public class ProductAdapter extends BaseAdapter{

    Context context;
    List<ProductListModel> list;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    public ProductAdapter(Context context, List<ProductListModel> list) {
        this.context = context;
        this.list = list;

        imageLoader = XlwApplication.getInstance().getImageLoader();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.product_loading)         // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)     // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)          // 设置图片加载/解码过程中错误时候显示的图片
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ProductViewHolder holder;
        if (convertView == null) {
            view = View.inflate(context, R.layout.product_list_items, null);
            holder = new ProductViewHolder();
            holder.goodsIv = (ImageView) view.findViewById(R.id.goodsIconIv);
            holder.tvName = (TextView) view.findViewById(R.id.textClothesName);
            holder.tvPrice = (TextView) view.findViewById(R.id.textClothesPrice);
            holder.tvMkPrice = (TextView) view.findViewById(R.id.textMarketPrice);
            holder.commNum = (TextView) view.findViewById(R.id.textProductCommentNum);
            view.setTag(R.layout.product_list_items, holder);
        } else {
            view = convertView;
            holder = (ProductViewHolder) view.getTag(R.layout.product_list_items);
        }

        ProductListModel item = (ProductListModel)getItem(position);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText("会员价:" + item.getPrice() + "");
        holder.tvMkPrice.setText("市场价:" + item.getMarketprice() + "");
        holder.commNum.setText(item.getComment_count() + "");
        view.setTag(position);

        String imageUrl = item.getPic();
        imageLoader.displayImage(imageUrl, holder.goodsIv, options);

        return view;
    }

    public class ProductViewHolder {
        ImageView goodsIv;
        TextView tvName;
        TextView tvPrice;
        TextView tvMkPrice;
        TextView commNum;
    }
}
