package com.rain.zhihu_example.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.mode.bean.LoginBean;
import com.rain.zhihu_example.ui.base.BaseShareActivity;
import com.rain.zhihu_example.util.BuildConfigUtil;
import com.rain.zhihu_example.util.LoginUtil;
import com.rain.zhihu_example.util.ShareSDKUtil;

import java.util.HashMap;

/**
 * 登录界面-第三方登录
 *
 * @author yangchunyu
 *         2016/3/2
 *         11:35
 */
public class LoginActivity extends BaseShareActivity implements View.OnClickListener {

    public static final String TAG = "LoginActivity";

    public static final String RESULT_NICKNAME = "nick_name";
    public static final String RESULT_ICON = "icon";


    @Bind(R.id.btn_sina_login) Button mBtnSinaLogin;//新浪微博登录
    @Bind(R.id.btn_tencent_login) Button mBtnTencentLogin;//腾讯微博登录
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private PlatformActionListener mPaListener;//平台监听

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**初始化事件监听*/
    private void initListener() {
        mBtnSinaLogin.setOnClickListener(this);
        mBtnTencentLogin.setOnClickListener(this);
        mPaListener = new MyPlatformActionListener();
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
            case R.id.btn_sina_login://新浪微博登录
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
               ShareSDKUtil.login(sina,mPaListener);
                break;
            case R.id.btn_tencent_login://腾讯微博登录
                Platform tencentWb = ShareSDK.getPlatform(TencentWeibo.NAME);
                ShareSDKUtil.login(tencentWb,mPaListener);
                break;
            default:
                finish();
        }
    }

    /**
     * 平台监听
     */
    private class MyPlatformActionListener implements PlatformActionListener{

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if(BuildConfigUtil.DEBUG){
                Log.d(LoginActivity.TAG,"编号:"+i);
            }
            System.out.println("编号:"+i+"   ---onComplete");
            if(i == 1) {//授权成功
                LoginBean bean = new LoginBean();
                setDataToBean(bean,platform);//存储bean对象
                //标识用户登录 保存至SharedPreferences
                LoginUtil.getInstance(LoginActivity.this).saveLoginInf(bean);

                Intent intent = new Intent();
                intent.putExtra(LoginActivity.RESULT_NICKNAME,bean.getNickName());
                intent.putExtra(LoginActivity.RESULT_ICON,bean.getIco());
                setResult(Constances.CODE_RESULT_LOGIN_FINISH,intent);//返回上一级菜单
                finish();
            }
        }

        //设置数据至bean对象
        private void setDataToBean(LoginBean bean,Platform platform){
            bean.setToken(platform.getDb().getToken()); // 获取授权token
            bean.setExpiresTime(platform.getDb().getExpiresTime()); // 获取token时间
            bean.setUserId(platform.getDb().getUserId()); // 获取用户在此平台的ID
            bean.setNickName(platform.getDb().get("nickname"));// 获取用户昵称
            bean.setIco(platform.getDb().getUserIcon());
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            if(BuildConfigUtil.DEBUG){
                Log.e(LoginActivity.TAG,"登录错误:"+throwable.toString());
            }
            platform.removeAccount();
        }

        @Override
        public void onCancel(Platform platform, int i) {
            if(BuildConfigUtil.DEBUG){
                Log.d(LoginActivity.TAG,"取消登录");
            }
        }
    }
}
