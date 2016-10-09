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
import com.xlw.babyshop.model.CategoryModel;
import com.xlw.babyshop.utils.Logger;
import com.xlw.marsbabyshop.R;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/19.
 */
public class CategoryAdapter extends BaseAdapter{

    private static final String TAG = "CategoryAdaper";

    private List<CategoryModel> categoryInfos;
    private Context context;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    public CategoryAdapter(Context context, List<CategoryModel> categoryInfos) {
        this.context = context ;
        this.categoryInfos = categoryInfos;

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
        Logger.i(TAG, "" + categoryInfos.size());
        return categoryInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 优化2:应用了ViewHolder模式
        ViewHolder holder = new ViewHolder();   // 它的作用就在于减少不必要的调用findViewById

        // 优化1:重用回收的View布局
        if(convertView == null){
            // convertView 是避免多次inflating View,重用回收的item view
            convertView = View.inflate(context, R.layout.category_item, null);
            holder.tv_content = (TextView) convertView.findViewById(R.id.textContent);
            holder.tv_describe = (TextView) convertView.findViewById(R.id.item_describe);
            holder.iv = (ImageView) convertView.findViewById(R.id.imgIcon);

            convertView.setTag(holder); // 把控件引用存在ViewHolder里面，再通过View.setTag(holder)把它放在view里，下次就可以直接取了
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        CategoryModel vo = categoryInfos.get(position);
        holder.tv_content.setText(vo.getName());
        holder.tv_describe.setText(vo.getTag());

        // 处理图片加载
        String imgUrl = vo.getPic();    // 获得图片的网络路径
        imageLoader.displayImage(imgUrl, holder.iv, options);

        return convertView;
    }

    static class ViewHolder{
        ImageView iv;
        TextView tv_content;
        TextView tv_describe;
    }
}
