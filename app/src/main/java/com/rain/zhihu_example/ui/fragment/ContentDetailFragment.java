package com.rain.zhihu_example.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.webkit.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.nineoldandroids.view.ViewHelper;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.api.Apis;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.present.ContentDetailPresent;
import com.rain.zhihu_example.ui.activity.ContentDetailActivity;
import com.rain.zhihu_example.ui.base.BaseFragment;
import com.rain.zhihu_example.ui.view.StoryView;
import com.rain.zhihu_example.util.BuildConfigUtil;
import com.rain.zhihu_example.util.ThemeUtil;
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
    @Bind(R.id.titleLayout) LinearLayout mTitleLayout;//外部布局 头布局 包括文字 图片
    @Bind(R.id.img_title) ImageView mTitleImg;
    @Bind(R.id.tv_title) TextView mTitleText;
    @Bind(R.id.tv_author) TextView mAuthorText;
    @Bind(R.id.layout_content) RelativeLayout mContentLayout;

    private Toolbar mToolBar;
    private ContentDetailPresent mPresent;
    private int titleImgHeight;//头部imgView的高度
    private boolean isDark;


    public static ContentDetailFragment newInstance(Bundle bundle) {
        ContentDetailFragment mFragment = new ContentDetailFragment();
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ThemeUtil mThemeUtil = new ThemeUtil(getActivity());
        isDark = mThemeUtil.isThemeDark();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    @SuppressWarnings("all")
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleLayout.measure(0,0);
        mContentLayout.measure(0,0);
        titleImgHeight = mTitleLayout.getMeasuredHeight();
        mToolBar = ((ContentDetailActivity)getActivity()).getToolbar();


        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setBlockNetworkImage(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setJavaScriptEnabled(true);


        mWebView.setBackgroundResource(R.color.transparent);
        mWebView.setBackgroundColor(0);
        mWebView.setWebViewClient(new WebClient());
        mWebView.setWebChromeClient(new WebChromeClient());
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
        mPresent = new ContentDetailPresent(this,isDark);
        String storyId = getArguments().getString(Constances.STORY_ID);
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
        if(TextUtils.isEmpty(imgUrl)){
            mTitleLayout.setVisibility(View.GONE);
            return;
        }
        Picasso.with(mContext).load(imgUrl).into(mTitleImg);
        mTitleText.setText(imgTitle);
        mAuthorText.setText(author);
    }

    @Override
    public void setShareMsg(String title, String imgUrl, String shareUrl) {
        ((ContentDetailActivity)getActivity()).setShareMsg(title,imgUrl,shareUrl);
    }

    /**
     * WebView滑动事件
     */
    private class MyScrollListener implements ScrollWebView.OnScrollChangeListener{

        RelativeLayout.LayoutParams titleParam = (RelativeLayout.LayoutParams) mTitleLayout
                .getLayoutParams();
        CoordinatorLayout.LayoutParams contentParam = (CoordinatorLayout.LayoutParams)mContentLayout
        .getLayoutParams();
        int nowHeight = 0;//由于改变内容布局的marginTop 会影响onScroll的newTop数值
        int oldHeight = 0;//记录上个高度

        @Override
        public void onScroll(int newLeft, int newTop, int oldLeft,int oldTop) {
            float dy = (newTop - oldTop);

            oldHeight = nowHeight;
            nowHeight += dy;
            //新滑动高度小于实际的头布局的高度 则需要进行高度处理
            if(nowHeight>=0 && nowHeight <= titleImgHeight){
                if((titleParam.height-dy)<=titleImgHeight
                        && (titleParam.height-dy)>= 0){
                    titleParam.height -= dy;
                    float percent = titleParam.height * 1.0f / titleImgHeight;
                    if(percent > 0.2f){
                        percent -= 0.2f;
                    }else{
                        percent = 0;
                    }
                    ViewHelper.setAlpha(mToolBar,percent);
                }else if((titleParam.height-dy) > titleImgHeight){//处理滑动速度过快 一下超出头部高度
                    titleParam.height = titleImgHeight;
                    ViewHelper.setAlpha(mToolBar,1);
                }else{
                    titleParam.height = 0;
                    ViewHelper.setAlpha(mToolBar,0);
                }
            }else if(nowHeight == 0){//如果新的高度等于0 则显示完整的高度 还原
                titleParam.height = titleImgHeight;
            }else if(nowHeight >= titleImgHeight) {//否则置为0
                titleParam.height = 0;
                if(dy>0){
                    ViewHelper.setAlpha(mToolBar,0);
                }else{
                    ViewHelper.setAlpha(mToolBar,1);
                }
            }
            mTitleLayout.setLayoutParams(titleParam);
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
            Uri uri = Uri.parse(url);
            Intent intent = new  Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
