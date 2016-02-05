package com.rain.zhihu_example.api;

import android.support.annotation.NonNull;
import com.rain.zhihu_example.global.RainApplication;
import okhttp3.*;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by yangchunyu
 * 2016/1/28 17:17
 */
public class ApiFactory {

    private static final long CACHE_SIZE = 10 * 1024 * 1024;//缓存大小
    private static final String CACHE_NAME = "xcar-cache";//缓存文件名

    /**
     * 创建Retrofit服务
     * @param serviceClass Service接口的.class
     * @param baseUrl 基础Url
     * @param <T> 类型泛型
     * @return 返回相应的Observable
     */
    public static <T> T createServiceFrom(final Class<T> serviceClass, String baseUrl,boolean isCache) {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(getHttpClient(isCache))
                .build();
        return adapter.create(serviceClass);
    }

    /**
     * 获取OkHttpClient
     * @param isCache 是否加载缓存
     */
    private static OkHttpClient getHttpClient(final boolean isCache) {
        //初始化OkHttp所用的缓存 以及OKhttp请求
        Interceptor interceptor = getInterceptor(isCache);

        File cacheFile = new File(RainApplication.getContext().getCacheDir(), CACHE_NAME);
        final Cache cache = new Cache(cacheFile, CACHE_SIZE);

        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build();
    }

    /**
     * 获取拦截器
     * @param isCache 是否加载缓存
     */
    @NonNull
    private static Interceptor getInterceptor(final boolean isCache) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();

                //TODO 在此可以拦截URL 并添加数字签名
                String originUrl = request.url().toString();
                String sinUrl = originUrl+"";//签名的链接
                builder.url(sinUrl);

//                if(isCache){//读取缓存
//                    builder.cacheControl(CacheControl.FORCE_CACHE);//强制读取缓存
//                }else{//读取网络
//                    builder.cacheControl(CacheControl.FORCE_NETWORK);//强制请求网络
//                }
                Response response = chain.proceed(builder.build());
                if(isCache){
                    int maxAge = 20;//20秒
                    builder.addHeader("Cache-Control", "public, only-if-cached,max-age=" + maxAge);
                }else{
                    int maxAge = 20;//20秒
                    builder.addHeader("Cache-Control", "public,max-age=" + maxAge);
                }

//                if(isCache){
//                    int maxAge = 20;
//                    response.newBuilder()
//                            .removeHeader("Pragma")
//                            .header("Cache-Control", "public, only-if-cache, max-age=" + maxAge)
//                            .build();
//                }else{
//                    response.newBuilder()
//                            .removeHeader("Pragma")
//                            .header("Cache-Control", "public, no-cache")
//                            .build();
//                }

                return response;
            }
        };
    }


}
