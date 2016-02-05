package com.rain.zhihu_example.present;

import com.rain.zhihu_example.mode.StoryMode;
import com.rain.zhihu_example.mode.bean.StoryBean;
import com.rain.zhihu_example.ui.view.StoryView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 内容详情的主导者
 * Created by yangchunyu
 * 2016/2/4 14:17
 */
public class ContentDetailPresent {
    private StoryView mView;
    private StoryMode mMode;

    public ContentDetailPresent(StoryView mView) {
        this.mView = mView;
        mMode = new StoryMode();
    }

    public void getStory(String storyID){
        Observable<StoryBean> storyMode = mMode.getStoryMode(storyID);
        storyMode.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new StorySubscribe());
    }

    /**
     * 网络请求后消息的订阅
     */
    private class StorySubscribe extends Subscriber<StoryBean> {

        @Override
        public void onCompleted() {
            //处理完成
            System.out.println("onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            //加载失败
            System.out.println("onError");
            if(mView != null){
                mView.setVebView(null);
            }
        }

        @Override
        public void onNext(StoryBean storyBean) {
            System.out.println("onNext");
            //处理数据
            if(mView != null && storyBean != null){
                String linkCss = "<link type ='text/css' rel='stylesheet' href='%s'/>";
                String linkJs = "<script type ='text/script' src='%s'/>";
                StringBuilder cssHead = new StringBuilder();
                for (String css : storyBean.getCss()){
                    cssHead.append(String.format(linkCss,css)+"\r\t");
                }
                for (String js : storyBean.getJs()){
                    cssHead.append(String.format(linkJs,js)+"\r\t");
                }
                mView.setVebView(cssHead.toString()+storyBean.getBody());
                mView.setTitleImg(storyBean.getImage(),storyBean.getTitle(),storyBean.getImage_source());
            }
        }
    }

}
