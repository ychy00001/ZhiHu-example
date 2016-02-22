package com.rain.zhihu_example.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.mode.bean.SubscribeBean;
import com.rain.zhihu_example.util.ViewUtil;

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

    public SubscribeAdapter(SubscribeBean data) {
        this.mSubscribe = data;
    }

    @Override
    public int getItemViewType(int position) {
       if(position == 0){
            return TYPE_TITLE_ITEM;
       }else if(position == 1){
           return TYPE_EDITOR_ITEM;
       }else{
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

      if (itemType == TYPE_NORMAL_ITEM && holder instanceof NormalItemViewHolder) {
//            //这里获取条目个数时，需要去掉已经添加的头
//            final NormalItemViewHolder normalItemViewHolder = (NormalItemViewHolder) holder;
//            normalItemViewHolder.textView.setText();
//            Picasso.with(RainApplication.getContext())
//                    .load()
//                    .into(normalItemViewHolder.imageView);
//            normalItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(normalItemViewHolder.itemView,fixPosition);
//                    }
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        //长度增加头的一位
        return mSubscribe.getStories().size()+2;
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
    public class TitleItemViewHolder extends RecyclerView.ViewHolder{
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
    public class EditorViewHolder extends RecyclerView.ViewHolder{

        public EditorViewHolder(View itemView) {
            super(itemView);
        }
    }

    /************************点击处理***********************/

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //设置监听
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
