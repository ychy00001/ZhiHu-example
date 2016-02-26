package com.rain.zhihu_example.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.mode.bean.SubscribeBean;
import com.rain.zhihu_example.util.ViewUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 订阅列表适配器
 * Created by yangchunyu
 * 2016/1/26 11:22
 */
public class SubscribeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_TITLE_ITEM = 0;
    public static final int TYPE_EDITOR_ITEM = 1;
    public static final int TYPE_NORMAL_ITEM = 2;

    private SubscribeBean mSubscribe;
    private List<SubscribeBean.EditorsEntity> mEditors;//编辑
    private List<SubscribeBean.StoriesEntity> mStories;//故事

    public SubscribeAdapter(SubscribeBean data) {
        this.mSubscribe = data;
        this.mEditors = data.getEditors();
        this.mStories = data.getStories();
    }

    /**
     * 加载更多数据
     */
    public void loadMoreData(SubscribeBean moreData){
        this.mStories.addAll(moreData.getStories());
    }

    /**
     * 更新数据
     */
    public void update(SubscribeBean data){
        this.mSubscribe = data;
        this.mEditors = data.getEditors();
        this.mStories = data.getStories();
    }

    @Override
    public int getItemViewType(int position) {
        //第一条头标题
        if (position == 0) {
            return TYPE_TITLE_ITEM;
        } else if (position == 1) {//第二条编辑列表
            return TYPE_EDITOR_ITEM;
        } else {//正常条目
            return TYPE_NORMAL_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL_ITEM) {//正常条目
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_home_base_list, null
            );
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(parent.getContext(), 100)
            );
            layoutParams.topMargin = ViewUtil.dp2px(parent.getContext(), 10);
            layoutParams.leftMargin = ViewUtil.dp2px(parent.getContext(), 10);
            layoutParams.rightMargin = ViewUtil.dp2px(parent.getContext(), 7);
            view.setLayoutParams(layoutParams);
            return new NormalItemViewHolder(view);
        } else if (viewType == TYPE_TITLE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_subscribe_title, null
            );
            return new TitleItemViewHolder(view);
        } else if (viewType == TYPE_EDITOR_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_subscirbe_editor, null
            );
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(parent.getContext(), 60)
            );
            view.setLayoutParams(layoutParams);
            return new EditorViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int itemType = getItemViewType(position);

        if (itemType == TYPE_NORMAL_ITEM && holder instanceof NormalItemViewHolder) {
            final int index = position - 2;
            SubscribeBean.StoriesEntity storiesEntity = mStories.get(index);
            //这里获取条目个数时，需要去掉已经添加的头
            final NormalItemViewHolder normalItemViewHolder = (NormalItemViewHolder) holder;
            normalItemViewHolder.textView.setText(storiesEntity.getTitle());
            normalItemViewHolder.imageView.setVisibility(View.GONE);
            if(null != storiesEntity.getImages() && storiesEntity.getImages().size()>0){
                normalItemViewHolder.imageView.setVisibility(View.VISIBLE);
                Picasso.with(normalItemViewHolder.imageView.getContext())
                        .load(storiesEntity.getImages().get(0))
                        .into(normalItemViewHolder.imageView);
            }

        } else if (itemType == TYPE_TITLE_ITEM && holder instanceof TitleItemViewHolder) {
            //头部holder
            final TitleItemViewHolder titleHolder = (TitleItemViewHolder) holder;
            titleHolder.textView.setText(mSubscribe.getDescription());
            Picasso.with(titleHolder.imageView.getContext())
                    .load(mSubscribe.getBackground())
                    .into(titleHolder.imageView);
        } else if (itemType == TYPE_EDITOR_ITEM && holder instanceof EditorViewHolder) {
            //编辑Holder
            final EditorViewHolder editorHolder = (EditorViewHolder) holder;
            editorHolder.mImgLayout.removeAllViews();
            for (int i = 0; i < mEditors.size(); i++) {
                ImageView imgView = new ImageView(editorHolder.mImgLayout.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
                params.leftMargin = ViewUtil.dp2px(editorHolder.mImgLayout.getContext(),9);
                imgView.setLayoutParams(params);
                Picasso.with(editorHolder.mImgLayout.getContext())
                        .load(mEditors.get(i).getAvatar())
                        .into(imgView);
                editorHolder.mImgLayout.addView(imgView);
            }
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
        //长度增加头的一位
        return mStories.size() + 2;
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
     * 头部图片Holder
     */
    public class TitleItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public TitleItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.img_title);
        }
    }

    /**
     * 编辑人员holder
     */
    public class EditorViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mImgLayout;

        public EditorViewHolder(View itemView) {
            super(itemView);
            mImgLayout = (LinearLayout) itemView.findViewById(R.id.ll_img_container);
        }
    }

    /************************ 点击处理 ***********************/

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //设置监听
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
