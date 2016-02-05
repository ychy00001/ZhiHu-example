package com.rain.zhihu_example.util;

import android.content.Context;

/**
 * 关于View的工具类
 * Created by yangchunyu
 * 2016/1/27 10:01
 */
public class ViewUtil {
    /**
     * dp转换px
     * @param ctx 上下文
     * @param dp dp值
     * @return 像素值
     */
    public static int dp2px(Context ctx, float dp){
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int)((density*dp) + 0.5f);
    }

    /**
     * px转换dp
     * @param ctx 上下文
     * @param px dp值
     * @return dp值
     */
    public static int px2dp(Context ctx,float px){
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int)((px/density) + 0.5f);
    }
}
