package com.xlw.babyshop.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xinliwei on 2015/7/18.
 *
 * 根据数值画圆弧
 */
public class RingView extends View {

    private int width,height;   // 屏幕的宽和高
    private int i;      // 画圆环的比例

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取屏幕的宽，高
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        // 将圆心设置在屏幕中心
        int pointWidth = width / 2;
        int pointHeight = height / 2;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(220, 220, 220));
        canvas.drawCircle(pointWidth, pointHeight, 100, paint);

        // 在其上边叠加一个画弧的Canvas，颜色设置为红色
        paint.setColor(Color.RED);
        RectF f = new RectF(pointWidth - 100, pointHeight - 100, pointWidth + 100, pointHeight + 100);
        canvas.drawArc(f, -90f, i, true, paint);

        // 想要变成圆环状，需要在其上边再次绘制一个圆形，以遮挡住弧形，实现圆环状的：颜色设置为白色
        paint.setColor(Color.WHITE);
        canvas.drawCircle(pointWidth, pointHeight, 80, paint);

        //绘制文字
        paint.setColor(Color.BLACK);
        //计算出数字的长度
        float lenTxt = paint.measureText(String.valueOf(i));
        canvas.drawText(String.valueOf(i), pointWidth - lenTxt / 2, pointHeight, paint);
    }

    public void setI(int i){
        this.i = i;
        this.invalidate();  // 重绘自身
    }
}
