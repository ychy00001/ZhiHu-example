package com.rain.zhihu_example.util;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

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

    //分享至新浪微博
    public static void shareSina(String title,String body,String imgUrl,String webUrl,PlatformActionListener mListener){
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setTitle(title);
        sp.setImageUrl(imgUrl);
        sp.setText(body);
        sp.setUrl(webUrl);
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(mListener); // 设置分享事件回调
        // 执行图文分享
        weibo.share(sp);
    }

    public static void shareWX(String title,String body,String imgUrl,String webUrl,PlatformActionListener mListener){
        Wechat.ShareParams sp = new Wechat.ShareParams();
        //微信特有
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setUrl(webUrl);

        sp.setTitle(title);
        sp.setImageUrl(imgUrl);
        sp.setText(body);
        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
        wx.setPlatformActionListener(mListener); // 设置分享事件回调
        // 执行图文分享
        wx.share(sp);
    }

    public static void shareWxMoment(String title,String body,String imgUrl,String webUrl,PlatformActionListener mListener){
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        //微信特有
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setUrl(webUrl);

        sp.setTitle(title);
        sp.setImageUrl(imgUrl);
        sp.setText(body);
        Platform wxMoment = ShareSDK.getPlatform(WechatMoments.NAME);
        wxMoment.setPlatformActionListener(mListener); // 设置分享事件回调
        // 执行图文分享
        wxMoment.share(sp);
    }

    public static void shareQQ(String title,String body,String imgUrl,String webUrl,PlatformActionListener mListener){
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(webUrl);
        sp.setImageUrl(imgUrl);
        sp.setText(body);
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(mListener); // 设置分享事件回调
        // 执行图文分享
        qq.share(sp);
    }

    public static void shareQZone(String title,String body,String imgUrl,String webUrl,PlatformActionListener mListener){
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(title);
        sp.setImageUrl(imgUrl);
        sp.setText(body);
        //QQ空间特有
        sp.setSiteUrl("http://daily.zhihu.com/");
        sp.setSite("知乎日报");
        sp.setTitleUrl(webUrl);

        Platform qZone = ShareSDK.getPlatform(QZone.NAME);
        qZone.setPlatformActionListener(mListener); // 设置分享事件回调
        // 执行图文分享
        qZone.share(sp);
    }


}
