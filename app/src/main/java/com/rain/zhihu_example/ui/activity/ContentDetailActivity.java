package com.rain.zhihu_example.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import butterknife.Bind;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.ui.fragment.ContentDetailFragment;
import com.rain.zhihu_example.util.BuildConfigUtil;

/**
 * Created by yangchunyu
 * 2016/2/4 10:19
 */
public class ContentDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ContentDetailActivity";
    private ContentDetailFragment mFragment;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_content_detent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        toolBarSetting();
        initFragment();
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
            Log.d(TAG,getIntent().getExtras().getString(Constances.ID_STORY)+"");
        }
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
        return true;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
