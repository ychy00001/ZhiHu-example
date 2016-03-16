package com.rain.zhihu_example.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.ui.base.BaseDialogFragment;
import com.rain.zhihu_example.util.ShareSDKUtil;
import com.rain.zhihu_example.util.ToastUtil;

import java.util.HashMap;

/**
 * 分享页面
 * @author yangchunyu
 *         2016/3/16
 *         13:44
 */
public class ShareFragment extends BaseDialogFragment implements PlatformActionListener {

    public static final String TAG = "ShareFragment";

    public static final String SHARE_TITLE = "shareTitle";
    public static final String SHARE_IMG = "shareImg";
    public static final String SHARE_BODY = "shareBody";
    public static final String SHARE_WEB_URL = "shareWebUrl";

    //分享内容
    private String mShareTitle;
    private String mShareImgUrl;
    private String mShareUrl;
    private String mShareBody;

    public static ShareFragment newInstance(Bundle args){
        ShareFragment fragment = new ShareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //initSDK 可重复调用
        ShareSDK.initSDK(getActivity());
        getDialog().setTitle("分享");
        return setContentView(inflater.inflate(R.layout.fragment_share,null));
    }

    public void show(Bundle bundle, FragmentManager manager, String tag) {
        mShareBody = bundle.getString(SHARE_BODY);
        mShareImgUrl = bundle.getString(SHARE_IMG);
        mShareTitle= bundle.getString(SHARE_TITLE);
        mShareUrl = bundle.getString(SHARE_WEB_URL);
        super.show(manager, tag);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.ll_sina)
    void shareSina(){
        //新浪微博
        ShareSDKUtil.shareSina(mShareTitle,mShareBody,mShareImgUrl,mShareUrl,this);
    }

    @OnClick(R.id.ll_weixin)
    void shareWX(View view){
        //微信
        ShareSDKUtil.shareWX(mShareTitle,mShareBody,mShareImgUrl,mShareUrl,this);
    }

    @OnClick(R.id.ll_weixin_friend)
    void shareWxFriend(View view){
        //微信朋友圈
        ShareSDKUtil.shareWxMoment(mShareTitle,mShareBody,mShareImgUrl,mShareUrl,this);
    }

    @OnClick(R.id.ll_qq)
    void shareQQ(View view){
        //QQ
        ShareSDKUtil.shareQQ(mShareTitle,mShareBody,mShareImgUrl,mShareUrl,this);
    }

    @OnClick(R.id.ll_qzone)
    void shareQZone(){
        //QQ空间
        ShareSDKUtil.shareQZone(mShareTitle,mShareBody,mShareImgUrl,mShareUrl,this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ShareSDK.stopSDK(getActivity());
    }

    /******分享回调********/
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtil.showToast("分享成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtil.showToast("分享失败,错误码:"+i);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if(platform.getName().equals(SinaWeibo.NAME)){
            return;
        }
        ToastUtil.showToast("分享取消");
    }

}
