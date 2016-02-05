package com.rain.zhihu_example.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.rain.zhihu_example.R;


/**
 * 下拉加载更多的RecyclerView
 * 如果要添加头部  需要在设置完Adapter后setHeadEnable()
 *                                  setHeadView()
 * Created by Rain
 * on 2016/1/14 11:28
 */
@SuppressWarnings("unused")
public class LoadMoreRecyclerView extends RecyclerView {

    /**
     * 定义RecyclerView中的条目类型
     */
    public final static int TYPE_NORMAL = 1000; //普通RecyclerView
    public final static int TYPE_HEADER = 1001;//带头的RecyclerView 用于下拉刷新
    public final static int TYPE_FOOTER = 1002;//带脚布局的RecyclerView  用于加载更多

    public final static int LOAD_TYPE_NORMAL = 0x0001;//正常加载 显示加载更多
    public final static int LOAD_TYPE_FAIL = 0x0002;//失败加载 显示加载失败
    public final static int LOAD_TYPE_LESS_DATA = 0x0003;//没有更多数据 显示没有更多

    private boolean mIsFooterEnable = false;//是否允许加载更多

    private boolean mIsLoadingMore;//用于标识是否正在加载更多，防止重复加载

    private int mLoadMorePosition;//标记加载更多的position

    private LoadMoreListener mListener;//加载更多的监听

    private AutoLoadAdapter mAutoLoadAdapter;//自适配Adapter

    private boolean mIsShowLoadLessData = false;//是否显示没有更多字样

    private int mLoadType = LOAD_TYPE_NORMAL;

