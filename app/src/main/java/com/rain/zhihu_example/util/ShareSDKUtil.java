package com.rain.zhihu_example.util;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * ShareSDK 工具类
 * 用于处理第三方登录 分享的工具类
 * @author yangchunyu
 *         2016/3/7
 *         15:08
 */
public class ShareSDKUtil {
    //登录第三方平台
    public static void login(Platform platform, PlatformActionListener listener){
        if(platform.isValid()){
            platform.removeAccount();
        }
        platform.setPlatformActionListener(listener);
        platform.authorize();
    }
}
