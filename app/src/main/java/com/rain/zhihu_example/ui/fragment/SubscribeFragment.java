package com.rain.zhihu_example.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.mode.bean.SubscribeBean;
import com.rain.zhihu_example.present.SubscribePresent;
import com.rain.zhihu_example.ui.activity.ContentDetailActivity;
import com.rain.zhihu_example.ui.activity.MainActivity;
import com.rain.zhihu_example.ui.adapter.SubscribeAdapter;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.ui.base.BaseFragment;
import com.rain.zhihu_example.ui.view.SubscribeView;
import com.rain.zhihu_example.widget.LoadMoreRecyclerView;

/**
 * 订阅页面展示
 * Created by yangchunyu
 * 2016/1/25 16:43
 */
public class SubscribeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.LoadMoreListener, SubscribeView {

    public static final String TAG = "SubscribeFragment";

    private String subscribeName;
    private String subscribeId;
    private SubscribePresent mPresent;
    private SubscribeAdapter mAdapter;

    private LoadMoreRecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private MyItemClick mClickCallBack;//Adapter条目点击事件

    public static SubscribeFragment newInstance(Bundle args) {
        SubscribeFragment fragment = new SubscribeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        subscribeName = getArguments().getString(Constances.NAME_SUBSCRIBE);
        subscribeId = getArguments().getString(Constances.ID_SUBSCRIBE);
        mPresent = new SubscribePresent(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setToolbarText(subscribeName);
        //设置刷新时动画的颜色，可以设置4个
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        mRecyclerView.setAutoLoadMoreEnable(true);
        mRecyclerView.setLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected View getSuccessView() {
        final View view = View.inflate(mContext, R.layout.fragment_main, null);
        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recycle_view);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        return view;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mPresent.requestData(subscribeId,false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        mPresent.loadMore(subscribeId);
    }


    /**
     * 执行数据请求
     */
    @Override
    protected void requestData()  {
        mPresent.requestData(subscribeId,true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 填充列表数据
     * @param bean 数据
     */
    @Override
    public void setListData(SubscribeBean bean) {
        if(mAdapter == null){
            mAdapter = new SubscribeAdapter(bean);
            mClickCallBack = new MyItemClick(bean);
            mAdapter.setOnItemClickListener(mClickCallBack);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.update(bean);
            mRecyclerView.notifyLoadMoreFinish(true);
        }

        mContentPage.notifyDataChange(bean);
    }

    @Override
    public void loadDataComplete() {
        if(mRefreshLayout.isRefreshing()){
            mRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 加载更多
     */
    @Override
    public void notifyLoadMoreData(SubscribeBean moreData) {
        if(moreData.getStories().size() == 0){
            mRecyclerView.notifyLoadMoreFinish(false);
            return;
        }
        mAdapter.loadMoreData(moreData);
        mRecyclerView.notifyLoadMoreFinish(true);
    }

    /**
     * 点击事件
     */
    private class MyItemClick implements SubscribeAdapter.OnItemClickListener{
        SubscribeBean mData;

        public MyItemClick(SubscribeBean mData) {
            this.mData = mData;
        }

        @Override
        public void onItemClick(View view, int position) {
            int storePosition = position-2;
            //跳转页面详情Activity
            Intent intent = new Intent(getActivity(), ContentDetailActivity.class);
            intent.putExtra(Constances.ID_STORY,mData.getStories().get(storePosition).getId()+"");
            intent.putExtra(Constances.IS_STORY_IMG,false);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, BaseActivity.TRANS_TYPE_TRANSLATE);
        }
    }
}
