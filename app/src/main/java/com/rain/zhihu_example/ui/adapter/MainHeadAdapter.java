package com.rain.zhihu_example.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.global.RainApplication;
import com.rain.zhihu_example.mode.HomeMode;
import com.rain.zhihu_example.ui.activity.ContentDetailActivity;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.util.CommonUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 轮播图适配器
 * Created by yangchunyu
 * 2016/1/27 10:14
 */
public class MainHeadAdapter extends PagerAdapter {

    private List<HomeMode.TopStoriesEntity> mData;
    private List<View> mViews;
    private Activity mActivity;

    public MainHeadAdapter(List<HomeMode.TopStoriesEntity> data,Activity activity) {
        this. mActivity = activity;
        update(data);
    }

    /**
     * 更新
     */
    public void update(List<HomeMode.TopStoriesEntity> data) {
        this.mData = data;
        mViews = null;
        mViews = new ArrayList<>();
        for(int i =0; i<mData.size();i++){
            View view = View.inflate(RainApplication.getContext(), R.layout.item_home_head, null);
            mViews.add(view);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //此处移除了该条目的父View 在添加 解决了在RecyclerView加载轮播图条目复用的问题
        CommonUtil.removeChildFrameParent(mViews.get(position % mViews.size()));
        View view = mViews.get(position % mViews.size());
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_focus_img);
        TextView textView = (TextView) view.findViewById(R.id.tv_focus_text);
        final HomeMode.TopStoriesEntity mEntity = mData.get(position % mViews.size());
        if(mEntity != null){
            textView.setText(mEntity.getTitle());
            Picasso.with(RainApplication.getContext())
                    .load(mEntity.getImage())
                    .into(imageView);
        }
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面详情Activity
                if(mEntity == null){
                    return;
                }
                Intent intent = new Intent(mActivity, ContentDetailActivity.class);
                intent.putExtra(Constances.STORY_ID,mEntity.getId()+"");
                intent.putExtra(Constances.STORY_TITLE,mEntity.getTitle()+"");
                intent.putExtra(Constances.STORY_IMG,mEntity.getImage()+"");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((BaseActivity)mActivity).startActivity(intent, BaseActivity.TRANS_TYPE_TRANSLATE);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position % mViews.size()));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
