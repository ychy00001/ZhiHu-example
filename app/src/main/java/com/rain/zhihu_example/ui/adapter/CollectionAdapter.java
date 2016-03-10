package com.rain.zhihu_example.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.ui.base.BaseAdapter;
import com.rain.zhihu_example.util.ViewUtil;
import com.squareup.picasso.Picasso;
import greendao.bean.Collection;

import java.util.List;

/**
 * 收藏页面的适配器
 * @author yangchunyu
 *         2016/3/10
 *         10:12
 */
public class CollectionAdapter extends BaseAdapter {

    private List<Collection> mCollections;

    public CollectionAdapter(List<Collection> mCollections) {
        this.mCollections = mCollections;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_home_base_list, null
        );
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(parent.getContext(),100)
        );
        layoutParams.topMargin = ViewUtil.dp2px(parent.getContext(),10);
        layoutParams.leftMargin = ViewUtil.dp2px(parent.getContext(),10);
        layoutParams.rightMargin = ViewUtil.dp2px(parent.getContext(),7);
        view.setLayoutParams(layoutParams);
        return new NormalItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Collection collection = mCollections.get(position);
        NormalItemViewHolder normalItemViewHolder = (NormalItemViewHolder) holder;
        normalItemViewHolder.textView.setText(collection.getTitle());
        normalItemViewHolder.imageView.setVisibility(View.GONE);
        if(null != collection.getImage()){
            normalItemViewHolder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(normalItemViewHolder.imageView.getContext())
                    .load(collection.getImage())
                    .into(normalItemViewHolder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCollections.size();
    }

    public void update(List<Collection> collections) {
        this.mCollections = collections;
        this.notifyDataSetChanged();
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
}
