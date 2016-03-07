package com.rain.zhihu_example.present;

import android.text.TextUtils;
import android.util.Log;
import com.rain.zhihu_example.mode.StoryMode;
import com.rain.zhihu_example.mode.bean.StoryBean;
import com.rain.zhihu_example.ui.view.StoryView;
import com.rain.zhihu_example.util.BuildConfigUtil;
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
    public static final String TAG = "ContentDetailPresent";

    private StoryView mView;
    private StoryMode mMode;
    private boolean isThemeDark;

    public ContentDetailPresent(StoryView mView, boolean isThemeDark) {
        this.mView = mView;
        this.isThemeDark = isThemeDark;
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
        }

        @Override
        public void onError(Throwable e) {
            //加载失败
            if(BuildConfigUtil.DEBUG){
                Log.e(ContentDetailPresent.TAG,e.toString());
            }
            if(mView != null){
                mView.setVebView(null);
            }
        }

        @Override
        public void onNext(StoryBean storyBean) {
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
                String start;
                if(isThemeDark){
                    start  = "<body class='night'>";
                }else{
                    start = "<body>";
                }
                String end = "</body>";
                String body = cssHead.toString()+start+storyBean.getBody()+end;
                //改变webView字体颜色
                mView.setVebView(body);
                if(TextUtils.isEmpty(storyBean.getImage())){
                    mView.setTitleImg(null,null,null);
                }else{
                    mView.setTitleImg(storyBean.getImage(),storyBean.getTitle(),storyBean.getImage_source());
                }

            }
        }
    }

}
