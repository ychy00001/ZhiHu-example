package com.rain.zhihu_example.api;

import com.rain.zhihu_example.mode.HomeMode;
import com.rain.zhihu_example.mode.bean.StoryBean;
import com.rain.zhihu_example.mode.bean.StroyExtraBean;
import com.rain.zhihu_example.mode.bean.SubscribeBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yangchunyu
 * 2016/1/6 11:20
 */
public class Apis {

    public static final String ZH_HOST = "http://news-at.zhihu.com";


    //测试URL
//    public static final String ZH_SPLASH_IMG_URL = "http://10.19.166.41:8080/test/splash.txt";


    /*******************************************分割线*************************/

    //启动页图片
    public static final String ZH_SPLASH_IMG_URL = "/api/4/start-image/1080*1920";
    //主页主题信息
    public static final String ZH_HOME_THEMES = "/api/4/themes";
    //主页最新信息
    public static final String ZH_HOME_LATEST = "/api/4/stories/latest";
    //主页后一天信息
    public static final String ZH_HOME_BEFORE = "/api/4/stories/before/20160125";
    //内容详情页
    public static final String ZH_CONTEN_STORY = "/api/4/story/";
    //内容详情页的额外内容
    public static final String ZH_CONTEN_STORY_EXTRA = "/api/4/story-extra/";

    /**
     * 加载首页数据
     */
    public interface MainDataService {
        @GET(ZH_HOME_LATEST)
        Call<HomeMode> requestHomeData();
    }

    /**
     * 加载更多首页数据
     */
    public interface MainLoadMoreDataService{
        @GET("/api/4/stories/before/{date}")
        Call<HomeMode> requestMoreData(@Path("date")String date);
    }

    /**
     * 加载内容详情页数据
     */
    public interface ContentDetailService{
        @GET("/api/4/story/{storyId}")
        Observable<StoryBean> requestStory(@Path("storyId")String stroyId);
    }

    /**
     * 加载内容详情页额外(不知道何用 先不管)
     */
    public interface ContentDetailExtraService{
        @GET("/api/4/story-extra/{storyId}")
        Observable<StroyExtraBean> requestExtraStory(@Path("storyId")String stroyId);
    }

    /**
     * 加载订阅标签数据
     */
    public interface  SubscribeService{
        @GET("/api/4/theme/{subscribeId}")
        Observable<SubscribeBean> requestSubscribe(@Path("subscribeId")String subscribeId);
    }

    /**
     * /api/4/theme/11/before/7258022
     * 加载更多订阅标签数据
     */
    public interface  SubscribeMoreService{
        @GET("/api/4/theme/{subscribeId}/before/{lastSubscribeId}")
        Observable<SubscribeBean> requestSubscribe(@Path("subscribeId")String subscribeId,@Path("lastSubscribeId")String lastId);
    }
}
