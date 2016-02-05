package com.rain.zhihu_example.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.rain.zhihu_example.util.CommonUtil;
import com.rain.zhihu_example.widget.ContentView;

/**
 * Created by yangchunyu
 * 2016/1/25 16:44
 */
public abstract class BaseFragment extends Fragment {

    protected ContentView mContentPage;
    protected Context mContext;
    public BaseFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        if(mContentPage == null) {
            mContentPage = new ContentView(getActivity()) {
                @Override
                protected void loadData() {
                    requestData();
                }

                @Override
                protected View createSuccessView() {
                    return getSuccessView();
                }
            };
        }else{
            //移除View
            CommonUtil.removeChildFrameParent(mContentPage);
        }
        ButterKnife.bind(this,mContentPage);
        return mContentPage;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * 代理请求成功View
     */
    protected abstract View getSuccessView();
    /**
     * 请求数据
     */
    protected abstract void requestData();

    /**
     *
     * 启动一个activity伴随着动画
     * @param intent 意图
     * @param animType 动画类型
     *
     */
    protected void startActivity(Intent intent, int animType){
        ((BaseActivity)getActivity()).startActivity(intent,animType);
    }

    /**
     * 开启一个activity伴随有返回
     * @param intent 意图
     * @param requestCode 请求码
     * @param animType 动画类型
     */
    protected void startActivityForResult(Intent intent, int requestCode, int animType){
        ((BaseActivity)getActivity()).startActivityForResult(intent,requestCode,animType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(getActivity());
    }
}
