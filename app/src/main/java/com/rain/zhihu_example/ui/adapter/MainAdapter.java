package com.rain.zhihu_example.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.RainApplication;
import com.rain.zhihu_example.mode.HomeMode;
import com.rain.zhihu_example.ui.base.BaseAdapter;
import com.rain.zhihu_example.util.DateUtil;
import com.rain.zhihu_example.util.ViewUtil;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 首页列表适配器
 * Created by yangchunyu
 * 2016/1/26 11:22
 */
public class MainAdapter extends BaseAdapter {

    public static final int TYPE_NORMAL_ITEM = 0;
    public static final int TYPE_DATA_ITEM = 1;

    private List<HomeMode.StoriesEntity> stories;
    private Map<Integer,String> titleDate;//日期头的标志 key 位置 value 日期

    public MainAdapter(HomeMode data) {
        stories = data.getStories();
        titleDate = new HashMap<>();
        titleDate.put(0, data.getDate());
    }

    public void loadMore(HomeMode data) {
        titleDate.put(stories.size() + titleDate.size(),data.getDate());//当前长度加上日期长度
        stories.addAll(data.getStories());//将数据填充至条目后面
    }

    @Override
    public int getItemViewType(int position) {
        //在此 如果日期头标志位 存储又当前位置 则显示日期头
        if (titleDate.containsKey(position)) {
            return TYPE_DATA_ITEM;
        } else {
            return TYPE_NORMAL_ITEM;
        }
    }

    public Map<Integer, String> getTitleDate() {
        return titleDate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATA_ITEM) {//带日期Holder
            //为条目设置LayoutParams 要不然xml中条目的宽高无法生效
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_home_date_title, null
            );
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(parent.getContext(),30)
            );
            view.setLayoutParams(layoutParams);
            return new GroupItemViewHolder(view);
        } else if (viewType == TYPE_NORMAL_ITEM) {//正常条目
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_home_base_list, null
            );
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,ViewUtil.dp2px(parent.getContext(),100)
            );
            layoutParams.topMargin = ViewUtil.dp2px(parent.getContext(),10);
            layoutParams.leftMargin = ViewUtil.dp2px(parent.getContext(),10);
            layoutParams.rightMargin = ViewUtil.dp2px(parent.getContext(),7);
            view.setLayoutParams(layoutParams);
            return new NormalItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemType = getItemViewType(position);
        //需要计算出当前位置前面有几个头标记 循环看保存的title位置是否比当前位置小
        int titlenum = 0;
        Set<Integer> titlePositions = titleDate.keySet();
        for (Integer titlePosition : titlePositions) {
            if (titlePosition < position) {
                titlenum++;
            }
        }

        if (itemType == TYPE_DATA_ITEM && holder instanceof GroupItemViewHolder) {
            GroupItemViewHolder groupItemViewHolder = (GroupItemViewHolder) holder;
            groupItemViewHolder.dateTextView.setText(DateUtil.formatDateByString(
                    titleDate.containsKey(position)?titleDate.get(position):""
            ));
        } else if (itemType == TYPE_NORMAL_ITEM && holder instanceof NormalItemViewHolder) {
            //这里获取条目个数时，需要去掉已经添加的头
            HomeMode.StoriesEntity storiesEntity = stories.get(position - titlenum);
            final NormalItemViewHolder normalItemViewHolder = (NormalItemViewHolder) holder;
            final int fixPosition = position - titlenum;
            normalItemViewHolder.textView.setText(storiesEntity.getTitle());
            Picasso.with(RainApplication.getContext())
                    .load(storiesEntity.getImages().get(0))
                    .into(normalItemViewHolder.imageView);
            normalItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(normalItemViewHolder.itemView,fixPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //长度增加头的一位
        return stories.size() + titleDate.size();//条目长度+日期头个数
    }

    /**
     * 正常条目
     */
    public class NormalItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public NormalItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_title);
            imageView = (ImageView) itemView.findViewById(R.id.item_icon);
        }
    }

    /**
     * 带日期Holder
     */
    public class GroupItemViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.base_swipe_group_item_time);
        }
    }


}
