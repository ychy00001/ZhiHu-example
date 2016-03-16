package com.rain.zhihu_example.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.rain.zhihu_example.global.RainApplication;
import com.rain.zhihu_example.mode.bean.LoginBean;

/**
 * 登录工具类 用于用户登录的一些操作
 * @author yangchunyu
 *         2016/3/7
 *         14:32
 */
public class LoginUtil {
    private static final String PRE_NAME = "SmallTownLoginXML";

    public static final String KEY_UID = "uid"; //id
    public static final String KEY_TOKEN = "token"; //令牌
    public static final String KEY_NICKNAME = "nickname"; //昵称
    public static final String KEY_ICON = "displayIcon"; //ico
    public static final String KEY_EXPIRES_TIME = "expires_time"; // 授权时间


    private LoginUtil(){}

    private SharedPreferences mPreferences;

    private static class Holder {
        public static final LoginUtil INSTANCE = new LoginUtil();
    }

    /**
     * 获取登录工具类实例
     */
    public static LoginUtil getInstance(Context context) {
        return Holder.INSTANCE.init(context);
    }

    private LoginUtil init(Context context) {
        if (context == null)
            context = RainApplication.getContext();
        if (mPreferences == null)
            mPreferences = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        return this;
    }


    /**
     * 保存登录信息
     */
    public void saveLoginInf(LoginBean bean){
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putString(KEY_UID, bean.getUserId());
        edit.putString(KEY_NICKNAME, bean.getNickName());
        edit.putString(KEY_ICON, bean.getIco());
        edit.putString(KEY_TOKEN, bean.getToken());
        edit.putLong(KEY_EXPIRES_TIME, bean.getExpiresTime()).apply();
    }

    //读取用户id
    public String getUid(){
        return mPreferences.getString(KEY_UID, "");
    }

    //获取用户昵称
    public String getUserName(){
        return mPreferences.getString(KEY_NICKNAME, "");
    }

    //获取用户头像
    public String getIco(){
        return mPreferences.getString(KEY_ICON, "");
    }

    //获取令牌信息
    public String getToken(){
        return mPreferences.getString(KEY_TOKEN, "");
    }

    //获取令牌保存时间
    public Long getExpiresTime(){
        return mPreferences.getLong(KEY_EXPIRES_TIME, -1);
    }

    //检查用户是否登录
    public boolean checkLogin() {
        return !TextUtils.isEmpty(getUid());
    }

    //清除登录信息
    public void clearInfo() {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.clear().apply();
    }
}
