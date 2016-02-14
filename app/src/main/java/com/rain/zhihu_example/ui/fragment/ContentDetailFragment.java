package com.rain.zhihu_example.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.api.Apis;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.present.ContentDetailPresent;
import com.rain.zhihu_example.ui.base.BaseFragment;
import com.rain.zhihu_example.ui.view.StoryView;
import com.rain.zhihu_example.util.BuildConfigUtil;
import com.rain.zhihu_example.widget.ScrollWebView;
import com.squareup.picasso.Picasso;

/**
 * 内容详情页面
 * Created by yangchunyu
 * 2016/2/4 10:35
 *
 */
public class ContentDetailFragment extends BaseFragment implements StoryView {

    public static final String TAG = "ContentDetailFragment";

    @Bind(R.id.webView) ScrollWebView mWebView;
    @Bind(R.id.toolbar) Toolbar mToolBar;
    @Bind(R.id.titleLayout) RelativeLayout mImgLayout;//头布局 包括文字 图片
    @Bind(R.id.img_title) ImageView mTitleImg;
    @Bind(R.id.tv_title) TextView mTitleText;
    @Bind(R.id.tv_author) TextView mAuthorText;
    @Bind(R.id.layout_content) RelativeLayout mContentLayout;

    private String storyId;
    private ContentDetailPresent mPresent;
    private int titleHeight;//头布局的高度
    private int titleImgHeight;//头部imgView的高度


    public static ContentDetailFragment newInstance(Bundle bundle) {
        ContentDetailFragment mFragment = new ContentDetailFragment();
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImgLayout.measure(0,0);
        mToolBar.measure(0,0);
        titleImgHeight = mImgLayout.getMeasuredHeight();
        titleHeight = titleImgHeight+mToolBar.getMeasuredHeight() ;

        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setBlockNetworkImage(true);
        mWebSettings.setDomStorageEnabled(true);

        mWebView.setWebViewClient(new WebClient());
        mWebView.setBackgroundResource(R.color.transparent);
        mWebView.setBackgroundColor(0);
        mWebView.setOnScrollListener(new MyScrollListener());
    }

    @Override
    protected View getSuccessView() {
        return View.inflate(mContext, R.layout.fragment_content_detail,null);
    }

    @Override
    protected void requestData() {
        mPresent = new ContentDetailPresent(this);
        storyId = getArguments().getString(Constances.ID_STORY);
        //请求网络
        if(BuildConfigUtil.DEBUG){
            Log.e(TAG, "requestData请求ID:" + storyId);
        }
        mPresent.getStory(storyId);
    }

    @Override
    public void setVebView(String body) {
        mWebView.loadDataWithBaseURL(Apis.ZH_HOST, body, "text/html", "utf-8", null);
         //通知页面更新
        mContentPage.notifyDataChange(body);
    }

    @Override
    public void setTitleImg(String imgUrl, String imgTitle, String author) {
        Picasso.with(mContext).load(imgUrl).into(mTitleImg);
        mTitleText.setText(imgTitle);
        mAuthorText.setText(author);
    }


    /**
     * WebView滑动事件
     */
    private class MyScrollListener implements ScrollWebView.OnScrollChangeListener{

        RelativeLayout.LayoutParams imgParam;
        CoordinatorLayout.LayoutParams contentParam;
        @Override
        public void onScroll(int newLeft, int newTop, int oldLeft,int oldTop) {
            int dy = newTop - oldTop;
            imgParam = (RelativeLayout.LayoutParams)mImgLayout
                            .getLayoutParams();
            contentParam = (CoordinatorLayout.LayoutParams)mContentLayout
                            .getLayoutParams();
            System.out.println("topMargin:"+contentParam.topMargin);
            //新滑动高度小于实际的头布局的高度 则需要进行高度处理
            System.out.println("newTop:"+newTop+"titleHeight:"+titleHeight);
            if(newTop>0 && newTop < titleHeight){
                System.out.println("更改界面");
                    imgParam.height -= dy;
                    contentParam.topMargin -=dy;
            }else if(newTop == 0){//如果新的高度等于0 则显示完整的高度
                System.out.println("重置界面 完全显示");
                imgParam.height = titleImgHeight;

                contentParam.topMargin = 0;
            }else{//否则置为0
                System.out.println("隐藏头布局");
                imgParam.height = 0;
                contentParam.topMargin = -titleImgHeight;
            }
            mImgLayout.setLayoutParams(imgParam);
            mContentLayout.setLayoutParams(contentParam);
        }
    }
    /**
     * WebView的具体处理
     */
    private class WebClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.getSettings().setBlockNetworkImage(false);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
