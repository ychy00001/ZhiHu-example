package com.rain.zhihu_example.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Diagrams on 2015/12/22 16:54
 */
public class SnackUtil {
    /**
     * 显示一个SnackBar
     * @param parent 父View
     * @param msg
     */
    public static void showMessage(View parent,String msg){
        showMessage(parent,msg,null);
    }
    /**
     * 显示一个SnackBar
     * @param parent 父View
     * @param msg 显示的信息
     * @param listener 用于SnackBar上Action的点击回调
     */
    public static void showMessage(View parent,String msg,View.OnClickListener listener){
        Snackbar.make(parent, msg, Snackbar.LENGTH_SHORT)
                .setAction("点我", listener).show();
    }

}
