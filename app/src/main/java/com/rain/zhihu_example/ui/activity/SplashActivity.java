package com.rain.zhihu_example.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.api.Apis;
import com.rain.zhihu_example.global.ImageLoaderOptions;
import com.rain.zhihu_example.mode.SplashMode;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.util.CommonUtil;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.*;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.io.File;
import java.io.IOException;


/**
 * 进入页面：读取缓存图片 设置给ImgView 若图片错误，则显示默认图片
 * Splash页面,首页页面请求网络 读取到图片的链接  然后启动异步任务 将图片保存成缓存
 *
 * Created by yangchunyu
 *       2016/1/4 14:47
 */
public class SplashActivity extends BaseActivity implements Animation.AnimationListener{
    public static final String TAG = "SplashActivity";

    private ImageView mImgStart;//背景图
    private Context mContext;

    private TextView mTvSplsahText;
    private TextView mTitle;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        assignViews();
        loadCache();
        //开始动画
        initImageAnim();
        saveCache();
    }

    //请求网络 保存数据至缓存
    private void saveCache() {
        //初始化OkHttp所用的缓存 以及OKhttp请求
        File cacheFile = new File(mContext.getCacheDir(), "zhihu");
        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(getInterceptor(false))
                .build();
        //初始化Retrofit
        Retrofit.Builder build = new Retrofit.Builder()
                .baseUrl(Apis.ZH_HOST)
                .addConverterFactory(GsonConverterFactory.create());
        build.client(client);

        Retrofit retrofit = build.build();

        SplashServer service = retrofit.create(SplashServer.class);
        //调用接口的方法 其实内部进行了url拼接 准备请求等操作 因为service实际是一个动态代理类
        Call<SplashMode> call = service.requestSplash();
        call.enqueue(new Callback<SplashMode>() {
            @Override
            public void onResponse(Response<SplashMode> response) {

            }

            @Override
            public void onFailure(Throwable t) {
                showNormalView();
            }
        });

    }

    public Interceptor getInterceptor(final boolean isCache) {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                if(isCache){
                    builder.cacheControl(CacheControl.FORCE_CACHE);//强制请求网络
                }else{
                    builder.cacheControl(CacheControl.FORCE_NETWORK);//强制请求网络
                }

                return chain.proceed(builder.build()).newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age="+Integer.MAX_VALUE+",max-stale="+Integer.MAX_VALUE)
                        .build();
            }
        };
    }
    /**
     * 加载缓存数据
     */
    private void loadCache() {
        //初始化OkHttp所用的缓存 以及OKhttp请求
        File cacheFile = new File(mContext.getCacheDir(), "zhihu");
        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(getInterceptor(true))
                .build();
        //初始化Retrofit
        Retrofit.Builder build = new Retrofit.Builder()
                .baseUrl(Apis.ZH_HOST)
                .addConverterFactory(GsonConverterFactory.create());
        build.client(client);

        Retrofit retrofit = build.build();

        SplashServer service = retrofit.create(SplashServer.class);
        //调用接口的方法 其实内部进行了url拼接 准备请求等操作 因为service实际是一个动态代理类
        Call<SplashMode> call = service.requestSplash();
        call.enqueue(new Callback<SplashMode>() {
            @Override
            public void onResponse(Response<SplashMode> response) {
                final SplashMode splashMode = response.body();
                //成功
                if (splashMode != null) {
                    CommonUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            String imgUrl = splashMode.getImg();
                            final String text = splashMode.getText();
                            ImageLoader.getInstance().displayImage(imgUrl,mImgStart, ImageLoaderOptions.splash_options,new SimpleImageLoadingListener(){
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    mTitle.setVisibility(View.VISIBLE);
                                    if (!TextUtils.isEmpty(text)) {
                                        mTvSplsahText.setText(text);
                                    }
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    super.onLoadingFailed(imageUri, view, failReason);
                                    mTitle.setVisibility(View.VISIBLE);
                                    if (!TextUtils.isEmpty(text)) {
                                        mTvSplsahText.setText(text);
                                    }
                                }
                            });
//                            Picasso picasso = Picasso.with(RainApplication.getContext());
//                            RequestCreator load = picasso.load(imgUrl);
//                            load.into(mImgStart, new com.squareup.picasso.Callback() {
//                                @Override
//                                public void onSuccess() {
//                                    mTitle.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(text)) {
//                                        mTvSplsahText.setText(text);
//                                    }
//                                }
//
//                                @Override
//                                public void onError() {
//                                    mTitle.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(text)) {
//                                        mTvSplsahText.setText(text);
//                                    }
//                                    mImgStart.setImageResource(R.mipmap.splash);
//                                }
//                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showNormalView();
            }
        });

    }

    //展示默认图
    private void showNormalView() {
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mTitle.setVisibility(View.VISIBLE);
                mImgStart.setImageResource(R.mipmap.splash);
            }
        });
    }

    /**
     * 初始化View
     */
    private void assignViews() {
        mImgStart = (ImageView) findViewById(R.id.img_start);
        mTvSplsahText = (TextView) findViewById(R.id.tv_splsah_text);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mTitle.setVisibility(View.GONE);
    }

    /**
     * 初始化启动图动画
     */
    private void initImageAnim() {// 图片动画
        Animation animation = new ScaleAnimation(1.0f, 1.09f, 1.0f, 1.09f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); // 将图片放大1.2倍，从中心开始缩放
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(3000); // 动画持续时间
        animation.setFillAfter(true); // 动画结束后停留在结束的位置
        animation.setAnimationListener(SplashActivity.this); // 添加动画监听
        mImgStart.startAnimation(animation); // 启动动画
    }

    /*********************** 生命周期处理 ****************************/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mImgStart.destroyDrawingCache();
        mImgStart.setImageBitmap(null);
        super.onDestroy();
    }


    /****************动画监听回调*************************/

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        toMainActivity(TAG);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }



    interface  SplashServer{
        @Headers("Cache-Control: public, max-age=604800 ,max-stale=2419200")
        @GET(Apis.ZH_SPLASH_IMG_URL)
        Call<SplashMode> requestSplash();
    }
}
