package com.rain.zhihu_example.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.api.Apis;
import com.rain.zhihu_example.global.RainApplication;
import com.rain.zhihu_example.mode.SplashMode;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.util.CommonUtil;
import com.squareup.picasso.Picasso;
import okhttp3.*;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.io.File;
import java.io.IOException;


/**
 * Created by Diagrams on 2016/1/4 14:47
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
            }
        });

    }

    public Interceptor getInterceptor(final boolean isCache) {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();

                //TODO 在此可以拦截URL 并添加数字签名
//                String originUrl = request.url().toString();
//                String sinUrl = originUrl + "";//签名的链接
//                builder.url(sinUrl);

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
     * 加载数据请求首页图片链接
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
                            String text = splashMode.getText();
                            Picasso.with(RainApplication.getContext()).load(imgUrl).error(R.mipmap.splash).into(mImgStart);
                            mTitle.setVisibility(View.VISIBLE);
                            if (!TextUtils.isEmpty(text)) {
                                mTvSplsahText.setText(text);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                CommonUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mTitle.setVisibility(View.VISIBLE);
                        mImgStart.setImageResource(R.mipmap.splash);
                    }
                });
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
