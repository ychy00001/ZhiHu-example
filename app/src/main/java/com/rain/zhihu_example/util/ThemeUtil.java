package com.rain.zhihu_example.util;

import android.app.Activity;
import com.rain.zhihu_example.global.Constances;

/**
 * 主题工具类  用于切换白天黑夜主题
 * @author yangchunyu
 *         2016/2/18
 *         10:46
 */
public class ThemeUtil {

    private Activity mActivity;

    public ThemeUtil(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //提供主题切换
    public void changeTheme(){
        boolean isLight = isThemeDark();
        SPUtil.putBoolean(Constances.SP_IS_NIGHT_THEME, !isLight);
        mActivity.recreate();
    }

    /**
     * 判断是否白天模式
     */
    public boolean isThemeDark(){
        return SPUtil.getBool(Constances.SP_IS_NIGHT_THEME, false);
    }
}