    private AutoLoadAdapter.FooterViewHolder footerViewHolder;


    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化-添加一个滚动监听
     *
     * 回调加载更多的方法
     * 1.有监听并且支持加载更多: null != mListener && mIsFooterEnable;
     * 正在上拉 dy>0,当前最后一条可见数据是总数据的最后一条 加载更多
     * 3. 当前不显示加载更多
     */
    private void init() {
        super.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    executeLoadMore();
                }else{
                    if(mLoadType != LOAD_TYPE_NORMAL){
                        setLoadDefault();
                    }
                }
            }
        });
    }

    /**
     *执行加载更多
     *
     * 1.有监听并且支持加载更多: null != mListener && mIsFooterEnable;
     * 2.当前最后一条可见数据是总数据的最后一条 加载更多
     * 3. 当前不显示 没有更多的字样
     */
    private void executeLoadMore() {
        if (null != mListener && mIsFooterEnable && !mIsLoadingMore && !mIsShowLoadLessData) {
            int lastVisiblePosition = getLastVisiblePosition();
            if (lastVisiblePosition + 1 == mAutoLoadAdapter.getItemCount()) {
                setLoadingMore(true);
                mLoadMorePosition = lastVisiblePosition;
                mListener.onLoadMore();
            }
        }
    }


    /**
     * 设置正在加载更多
     *
     * @param loadingMore 是否正在加载
     */
    public void setLoadingMore(boolean loadingMore) {
        this.mIsLoadingMore = loadingMore;

    }

    /**
     * 设置加载更多的监听
     *
     * @param listener 加载更多监听
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 设置是否显示加载更多
     */
    private void setShowLoadMore(boolean showLoadNoMore) {
        this.mIsShowLoadLessData = showLoadNoMore;
        if(mIsShowLoadLessData){
            mLoadType = LOAD_TYPE_NORMAL;
        }else{
            mLoadType = LOAD_TYPE_LESS_DATA;
        }
    }

    /**
     * 设置头布局
     *
     * @param view 头布局
     */
    public void setHeaderView(View view) {
        if(view != null){
            mAutoLoadAdapter.setHeaderView(view);
            setHeaderEnable(true);
        }
    }

    /**
     * 获取脚布局的高度
     * @return 脚布局高度
     */
    public int getFootViewHeight(Context context) {
        if(mIsFooterEnable){
            View view = View.inflate(context, R.layout.layout_recycler_loadmore_foot, null);
            view.measure(0,0);
            return view.getMeasuredHeight();
        }
        return 0;
    }

    /**
     * 设置脚布局加载更多 显示加载失败
     */
    public void setLoadFailure() {
        mIsLoadingMore = false;//设置不在加载
        mLoadType = LOAD_TYPE_FAIL;
        if(footerViewHolder != null){
            setLoadMoreView(footerViewHolder,mLoadType);
        }
    }

    /**
     * 设置脚布局加载更多 显示加载更多
     */
    public void setLoadDefault() {
        mLoadType = LOAD_TYPE_NORMAL;
        if(footerViewHolder != null){
            setLoadMoreView(footerViewHolder,mLoadType);
        }
    }



    /**
     * 加载更多的监听
     */
    public interface LoadMoreListener {
        //加载更多
        void onLoadMore();
    }

    /**
     * 自适应LoadAdapter
     */
    public class AutoLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private RecyclerView.Adapter mInternalAdapter;
        private int mHeadResId;
        private boolean mIsHeaderEnable;
        private View mHeaderView;
        private LayoutManager mLayoutManager;
        private HeaderViewHolder headerViewHolder = null;//头的holder如果有就不需要重新new


        public AutoLoadAdapter(Adapter internalAdapter) {
            this.mInternalAdapter = internalAdapter;
            this.mLayoutManager = getLayoutManager();
            mIsHeaderEnable = false;
            mHeaderView = null;
            setGridSpanCount(mLayoutManager);
        }

        @Override
        public int getItemViewType(int position) {
            int headerPosition = 0;
            int footerPosition = getItemCount() - 1;
            int type = mInternalAdapter.getItemViewType(position);
            //如果有头布局 条目大于0时 返回位置-1的Type
            if(mIsHeaderEnable && position>0){
                type = mInternalAdapter.getItemViewType(position-1);
            }
            if (headerPosition == position && mIsHeaderEnable && (mHeadResId > 0 | mHeaderView != null)) {
                type = TYPE_HEADER;
            }
            if (footerPosition == position && mIsFooterEnable) {
                type = TYPE_FOOTER;
            }
            return type;
        }

        /**
         * 设置在GridLayout中对于条目横跨处理
         * 1.如果存在头布局 并且位置为0  横跨一行
         * 2.如果存在尾布局 并且位置为最后一位  横跨一行
         * @param layoutManager 布局管理器
         */
        private void setGridSpanCount(final LayoutManager layoutManager) {//当时卡片布局 GridLayoutManager 继承LinearLayoutManager
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = (GridLayoutManager) layoutManager;
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int spanCount = gridManager.getSpanCount();
                        if(mIsHeaderEnable && position == 0){
                            return spanCount;
                        }
                        if(mIsFooterEnable && position == getItemCount()-1){
                            return spanCount;
                        }
                        return 1;
                    }
                });
            }
        }

        public void setHeaderView(View headerView) {
            mHeaderView = null;
            this.mHeaderView = headerView;
        }

        /**
         * 脚布局Holder
         */
        public class FooterViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_nomore;
            LinearLayout ll_no_more;
            LinearLayout ll_fail;
            public FooterViewHolder(View itemView) {
                super(itemView);
                this.ll_nomore = (LinearLayout) itemView.findViewById(R.id.ll_loading_more);
                this.ll_no_more = (LinearLayout) itemView.findViewById(R.id.ll_loading_no_more);
                this.ll_fail = (LinearLayout) itemView.findViewById(R.id.ll_loading_fail);
            }
        }

        /**
         * 头布局Holder
         */
        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER && mHeadResId > 0) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        mHeadResId, parent, false));
            }else if(viewType == TYPE_HEADER && mHeaderView != null){
                return new HeaderViewHolder(mHeaderView);
            }

            if (viewType == TYPE_FOOTER) {
                footerViewHolder = new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_recycler_loadmore_foot, parent, false));
                return footerViewHolder;
            } else { //TYPE_NORMAL
                return mInternalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type != TYPE_FOOTER && type != TYPE_HEADER) {
                //如果有头布局 传递给需要代理的Adapter中时候 需要位置-1
                if(mIsHeaderEnable){
                    position -= 1;
                }
                mInternalAdapter.onBindViewHolder(holder, position);
            }
            if(type == TYPE_FOOTER ){
                setLoadMoreView(holder,mLoadType);
            }
        }


        @Override
        public int getItemCount() {
            int count = mInternalAdapter.getItemCount();
            if (mIsFooterEnable) count++;
            if (mIsHeaderEnable) count++;
            return count;
        }

        public void setHeaderEnable(boolean enable) {
            mIsHeaderEnable = enable;
        }

        public void addHeaderView(int resId) {
            mHeadResId = resId;
        }
    }

    /**
     * 设置底部加载的样式
     */
    private void setLoadMoreView(ViewHolder holder, int mLoadType) {
        AutoLoadAdapter.FooterViewHolder footerViewHolder = (AutoLoadAdapter.FooterViewHolder) holder;
        if(mLoadType == LOAD_TYPE_NORMAL){
            footerViewHolder.ll_nomore.setVisibility(View.VISIBLE);
            footerViewHolder.ll_no_more.setVisibility(View.GONE);
            footerViewHolder.ll_fail.setVisibility(View.GONE);
        }else if(mLoadType == LOAD_TYPE_LESS_DATA){
            footerViewHolder.ll_nomore.setVisibility(View.GONE);
            footerViewHolder.ll_no_more.setVisibility(View.VISIBLE);
            footerViewHolder.ll_fail.setVisibility(View.GONE);
        }else if(mLoadType == LOAD_TYPE_FAIL){
            footerViewHolder.ll_nomore.setVisibility(View.GONE);
            footerViewHolder.ll_no_more.setVisibility(View.GONE);
            footerViewHolder.ll_fail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mAutoLoadAdapter = new AutoLoadAdapter(adapter);
        }
        super.swapAdapter(mAutoLoadAdapter, true);
    }

    /**
     * 切换页面布局
     * @param layoutManager 布局管理者
     */
    public void switchLayoutManager(LayoutManager layoutManager) {
        int firstVisiblePosition = getFirstVisiblePosition();
        setLayoutManager(layoutManager);
        getLayoutManager().scrollToPosition(firstVisiblePosition);
    }

    /**
     * 获取第一个可见条目
     * @return 第一个可见条目
     */
    public int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获取最后一条可见数据
     *
     * @return 数据位置
     */
    public int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions 位置集合
     * @return 返回最小的位置
     */
    private int getMinPositions(int[] positions) {
        int minPosition = Integer.MAX_VALUE;
        for (int position : positions) {
            minPosition = Math.min(minPosition, position);
        }
        return minPosition;
    }


    /**
     * 获取最大条目数
     *
     * @param positions 获取最大位置数组
     *
     * @return 最大的的某个位置
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    /**
     * 添加头部View
     * @param resId 资源ID
     */
    public void addHeaderView(int resId) {
        mAutoLoadAdapter.addHeaderView(resId);
    }

    /**
     * 设置头部View是否展示
     * @param enable true 显示  false 不显示
     */
    public void setHeaderEnable(boolean enable){
        mAutoLoadAdapter.setHeaderEnable(enable);
    }

    /**
     * 设置是否支持自动加载更多
     * @param autoLoadMore true 加载更多 false 不加载
     */
    public void setAutoLoadMoreEnable(boolean autoLoadMore) {
        mIsFooterEnable = autoLoadMore;
    }

    /**
     * 通知更多的数据已经加载
     *
     * 每次加载完成后添加了data数据 用该方法刷新列表
     * 不用notifyDataSetChange来刷新
     * @param hasMore 是否还有更多了  没有的话不显示上拉加载框
     */
    public void notifyLoadMoreFinish(boolean hasMore) {
        setAutoLoadMoreEnable(hasMore);
        getAdapter().notifyItemRemoved(mLoadMorePosition);
        mIsLoadingMore = false;
        //继续执行加载更多 用于在网格布局中底部一行没有排满 继续加载更多
        executeLoadMore();
    }


    /**
     * 通知更多的数据已经加载 并在最后布局中提示没有更多
     *
     * @param hasMore 是否还有更多了  没有的话脚布局显示没有更多了
     */
    public void notifyWithLessMoreData(boolean hasMore) {
        setAutoLoadMoreEnable(true);
        if(!hasMore)
            setShowLoadMore(true);
        getAdapter().notifyItemRemoved(mLoadMorePosition);

        mIsLoadingMore = false;
        //继续执行加载更多
        executeLoadMore();
    }

    @Override
    public Adapter getAdapter() {
        if(mAutoLoadAdapter != null){
            return mAutoLoadAdapter;
        }
        return super.getAdapter();
    }
}
