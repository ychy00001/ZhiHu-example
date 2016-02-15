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
import com.nineoldandroids.view.ViewHelper;
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
    public static final int COLOR_TOOL_BAR = 0xFF27ADEC;
    public static final int COLOR_TOOL_BAR_TRANS = 0x0027ADEC;


    @Bind(R.id.webView) ScrollWebView mWebView;
    @Bind(R.id.toolbar) Toolbar mToolBar;
    @Bind(R.id.titleLayout) RelativeLayout mImgLayout;//头布局 包括文字 图片
    @Bind(R.id.img_title) ImageView mTitleImg;
    @Bind(R.id.tv_title) TextView mTitleText;
    @Bind(R.id.tv_author) TextView mAuthorText;
    @Bind(R.id.layout_content) RelativeLayout mContentLayout;

    private String storyId;
    private ContentDetailPresent mPresent;
//    private int titleHeight;//头布局的高度
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
        mContentLayout.measure(0,0);
        titleImgHeight = mImgLayout.getMeasuredHeight();


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
        CoordinatorLayout.LayoutParams contentParam = (CoordinatorLayout.LayoutParams)mContentLayout
        .getLayoutParams();
        int topMargin = contentParam.topMargin;
        int nowHeight = 0;//由于改变内容布局的marginTop 会影响onScroll的newTop数值
        int oldHeight = 0;//记录上个高度
        @Override
        public void onScroll(int newLeft, int newTop, int oldLeft,int oldTop) {
            int dy = newTop - oldTop;

            oldHeight = nowHeight;
            nowHeight += dy;
            imgParam = (RelativeLayout.LayoutParams)mImgLayout
                            .getLayoutParams();
            System.out.println("dy:"+dy);
            System.out.println("oldHeight:"+oldHeight+"  newHeight:"+nowHeight);

            //新滑动高度小于实际的头布局的高度 则需要进行高度处理
            if(nowHeight>=0 && nowHeight < titleImgHeight){
                if((imgParam.height-dy)<=titleImgHeight
                        && (imgParam.height-dy)>= 0){
                    imgParam.height -= dy;
                }else if((imgParam.height-dy)>titleImgHeight){
                    imgParam.height = titleImgHeight;
                }else{
                    imgParam.height = 0;
                }

//                if(dy%2 != 0){
//                    dy = dy/2+1;
//                }else{
//                    dy = dy/2;
//                }

                if((contentParam.topMargin - dy)<=topMargin
                        && (contentParam.topMargin-dy)>= 0){
                    contentParam.topMargin -= dy;
                    float percent = contentParam.topMargin * 1.0f / topMargin;
                    ViewHelper.setAlpha(mToolBar,percent);
                }else if((contentParam.topMargin- dy)>topMargin){
                    contentParam.topMargin = topMargin;
                    ViewHelper.setAlpha(mToolBar,1);
                }else{
                    contentParam.topMargin = 0;
                    ViewHelper.setAlpha(mToolBar,0);
                }

            }else if(nowHeight == 0){//如果新的高度等于0 则显示完整的高度
                //                System.out.println("重置界面 完全显示");
                imgParam.height = titleImgHeight;
                contentParam.topMargin = topMargin;
            }else if(nowHeight >= titleImgHeight) {//否则置为0
                //                System.out.println("隐藏头布局");
                imgParam.height = 0;
                contentParam.topMargin = 0;
            }
            mImgLayout.setLayoutParams(imgParam);
            mContentLayout.setLayoutParams(contentParam);
            mContentLayout.requestLayout();
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
