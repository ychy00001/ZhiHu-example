package com.rain.zhihu_example.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.ui.base.BaseActivity;

/**
 * 登录界面-第三方登录
 *
 * @author yangchunyu
 *         2016/3/2
 *         11:35
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.btn_sina_login) Button mBtnSinaLogin;//新浪微博登录
    @Bind(R.id.btn_tencent_login) Button mBtnTencentLogin;//腾讯微博登录
    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolBarSetting();
        initListener();
    }

    /**初始化事件监听*/
    private void initListener() {
        mBtnSinaLogin.setOnClickListener(this);
        mBtnTencentLogin.setOnClickListener(this);
    }


    /**
     * 关于toolbar的设置
     */
    private void toolBarSetting() {
        //设置左上角的返回按钮
        mToolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.setTitle("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sina_login:
                //TODO 新浪微博登录

                break;
            case R.id.btn_tencent_login:
                //TODO 腾讯微博登录

                break;
            default:
                finish();
        }
    }
}
