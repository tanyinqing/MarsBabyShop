package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlw.babyshop.model.CartModel;
import com.xlw.babyshop.model.CartProductModel;
import com.xlw.babyshop.utils.ImageUtil;
import com.xlw.marsbabyshop.R;

import java.util.List;

/**
 * 购物车适配器
 */
public class ShoppingCarAdapter extends BaseAdapter {
    private Context context;
    private CartModel cart;
    private List<CartProductModel> productlist;
    private CartProductModel cartProduct;

    public ShoppingCarAdapter() {
        super();
    }

    public ShoppingCarAdapter(Context context, CartModel cart) {
        super();
        this.context = context;
        this.cart = cart;
        productlist = cart.productlist;
    }

    @Override
    public int getCount() {
        return productlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = new View(context);
        view = View.inflate(context, R.layout.shopping_car_listitem, null);

        cartProduct = productlist.get(position);

        final ImageView shopcar_item_prodImage_img= (ImageView) view.findViewById(R.id.shopcar_item_prodImage_img);
        TextView shopcar_item_prodName_text = new TextView(context);//名称
        shopcar_item_prodName_text = (TextView) view.findViewById(R.id.shopcar_item_prodName_text);
        TextView shopcar_item_prodId_text = new TextView(context);//编码
        shopcar_item_prodId_text = (TextView) view.findViewById(R.id.shopcar_item_prodId_text);
        TextView shopcar_item_prodPrice_text = new TextView(context);//单价
        shopcar_item_prodPrice_text = (TextView) view.findViewById(R.id.shopcar_item_prodPrice_text);
        TextView shopcar_item_prodCount_text = new TextView(context);//数量
        shopcar_item_prodCount_text = (TextView) view.findViewById(R.id.shopcar_item_prodCount_text);
        TextView shopcar_item_subtotal_text = new TextView(context);//小计
        shopcar_item_subtotal_text = (TextView) view.findViewById(R.id.shopcar_item_subtotal_text);
        EditText shopcar_item_prodCount_edit=(EditText) view.findViewById(R.id.shopcar_item_prodCount_edit);
        String imageUrl =cartProduct.pic;

        String imagePath = ImageUtil.getCacheImgPath().concat(ImageUtil.md5(imageUrl));
        System.out.println(imagePath);
        shopcar_item_prodImage_img.setTag(imagePath);
        System.out.println(12345);
        Bitmap bitmap = ImageUtil.loadImage(imagePath, imageUrl, new ImageUtil.ImageCallback() {

            @Override
            public void loadImage(Bitmap bitmap, String imagePath) {
                ImageView iView = (ImageView) shopcar_item_prodImage_img.findViewWithTag(imagePath);
                if(iView!=null){
                    iView.setImageBitmap(bitmap);
                }

            }
        } );
        if(bitmap==null){
            shopcar_item_prodImage_img.setImageResource(R.drawable.product_loading);
        }else{
            shopcar_item_prodImage_img.setImageBitmap(bitmap);
        }

        shopcar_item_prodId_text.setText(cartProduct.id + "");
        shopcar_item_prodName_text.setText(cartProduct.name);
        shopcar_item_prodPrice_text.setText(cartProduct.price + "");
        shopcar_item_prodCount_text.setText(cartProduct.prodNum + "");
        shopcar_item_prodCount_edit.setText(cartProduct.prodNum + "");
        shopcar_item_subtotal_text.setText(cartProduct.subtotal + "");

        return view;
    }
}
