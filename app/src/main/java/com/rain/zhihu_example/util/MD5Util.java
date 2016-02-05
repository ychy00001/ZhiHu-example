package com.rain.zhihu_example.util;

import java.security.MessageDigest;

/**
 * MD5加密
 * Created by Diagrams on 2016/1/5 14:01
 */
public class MD5Util {
    public static String encode(String string) throws Exception {
        byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
