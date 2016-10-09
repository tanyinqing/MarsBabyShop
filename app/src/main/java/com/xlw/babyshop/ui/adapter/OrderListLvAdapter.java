package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tandong.sa.zUImageLoader.core.DisplayImageOptions;
import com.tandong.sa.zUImageLoader.core.ImageLoader;
import com.tandong.sa.zUImageLoader.core.assist.ImageScaleType;
import com.tandong.sa.zUImageLoader.core.display.FadeInBitmapDisplayer;
import com.xlw.babyshop.application.XlwApplication;
import com.xlw.babyshop.model.OrderListModel;
import com.xlw.babyshop.ui.activity.OrderListActivity;
import com.xlw.marsbabyshop.R;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/23.
 */
public class OrderListLvAdapter extends BaseAdapter{

    private static final String TAG = "OrderListLvAdapter";

    private Context context;
    private List<OrderListModel> orderInfos;
    private List<OrderListModel> cancelablelist;
    private List<OrderListModel> uncancelablelist;
    private OrderListActivity activity;

    private OrderListModel orderVo = null;

    private ImageLoader imageLoader;
    DisplayImageOptions options;        // 图像显示策略

    public OrderListLvAdapter(Context context, List<OrderListModel> orderInfos,
                              List<OrderListModel> cancelablelist, List<OrderListModel> uncancelablelist) {
        this.context = context;
        this.activity = (OrderListActivity) context;

        this.orderInfos = orderInfos;
        this.cancelablelist = cancelablelist;
        this.uncancelablelist = uncancelablelist;
        Log.i(TAG, "cancelablelist可以取消的数目为:" + cancelablelist.size());
        Log.i(TAG, "uncancelablelist不可以取消的数目为:" + uncancelablelist.size());

        imageLoader = XlwApplication.getInstance().getImageLoader();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.product_loading)         // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.product_01)     // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.product_01)          // 设置图片加载/解码过程中错误时候显示的图片
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
        return orderInfos.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i(TAG, "当前列表下总订单大小为："+ orderInfos.size());
        Log.i(TAG, "可取消订单的数目为："+ cancelablelist.size());
        Log.i(TAG, "不可取消订单的数目为："+ uncancelablelist.size());
        if(cancelablelist.size()>0 && position<=cancelablelist.size()-1){
            return cancelablelist.get(position);
        }else if(position>cancelablelist.size()-1 && position<=orderInfos.size()-1){
            return uncancelablelist.get(position-cancelablelist.size());
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "当前位置为：" + position);
        View view = null;
        if(cancelablelist.size()>0 && position<=cancelablelist.size()-1){
            view = View.inflate(context, R.layout.my_order_listitem_cancelable, null);
            Holder.orderId_text = (TextView) view.findViewById(R.id.orderId_text);
            Holder.orderPrice_text = (TextView) view.findViewById(R.id.orderPrice_text);
            Holder.orderTime_text = (TextView) view.findViewById(R.id.orderTime_text);
            Holder.orderState_text = (TextView)view.findViewById(R.id.orderState_text);
            Holder.textCancelOrder = (TextView)view.findViewById(R.id.textCancelOrder);

            Holder.textCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 回调,删除指定的订单
                    OrderListModel orderListModel = (OrderListModel)getItem(position);
                    activity.processCancelOrder(orderListModel.getOrderid());
                }
            });

            Holder.textCancelOrder.setTag(position);
        }else if(position>cancelablelist.size()-1 && position<=orderInfos.size()-1){
            view = View.inflate(context, R.layout.my_order_listitem, null);
            Holder.orderId_text = (TextView) view.findViewById(R.id.orderId_text);
            Holder.orderPrice_text = (TextView) view.findViewById(R.id.orderPrice_text);
            Holder.orderTime_text = (TextView) view.findViewById(R.id.orderTime_text);
            Holder.orderState_text = (TextView)view.findViewById(R.id.orderState_text);
        }else{
            Log.i(TAG, "XXXXXXXXXXXXXXXX");
        }

//		orderVo = this.paramList.get(position);
        if(cancelablelist.size()>0 && position<=cancelablelist.size()-1){
            orderVo = cancelablelist.get(position);
        }else if(position>cancelablelist.size()-1 && position<=orderInfos.size()-1){
            orderVo = uncancelablelist.get(position-cancelablelist.size());
        }else{
            Log.i(TAG, "XXXXXXXXXXXXXXXX");
        }
        Log.i(TAG, orderVo.getFlag() + "-可取消的上-" + orderVo.getStatus());
        paint(orderVo);

        return view;
    }

    private void paint(OrderListModel orderVo) {
        Holder.orderId_text.setText(orderVo.getOrderid() + "");
        Holder.orderPrice_text.setText(orderVo.getPrice()+"");
        Holder.orderTime_text.setText(orderVo.getTime()+"");
        Holder.orderState_text.setText(orderVo.getStatus()+"");
//		if("1".equals(orderVo.getFlag()+"")){
//			Holder.textCancelOrder.setText("取消订单");
//		}else if("2".equals(orderVo.getFlag()+"")){
//			Holder.textCancelOrder.setText("待确认");
//		}else if("3".equals(orderVo.getFlag()+"")){
//			Holder.textCancelOrder.setText("已完成");
//		}
        if("1".equals(orderVo.getFlag()+"")){
            Holder.textCancelOrder.setText("取消订单");
        }
    }
    static class Holder{
        static TextView orderId_text;       // 订单编号
        static TextView orderPrice_text;    // 总价
        static TextView orderTime_text;     // 下单时间
        static TextView orderState_text;    // 状态
        static TextView textCancelOrder;    // 取消订单
    }

}
