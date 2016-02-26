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
    public static final int LOAD_DATA = 0;
    public static final int LOAD_MORE = 1;

    private SubscribeMode mSubscribeMode;
    private SubscribeView mSubscribeView;
    private SubscribeBean mData;
    private SubscribeSubscrib mSubsCallBack;

    public SubscribePresent(SubscribeView mSubscribeView) {
        this.mSubscribeView = mSubscribeView;
        mSubscribeMode = new SubscribeMode();
    }


    public void requestData(String subscribeId,boolean isCache){
        Observable<SubscribeBean> subscribeMode = mSubscribeMode.getSubscribeMode(subscribeId, isCache);
        subscribeMode.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new SubscribeSubscrib(LOAD_DATA));
    }

    /**
     * 加载更多
     * 获取最后一项ID，根据最后一项id 请求
     */
    public void loadMore(String subscribeId) {
        if(mData!=null){
            String lastId = String.valueOf(mData.getStories().get(mData.getStories().size() - 1).getId());//获取最后一项ID
            Observable<SubscribeBean> moreSubsc = mSubscribeMode.getMoreSubscribe(subscribeId, lastId);
            moreSubsc.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new SubscribeSubscrib(LOAD_MORE));
        }
    }

    private class SubscribeSubscrib extends Subscriber<SubscribeBean>{

        int loadType;

        public SubscribeSubscrib(int loadType) {
            this.loadType = loadType;
        }

        /**
         * 设置加载类型
         */
        public void setLoadType(int loadType){
            this.loadType = loadType;
        }
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
        public void onNext(SubscribeBean o) {
            System.out.println("onNext");
            if(null != o){
                if(loadType == LOAD_DATA){
                    System.out.println("下拉刷新完成");
                    mData = o;
                    mSubscribeView.setListData(o);
                }else{
                    System.out.println("加载更多了");
                    mData.getStories().addAll(o.getStories());
                    mSubscribeView.notifyLoadMoreData(o);
                }
            }
        }
    }
}
