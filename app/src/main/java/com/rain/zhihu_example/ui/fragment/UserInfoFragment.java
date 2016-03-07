package com.rain.zhihu_example.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.ui.activity.MainActivity;
import com.rain.zhihu_example.ui.base.BaseFragment;
import com.rain.zhihu_example.util.LoginUtil;
import com.squareup.picasso.Picasso;

/**
 * 用户信息页Fragment
 * @author yangchunyu
 *         2016/3/7
 *         16:02
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "UserInfoFragment";
    private String mIco;
    private String mNickName;

    @Bind(R.id.img_ico) ImageView mImgIco;
    @Bind(R.id.tv_nickName) TextView mTVName;
    @Bind(R.id.btn_login_out) Button mBtnLoginOut;

    public static UserInfoFragment newInstance(Bundle bundle) {
        UserInfoFragment mFragment = new UserInfoFragment();
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    protected View getSuccessView() {
        return View.inflate(mContext, R.layout.fragment_user_info, null);
    }

    @Override
    protected void requestData() {
        mContentPage.notifyDataChange(new Object());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIco = LoginUtil.getInstance(getContext()).getIco();
        mNickName = LoginUtil.getInstance(getContext()).getUserName();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.with(mContext).load(mIco).into(mImgIco);
        mTVName.setText(mNickName);
        mBtnLoginOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_out:
                LoginUtil.getInstance(mContext).clearInfo();
                ((MainActivity)getActivity()).loginOut();
                break;
        }
    }
}
