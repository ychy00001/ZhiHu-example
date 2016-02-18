package com.rain.zhihu_example.util;

import android.content.SharedPreferences;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.global.RainApplication;

/**
 * SharePreference工具类
 * @author yangchunyu
 *         2016/2/18
 *         11:08
 */
public class SPUtil {
    private static SharedPreferences mSp;
    /**
     *获取boolean数据
     */
    public static boolean getBool(String key,boolean defValue){
        return getSharedPreference().getBoolean(key,defValue);
    }

    /**
     * 保存boolean数据
     */
    public static boolean putBoolean(String key,boolean value){
        return getSharedPreference().edit().putBoolean(key,value).commit();
    }

    /**
     *获取String数据
     */
    public static String getString(String key,String defValue){
        return getSharedPreference().getString(key,defValue);
    }


    /**
     * 获取sharePreference
     */
    private static SharedPreferences getSharedPreference(){
        if(mSp == null){
            mSp = RainApplication.getContext().getSharedPreferences(Constances.SP_NAME, RainApplication.MODE_PRIVATE);
        }
        return mSp;
    }


}
