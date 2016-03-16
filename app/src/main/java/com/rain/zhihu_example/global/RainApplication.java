package com.rain.zhihu_example.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;


/**
 * Created by Diagrams on 2015/12/22 16:37
 */
public class RainApplication extends Application {
    /**
     * app入口
     */
    private static Context mContext;
    private static Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Context
        mContext = this;
        mainHandler = new Handler();

    }
    public static Context getContext(){
        return mContext;
    }
    public static Handler getHandler(){
        return mainHandler;
    }
}
