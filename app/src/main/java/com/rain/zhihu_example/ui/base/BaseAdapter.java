package com.rain.zhihu_example.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author yangchunyu
 *         2016/3/10
 *         10:20
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);
    @Override
    public abstract int getItemCount();

    /************************点击处理***********************/

    protected OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //设置监听
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
