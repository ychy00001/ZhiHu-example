package com.rain.zhihu_example.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.ui.activity.MainActivity;
import com.rain.zhihu_example.ui.adapter.CollectionAdapter;
import com.rain.zhihu_example.ui.base.BaseFragment;
import com.rain.zhihu_example.util.GreenDaoUtil;
import com.rain.zhihu_example.util.LoginUtil;
import com.rain.zhihu_example.widget.LoadMoreRecyclerView;
import greendao.bean.Collection;
import greendao.bean.User;
import greendao.dao.DaoSession;
import greendao.dao.UserDao;

import java.util.List;

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

    @Bind(R.id.recycle_view) LoadMoreRecyclerView mRecyclerView;
    @Bind(R.id.tv_no_collection) TextView mTVNoCollection;
    private GreenDaoUtil mDaoUtil;
    private DaoSession mDaoSession;
    private CollectionAdapter mAdapter;

    public static CollectionFragment newInstance(Bundle args) {
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDaoUtil = GreenDaoUtil.getInstance(getContext(),GreenDaoUtil.DB_COLLECTION_NAME);
        mDaoSession = mDaoUtil.getDaoSesstion();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setToolbarText("我的收藏");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setVisibility(View.GONE);
        mTVNoCollection.setVisibility(View.VISIBLE);
    }

    @Override
    protected View getSuccessView() {
        return View.inflate(mContext, R.layout.fragment_collection, null);
    }

    @Override
    protected void requestData() {
        //读取本地数据库数据
        String uid = LoginUtil.getInstance(getContext()).getUid();
        UserDao userDao = mDaoSession.getUserDao();
        User user = mDaoUtil.QueryBean(userDao, UserDao.Properties.UserId.eq(uid));
        setCollectionAdapter(user.getCollections());
    }

    private void setCollectionAdapter(final List<Collection> collections) {
        mContentPage.showSuccessPage();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(collections == null || collections.size() < 1){
                    //提示没有收藏
                    mRecyclerView.setVisibility(View.GONE);
                    mTVNoCollection.setVisibility(View.VISIBLE);
                }else{
                    //显示收藏
                    if(mAdapter == null){
                        mAdapter  = new CollectionAdapter(collections);
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        mAdapter.notifyDataSetChanged();
                    }
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mTVNoCollection.setVisibility(View.GONE);
                }
            }
        });
    }

}
