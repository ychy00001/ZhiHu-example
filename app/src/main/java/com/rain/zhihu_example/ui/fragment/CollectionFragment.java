package com.rain.zhihu_example.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.ui.activity.ContentDetailActivity;
import com.rain.zhihu_example.ui.activity.MainActivity;
import com.rain.zhihu_example.ui.adapter.CollectionAdapter;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.ui.base.BaseAdapter;
import com.rain.zhihu_example.ui.base.BaseFragment;
import com.rain.zhihu_example.util.GreenDaoUtil;
import com.rain.zhihu_example.util.LoginUtil;
import greendao.bean.Collection;
import greendao.bean.User;
import greendao.dao.DaoSession;
import greendao.dao.UserDao;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
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

    @Bind(R.id.recycle_view) RecyclerView mRecyclerView;
    @Bind(R.id.tv_no_collection) TextView mTVNoCollection;
    private GreenDaoUtil mDaoUtil;
    private DaoSession mDaoSession;
    private CollectionAdapter mAdapter;

    private String mOperator;//标识操作类型
    private String mOperatorId;//标识操作StoryId
    private String mAddTitle;
    private String mAddImg;
    private List<Collection> mCollectionLists;

    public static CollectionFragment newInstance(Bundle args) {
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mDaoUtil = GreenDaoUtil.getInstance(getContext(),GreenDaoUtil.DB_COLLECTION_NAME);
        mDaoSession = mDaoUtil.getDaoSesstion();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setToolbarText("我的收藏");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(mOperator) &&
                !TextUtils.isEmpty(mOperatorId) &&
                mAdapter != null
                && mCollectionLists != null){
            if(mOperator.equals(CollectionEvent.OPERATE_DELETE)
                    && TextUtils.isEmpty(mAddTitle)){
                deleteListById(mOperatorId);
            }
            setCollectionAdapter(mCollectionLists);
        }
    }

    /**
     * 添加条目至收藏列表
     */
    private void addListById(String mOperatorId, String mAddTitle, String mAddImg) {
        Collection collection = new Collection(null,mOperatorId,mAddImg,mAddTitle,null,null);
        mCollectionLists.add(0,collection);
    }

    /**
     * 依靠ID删除所有列表
     * @param operatorId 操作Story的ID
     */
    private void deleteListById(String operatorId) {
        for(int i=0;i<mCollectionLists.size();i++){
            if(mCollectionLists.get(i).getStoryId().equals(operatorId)){
                mCollectionLists.remove(i);
                return;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mOperator = "";
        mOperatorId = "";
        mAddImg = "";
        mAddTitle = "";
    }

    @Override
    protected View getSuccessView() {
        return View.inflate(mContext, R.layout.fragment_collection, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void requestData() {
        //读取本地数据库数据  注意读取列表时候读取的是缓存
        String uid = LoginUtil.getInstance(getContext()).getUid();
        UserDao userDao = mDaoSession.getUserDao();
        User user = mDaoUtil.QueryBean(userDao, UserDao.Properties.UserId.eq(uid));
        userDao.refresh(user);
        if(null == user){
            setCollectionAdapter(null);
        }else{
            setCollectionAdapter(user.getCollections());
        }
    }

    private void setCollectionAdapter(List<Collection> collections) {
        mContentPage.showSuccessPage();
        mCollectionLists = collections;
        //倒序集合  最新收藏放置第一位
        Collections.reverse(mCollectionLists);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mCollectionLists == null || mCollectionLists.size() < 1){
                    //提示没有收藏
                    mRecyclerView.setVisibility(View.GONE);
                    mTVNoCollection.setVisibility(View.VISIBLE);
                }else{
                    //显示收藏
                    if(mAdapter == null){
                        mAdapter  = new CollectionAdapter(mCollectionLists);
                        mAdapter.setOnItemClickListener(new MyItemClick(mCollectionLists));
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        mAdapter.update(mCollectionLists);
                    }
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mTVNoCollection.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 条目点击
     */
    private class MyItemClick implements BaseAdapter.OnItemClickListener {

        private List<Collection> mLists;

        public MyItemClick(List<Collection> mLists) {
            this.mLists = mLists;
        }

        @Override
        public void onItemClick(View view, int position) {
            //打开详情页面
            //跳转页面详情Activity
            Intent intent = new Intent(getActivity(), ContentDetailActivity.class);
            intent.putExtra(Constances.STORY_ID,mLists.get(position).getStoryId());
            intent.putExtra(Constances.STORY_TITLE,mLists.get(position).getTitle());
            intent.putExtra(Constances.STORY_TYPE,mLists.get(position).getType());
            String img = mLists.get(position).getImage();
            if(null != img){
                intent.putExtra(Constances.STORY_IMG,img);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, BaseActivity.TRANS_TYPE_TRANSLATE);
        }
    }


    @Subscribe
    public void onEvent(CollectionEvent event){
        mOperatorId = event.getStoryId();
        mOperator = event.getOperate();
        mAddTitle = event.getAddTitle();
        mAddImg = event.getAddImg();
    }

    /**
     * 收藏的Event事件 用于接收收藏页面发送的消息 是否取消/收藏某项
     */
    public static class CollectionEvent{
        public static final String OPERATE_ADD = "add";//添加
        public static final String OPERATE_DELETE = "delete";//删除

        private String operate;
        private String storyId;

        private String addTitle;
        private String addImg;

        public CollectionEvent(String operate,String id,String addTitle,String addImg) {
            this.operate = operate;
            this.storyId = id;
            this.addTitle = addTitle;
            this.addImg = addImg;
        }

        public String getOperate() {
            return operate;
        }

        public String getStoryId() {
            return storyId;
        }

        public String getAddTitle() {
            return addTitle;
        }

        public String getAddImg() {
            return addImg;
        }
    }
}
