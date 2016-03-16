package com.rain.zhihu_example.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.util.CommonUtil;

/**
 * Created by yangchunyu
 * 2015/12/23 9:54
 */
public abstract class ContentView extends FrameLayout{

    public ContentView(Context context) {
        super(context);
        initContentPage();
    }

    public ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContentPage();
    }

    public ContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContentPage();
    }

    /*************************分割线****************************/

    public static final int STATE_LOADING = 0;//加载中
    public static final int STATE_SUCCESS = 1;//加载成功
    public static final int STATE_ERROR = 2;//加载失败

    private View loadingView; //加载页
    private View successView; //成功页
    private View errorView; //失败页

    private int mState = STATE_LOADING;//当前状态
    /**
     * 初始化页面
     * 天然在ContentView中添加三种状态页面
     */
    private void initContentPage() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if(loadingView == null){
            loadingView = View.inflate(getContext(), R.layout.loading_page_loading,null);
        }
        addView(loadingView,params);

        if (errorView == null) {
            errorView = View.inflate(getContext(), R.layout.loading_page_error, null);
            RelativeLayout rl_page_error = (RelativeLayout)errorView.findViewById(R.id.rl_page_error);
            //设置点击事件 触发重新加载页面
            rl_page_error.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mState = STATE_LOADING;
                    showPage();
                    loadDataAndRefreshPage();
                }
            });
        }
        addView(errorView, params);

        if (successView == null) {
            successView = createSuccessView();
        }
        if (successView != null) {
            addView(successView, params);
        } else {
            throw new IllegalArgumentException("createSuccessView not success ,maybe return not a View or null");
        }

        //显示当前默认View
        showPage();
        //请求数据
        loadDataAndRefreshPage();
    }

    /**
     * 加载网络数据
     */
    private void loadDataAndRefreshPage() {
        new Thread(){
            @Override
            public void run() {
                loadData();
            }
        }.start();
    }

    /**
     * 通知更新页面数据
     * @param data 接受到的数据
     */
    public void notifyDataChange(Object data){
        mState = checkData(data);
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        });
    }

    /**
     * 直接展示成功页面
     */
    public void showSuccessPage(){
        mState = checkData(new Object());
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        });
    }
    /**
     * 根据返回数据的类型来判断当前的状态
     * @param data 数据对象
     * @return 页面状态
     */
    private int checkData(Object data) {
        if(data ==null){
            return STATE_ERROR;
        }else{
            return STATE_SUCCESS;
        }
    }


    /**
     * 显示页面 先隐藏所有页面 是那种状态显示哪种页面
     */
    private void showPage() {
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        successView.setVisibility(View.GONE);
        switch (mState) {
            case STATE_SUCCESS:
                successView.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                errorView.setVisibility(View.VISIBLE);
                break;
            case STATE_LOADING:
                loadingView.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 用于创建成功返回页面
     * @return 返回成功页
     */
    protected abstract View createSuccessView();

    /**
     * 加载网络数据
     * 会启动子线程
     */
    protected abstract void loadData();
}
