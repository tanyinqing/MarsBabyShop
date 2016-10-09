package com.xlw.babyshop.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Created by xinliwei on 2015/7/19.
 *
 * 自定义的Gallery
 */
public class MyGallery extends Gallery {

    public MyGallery(Context context) {
        super(context);
    }

    public MyGallery(Context context, AttributeSet attr){
        super(context, attr);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
