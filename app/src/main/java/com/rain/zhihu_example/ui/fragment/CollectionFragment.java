package com.rain.zhihu_example.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.ui.activity.MainActivity;
import com.rain.zhihu_example.ui.base.BaseFragment;
import com.rain.zhihu_example.widget.LoadMoreRecyclerView;

/**
 * 侧滑菜单-收藏
 *
 * 点击收藏应该将收藏的数据添加到数据库
 * 本页面读取数据库的数据来获取哪些收藏
 * 数据库中可以存储用户信息，只读用户的收藏，如果没有用户则显示没有用户时候的收藏
 * 先做登录吧。。。。
 * @author yangchunyu
 *         2016/3/2
 *         10:20
 */
public class CollectionFragment extends BaseFragment {

    public static final String TAG = "CollectionFragment";

    private LoadMoreRecyclerView mRecyclerView;

    public static CollectionFragment newInstance(Bundle args) {
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setToolbarText("我的收藏");
    }

    @Override
    protected View getSuccessView() {
        return View.inflate(mContext, R.layout.fragment_collection, null);
    }

    @Override
    protected void requestData() {
        //请求数据
    }


}
