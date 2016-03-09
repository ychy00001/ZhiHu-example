package com.rain.zhihu_example.global;

/**
 * Created by yangchunyu
 * 2016/1/6 13:49
 */
public class Constances {
    //sharedPreference 名称
    public static final String SP_NAME = "smalltown";

    //获取主题模式 日间还是夜间
    public static final String SP_IS_NIGHT_THEME = "isNightTheme";

    //StoryId 用于Intent传送数据
    public static final String STORY_ID = "storyId";
    public static final String STORY_TITLE = "storyTitle";
    public static final String STORY_TYPE = "storyType";
    public static final String STORY_IMG = "storyImg";
    public static final String IS_STORY_IMG = "isShowStoryImg";//是否显示story头IMG

    //订阅ID以及名称
    public static final String ID_SUBSCRIBE = "subscribeId";
    public static final String NAME_SUBSCRIBE = "subscribeName";



    /**!start 请求响应码*/
    //ResultCode
    public static final int CODE_RESULT_LOGIN_FINISH = 1001;//登录成功

    //RequestCode
    public static final int CODE_REQUEST_LOGIN = 1002;//登录的requestCode
    public static final int CODE_REQUEST_COLLECTION = 1003;//收藏页面的requestCode
    public static final int CODE_REQUEST_CONTENT_DETAIL= 1004;//点击收藏请求的Code


    /**!end 请求响应码*/
}
