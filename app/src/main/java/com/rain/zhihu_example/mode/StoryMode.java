package com.rain.zhihu_example.mode;

import com.rain.zhihu_example.api.ApiFactory;
import com.rain.zhihu_example.api.Apis;
import com.rain.zhihu_example.mode.bean.StoryBean;
import rx.Observable;

/**
 * Created by yangchunyu
 * 2016/2/4 14:28
 */
public class StoryMode {
    /**
     * 获取内容详情的Bean对象
     * @param storyId 需要传递id
     * @return 返回Observable
     */
    public Observable<StoryBean> getStoryMode(String storyId){
        Apis.ContentDetailService contentService = ApiFactory.createServiceFrom(Apis.ContentDetailService.class, Apis.ZH_HOST, true);
        return contentService.requestStory(storyId);
    }
}
