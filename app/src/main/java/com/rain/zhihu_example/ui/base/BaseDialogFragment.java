package com.rain.zhihu_example.ui.base;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.view.View;
import butterknife.ButterKnife;

/**
 * @author yangchunyu
 *         2016/3/16
 *         14:30
 */
@SuppressWarnings("all")
public class BaseDialogFragment extends DialogFragment {

    public View setContentView(View view) {
        // 使用ButterKnife对Fragment进行初始化
        ButterKnife.bind(this,view);
        return view;
    }

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
