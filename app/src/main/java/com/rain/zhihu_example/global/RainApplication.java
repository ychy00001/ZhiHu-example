package com.rain.zhihu_example.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import com.umeng.socialize.PlatformConfig;

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


        /**start youmeng社会化组件初始化*/

        //微信 appid appsecret（未替换）
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("3865618595","83439d041c22bf1447751998f2860d21");
        //QQ和Qzone appid appkey（未替换）
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        //QQ微博（未替换）
        PlatformConfig.setTencentWB("1105154231","O0XwnnzcppgmLtgJ");

        /**end youmeng社会化组件初始化*/
    }
    public static Context getContext(){
        return mContext;
    }
    public static Handler getHandler(){
        return mainHandler;
    }
}
