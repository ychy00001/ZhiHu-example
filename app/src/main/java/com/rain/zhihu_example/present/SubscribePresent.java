package com.rain.zhihu_example.present;

import com.rain.zhihu_example.mode.SubscribeMode;
import com.rain.zhihu_example.mode.bean.SubscribeBean;
import com.rain.zhihu_example.ui.view.SubscribeView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 控制订阅标签的主导者
 * @author yangchunyu
 *         2016/2/22
 *         14:48
 */
public class SubscribePresent {
    private SubscribeMode mSubscribeMode;
    private SubscribeView mSubscribeView;

    public SubscribePresent(SubscribeView mSubscribeView) {
        this.mSubscribeView = mSubscribeView;
        mSubscribeMode = new SubscribeMode();
    }


    public void requestData(String subscribeId,boolean isCache){
        Observable<SubscribeBean> subscribeMode = mSubscribeMode.getSubscribeMode(subscribeId, isCache);
        subscribeMode.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new SubscribeSubscrib());
    }

    private class SubscribeSubscrib extends Subscriber{

        @Override
        public void onCompleted() {
            System.out.println("请求完成");
            mSubscribeView.loadDataComplete();
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("请求失败"+e.toString());
        }

        @Override
        public void onNext(Object o) {
            System.out.println("执行数据");
            mSubscribeView.setListData((SubscribeBean) o);
        }
    }
}
