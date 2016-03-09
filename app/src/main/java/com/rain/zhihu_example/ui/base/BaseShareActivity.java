package com.rain.zhihu_example.ui.base;

import android.content.Intent;
import android.os.Bundle;
import cn.sharesdk.framework.ShareSDK;
import com.rain.zhihu_example.ui.activity.LoginActivity;

/**
 * 用于ShareSDK基类Activity  处理了初始化以及销毁的操作
 * @author yangchunyu
 *         2016/3/7
 *         11:17
 */
public abstract class BaseShareActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initSDK 可重复调用
        ShareSDK.initSDK(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        //stopSDK 不可重复调用  如果调用stop 必须重新initSDK才可，否则报空指针
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    /**
     * 启动登录页
     */
    protected void startLoginActivity(int requestCode) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivityForResult(intent,requestCode,BaseActivity.TRANS_TYPE_TRANSLATE);
    }
}
