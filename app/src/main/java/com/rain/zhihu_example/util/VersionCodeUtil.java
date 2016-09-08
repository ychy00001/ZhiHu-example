package com.rain.zhihu_example.util;

/**
 * 获取版本号
 * Created by Rain on 2016/9/5.
 */
public class VersionCodeUtil {
    public static int getAndroidOSVersion()
    {
        int osVersion;
        try
        {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        }
        catch (NumberFormatException e)
        {
            osVersion = 0;
        }

        return osVersion;
    }
}
