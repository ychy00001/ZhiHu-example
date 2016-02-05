package com.rain.zhihu_example.ui.activity;

import android.os.Bundle;
import android.util.Log;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.ui.fragment.ContentDetailFragment;
import com.rain.zhihu_example.util.BuildConfigUtil;

/**
 * Created by yangchunyu
 * 2016/2/4 10:19
 */
public class ContentDetailActivity extends BaseActivity{

    private static final String TAG = "ContentDetailActivity";
    private ContentDetailFragment mFragment;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_content_detent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
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


}
