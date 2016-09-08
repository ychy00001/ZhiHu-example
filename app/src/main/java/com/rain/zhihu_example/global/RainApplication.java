package com.rain.zhihu_example.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


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
        initImageLoader(this);

    }
    public static Context getContext(){
        return mContext;
    }
    public static Handler getHandler(){
        return mainHandler;
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //		  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);//配置下载图片的线程优先级
        config.denyCacheImageMultipleSizesInMemory();//不会在内存中缓存多个大小的图片
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//为了保证图片名称唯一
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        //内存缓存大小默认是：app可用内存的1/8
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
        //		ImageLoader.getInstance().init( ImageLoaderConfiguration.createDefault(this));
    }
}
