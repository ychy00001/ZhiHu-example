package com.rain.zhihu_example.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by yangchunyu
 *  on 2015/10/14.
 */
public class DrawableUtil {
    /**
     * 生成自定义Shape
     * @param radius 圆角角度
     * @param argb 颜色
     * @return
     */
    public static GradientDrawable generateDrawable(float radius, int argb){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(radius);
        drawable.setColor(argb);
        return drawable;
    }

    /**
     * 生成一个Selecter
     * @param press 按下
     * @param normal 正常
     * @return
     */
    public static StateListDrawable generateSeleter(Drawable press,Drawable normal){
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},press);
        drawable.addState(new int[]{},normal);
        return drawable;
    }
}
