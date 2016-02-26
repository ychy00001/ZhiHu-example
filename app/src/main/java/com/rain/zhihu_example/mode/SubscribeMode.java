package com.rain.zhihu_example.mode;

import com.rain.zhihu_example.api.ApiFactory;
import com.rain.zhihu_example.api.Apis;
import com.rain.zhihu_example.mode.bean.SubscribeBean;
import rx.Observable;

/**
 * 订阅的模型类
 * @author yangchunyu
 *         2016/2/22
 *         14:16
 */
public class SubscribeMode {
    /**
     * 获取Subscribe对象
     * @return 返回Observable
     */
    public Observable<SubscribeBean> getSubscribeMode(String subscribeId,boolean isCache){
        Apis.SubscribeService subscribeService = ApiFactory.createServiceFrom(Apis.SubscribeService.class, Apis.ZH_HOST, isCache);
        return subscribeService.requestSubscribe(subscribeId);
    }

    /**
     * 获取SubscribeMore 对象
     */
    public Observable<SubscribeBean> getMoreSubscribe(String subscribeId,String lastSubscribeId){
        Apis.SubscribeMoreService moreService = ApiFactory.createServiceFrom(Apis.SubscribeMoreService.class, Apis.ZH_HOST,false);
        return moreService.requestSubscribe(subscribeId, lastSubscribeId);
    }
}
