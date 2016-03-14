package com.rain.zhihu_example.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.ui.base.BaseShareActivity;
import com.rain.zhihu_example.ui.fragment.CollectionFragment;
import com.rain.zhihu_example.ui.fragment.ContentDetailFragment;
import com.rain.zhihu_example.util.BuildConfigUtil;
import com.rain.zhihu_example.util.GreenDaoUtil;
import com.rain.zhihu_example.util.LoginUtil;
import greendao.bean.Collection;
import greendao.bean.User;
import greendao.dao.CollectionDao;
import greendao.dao.DaoSession;
import greendao.dao.UserDao;
import org.greenrobot.eventbus.EventBus;

/**
 * 详情页面
 * Created by yangchunyu
 * 2016/2/4 10:19
 */
public class ContentDetailActivity extends BaseShareActivity implements View.OnClickListener {

    private static final String TAG = "ContentDetailActivity";

    private ContentDetailFragment mFragment;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private DaoSession mDaoSession;

    private String mStoryId;
    private String mStoryTitle;
    private String mStoryImg;
    private int mStoryType;
    private GreenDaoUtil mGreenDaoUtil;//数据库工具类
    private CollectionDao collectionDao;
    private UserDao userDao;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_content_detent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        initDBManager();
        toolBarSetting();
        initFragment();
    }

    //初始化数据库所需参数
    private void initDBManager() {
        mGreenDaoUtil = GreenDaoUtil.getInstance(this, GreenDaoUtil.DB_COLLECTION_NAME);
        mDaoSession = mGreenDaoUtil.getDaoSesstion();
        collectionDao = mDaoSession.getCollectionDao();
        userDao = mDaoSession.getUserDao();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constances.CODE_REQUEST_CONTENT_DETAIL
                && resultCode == Constances.CODE_RESULT_LOGIN_FINISH
                && data != null){
            //发送消息给MainActivity 用户登录
            String nickName = data.getStringExtra(LoginActivity.RESULT_NICKNAME);
            String ico = data.getStringExtra(LoginActivity.RESULT_ICON);
            EventBus.getDefault().post(new MainActivity.LoginEvent(nickName,ico));
        }
    }

    /**
     * 关于toolbar的设置
     */
    private void toolBarSetting() {
        //设置左上角的返回按钮
        mToolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.setTitle("");
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        if(BuildConfigUtil.DEBUG){
            Log.d(TAG,getIntent().getStringExtra(Constances.STORY_ID)+"");
        }
        mStoryId = getIntent().getStringExtra(Constances.STORY_ID);
        mStoryImg = getIntent().getStringExtra(Constances.STORY_IMG);
        mStoryTitle = getIntent().getStringExtra(Constances.STORY_TITLE);
        mStoryType = getIntent().getIntExtra(Constances.STORY_TYPE,-1);

        mFragment = ContentDetailFragment.newInstance(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mFragment, ContentDetailFragment.TAG)
                .commit();
    }

    /**
     * 获取Toolbar
     */
    public Toolbar getToolbar(){
        if (mToolbar != null) {
            return mToolbar;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_detail, menu);
        MenuItem item = menu.findItem(R.id.action_collection);
        //设置收藏点击效果
        item.setIcon(R.mipmap.action_collection_false);
        if(LoginUtil.getInstance(this).checkLogin()){
            String uid = LoginUtil.getInstance(this).getUid();
            Collection collection = mGreenDaoUtil.QueryBean(collectionDao, CollectionDao.Properties.UserId.eq(uid), CollectionDao.Properties.StoryId.eq(mStoryId));
            if(collection != null){
                item.setIcon(R.mipmap.action_collection_true);
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                //分享
                break;
            case R.id.action_collection:
                //收藏
                if(LoginUtil.getInstance(this).checkLogin()){
                    String uid = LoginUtil.getInstance(this).getUid();
                    //此处一登录应该就保存改用户的数据至数据库 若没有该用户数据 则需要添加一个数据 如果有数据 只需要更新数据
                    if(mGreenDaoUtil.QueryBean(userDao, UserDao.Properties.UserId.eq(uid)) == null){
                        //无用户 无收藏  添加用户以及收藏
                        addUserToDB(uid, userDao);
                        addCollectionToDB(uid, collectionDao);
                        item.setIcon(R.mipmap.action_collection_true);
                    }else{
                        //有用户  查询收藏 看有没有关于用户的收藏
                        Collection collection = mGreenDaoUtil.QueryBean(collectionDao, CollectionDao.Properties.UserId.eq(uid), CollectionDao.Properties.StoryId.eq(mStoryId));
                        if(collection == null){
                            //没有收藏  添加收藏
                            addCollectionToDB(uid, collectionDao);
                            item.setIcon(R.mipmap.action_collection_true);
                            EventBus.getDefault().post(new CollectionFragment.CollectionEvent(CollectionFragment.CollectionEvent.OPERATE_ADD
                            ,mStoryId,mStoryTitle,mStoryImg));
                        }else{
                            //有收藏  删除收藏
                            collection.delete();
                            item.setIcon(R.mipmap.action_collection_false);
                            EventBus.getDefault().post(new CollectionFragment.CollectionEvent(CollectionFragment.CollectionEvent.OPERATE_DELETE
                                    ,mStoryId,null,null));
                        }
                    }
                }else{
                    startLoginActivity(Constances.CODE_REQUEST_CONTENT_DETAIL);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加收藏至数据库
     */
    private void addCollectionToDB(String uid, CollectionDao collectionDao) {
        Collection insert = new Collection();
        insert.setImage(mStoryImg);
        insert.setStoryId(mStoryId);
        insert.setTitle(mStoryTitle);
        insert.setType(mStoryType);
        insert.setUserId(uid);
        collectionDao.insert(insert);
    }

    /**
     * 添加User至数据库
     */
    private void addUserToDB(String uid, UserDao userDao) {
        User userEntity = new User(uid);
        userDao.insert(userEntity);
    }
}
