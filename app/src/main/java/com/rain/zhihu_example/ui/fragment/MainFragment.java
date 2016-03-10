package com.rain.zhihu_example.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.api.Apis;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.mode.HomeMode;
import com.rain.zhihu_example.ui.activity.ContentDetailActivity;
import com.rain.zhihu_example.ui.activity.MainActivity;
import com.rain.zhihu_example.ui.adapter.MainAdapter;
import com.rain.zhihu_example.ui.adapter.MainHeadAdapter;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.ui.base.BaseAdapter;
import com.rain.zhihu_example.ui.base.BaseFragment;
import com.rain.zhihu_example.util.CommonUtil;
import com.rain.zhihu_example.util.DateUtil;
import com.rain.zhihu_example.util.ViewUtil;
import com.rain.zhihu_example.widget.LoadMoreRecyclerView;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.*;
import retrofit2.Response;
import retrofit2.http.GET;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yangchunyu
 * 2016/1/25 16:43
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.LoadMoreListener {

    public static final String TAG = "MainFragment";

    public static final int LOAD_TYPE_REFRESH = 0;//加载类型 下拉刷新
    public static final int LOAD_TYPE_LOADMORE = 1;//加载类型 上拉加载更多
    public static final int LOAD_TYPE_CACHE= 2;//加载类型 加载缓存


    private final float POINT_MARGIN = 3;//小点的margin值 dp
    private final float POINT_WIDTH = 6;//小点的width值 dp

    private String data;

    private LoadMoreRecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private MainAdapter mAdapter;
    private ViewPager mViewPager;
    private MainHeadAdapter mHeadAdapter;
    private RelativeLayout mHeadView;
    private Thread headThread;
    private boolean isStop;//是否停止线程
    private boolean isScroll = true;//是否自动滑动 设置改标记是因为isStop设置了false 就会销毁线程
    private LinearLayout pointLayout;
    private ImageView focusPoint;
    private int pointSize = 0;

    private boolean isFinallyShowPage;//最终显示成功页面 用于断网加载缓存后 显示缓存数据

    public static MainFragment newInstance(Bundle args) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置刷新时动画的颜色，可以设置4个
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        mRecyclerView.setAutoLoadMoreEnable(true);
        mRecyclerView.setLoadMoreListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取第一个可见条目
                int first = mRecyclerView.getFirstVisiblePosition();
                //由于添加了头 所以头是第一个
                if(first == 0){
                    ((MainActivity)getActivity()).setToolbarText("首页");
                }else{//在判断是否存在日期头时候 需要first-1 减去头
                    first -= 1;
                    //拿这个条目与mAdapter中的日期列表对应位置相同 则改变MainActivity的actionBar
                    if(mAdapter != null){
                        Map<Integer, String> titleDate = mAdapter.getTitleDate();
                        if(titleDate.containsKey(first)){
                            ((MainActivity)getActivity()).setToolbarText(DateUtil.formatDateByString(titleDate.get(first)));
                        }
                    }
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 用于初始化轮播图方法
     */
    private void initHeadView() {
        //定义ViewPager的父布局
        mHeadView = new RelativeLayout(mContext);
        int height = ViewUtil.dp2px(mContext, 200);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        mHeadView.setLayoutParams(params);

        //定义ViewPager
        mViewPager = new ViewPager(mContext);
        ViewPager.LayoutParams pagerParams = new ViewPager.LayoutParams();
        pagerParams.width = ViewPager.LayoutParams.MATCH_PARENT;
        pagerParams.height = ViewPager.LayoutParams.MATCH_PARENT;
        mViewPager.setLayoutParams(pagerParams);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //因为将ViewPager的长度变为最大int数 所以返回的position需要修正一下 用当前数据的长度
                if (pointSize != 0) position = position % pointSize;
                //判断当前选中点已初始化 并且位置不在最后一个就跟随滚动 （因为最后一个点就不能在滑动了）
                if (focusPoint != null && position != pointSize-1) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) focusPoint.getLayoutParams();
                    float offset = ((POINT_MARGIN + POINT_WIDTH) * (positionOffset + position));
                    params.leftMargin = ViewUtil.dp2px(mContext, offset);
                    focusPoint.setLayoutParams(params);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        isScroll = true;
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isScroll = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isScroll = true;
                        break;
                }
            }
        });
    }

    /**
     * 初始化点
     */
    private void initPointView(int size) {
        pointSize = size;
        pointLayout = null;
        focusPoint = null;
        //定义默认小点的容器
        pointLayout = new LinearLayout(mContext);
        pointLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams pointParam = new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
        pointLayout.setLayoutParams(pointParam);

        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.shape_point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                params.leftMargin = ViewUtil.dp2px(mContext, POINT_MARGIN);
            }
            imageView.setLayoutParams(params);
            pointLayout.addView(imageView);
        }

        //定义小点的容器
        RelativeLayout conter = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams conterParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        conterParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        conterParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        conterParam.bottomMargin = ViewUtil.dp2px(mContext, 6);
        conter.setLayoutParams(conterParam);

        //定义焦点点 和小点容器并列添加至父conter
        focusPoint = new ImageView(mContext);
        focusPoint.setImageResource(R.drawable.shape_point_focus);
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        focusPoint.setLayoutParams(imgParams);

        conter.addView(pointLayout);
        conter.addView(focusPoint);

        mHeadView.removeAllViews();

        mHeadView.addView(mViewPager);
        mHeadView.addView(conter);

    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        data = null;
        requestData(false, LOAD_TYPE_REFRESH);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        if (TextUtils.isEmpty(data)) {
            data = DateUtil.getNowDay();
        } else {
            data = DateUtil.getLastDate(data);
        }
        requestData(false, LOAD_TYPE_LOADMORE);
    }

    @Override
    protected View getSuccessView() {
        final View view = View.inflate(mContext, R.layout.fragment_main, null);
        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recycle_view);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        //初始化头部轮播图
        initHeadView();
        return view;
    }

    @Override
    protected void requestData() {
        data = null;
        requestData(true, LOAD_TYPE_CACHE);
        requestData(false, LOAD_TYPE_REFRESH);
    }

    /**
     * 数据请求
     *
     * @param isCache  是否保存Cache
     * @param loadType 加载类型
     */
    protected void requestData(boolean isCache, int loadType) {
        Retrofit.Builder build = new Retrofit.Builder()
                .baseUrl(Apis.ZH_HOST)
                .addConverterFactory(GsonConverterFactory.create());

        //初始化OkHttp所用的缓存 以及OKhttp请求
        File cacheFile = new File(mContext.getCacheDir(), "zhihu");
        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(getInterceptor(isCache))
                .build();
        build.client(client);
        Retrofit retrofit = build.build();

        Call<HomeMode> call;
        if (loadType == LOAD_TYPE_REFRESH || loadType == LOAD_TYPE_CACHE) {//刷新
            Apis.MainDataService mainService = retrofit.create(Apis.MainDataService.class);
            call = mainService.requestHomeData();
            call.enqueue(new MyCallBack(loadType));
        } else {//加载更多
            Apis.MainLoadMoreDataService mainService = retrofit.create(Apis.MainLoadMoreDataService.class);
            call = mainService.requestMoreData(data);
            call.enqueue(new MyCallBack(loadType));
        }
    }

    private static Interceptor getInterceptor(final boolean isCache) {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();

                //TODO 在此可以拦截URL 并添加数字签名
//                String originUrl = request.url().toString();
//                String sinUrl = originUrl + "";//签名的链接
//                builder.url(sinUrl);

                if (isCache) {//读取缓存
                    builder.cacheControl(CacheControl.FORCE_CACHE);//强制读取缓存
                } else {//读取网络
                    builder.cacheControl(CacheControl.FORCE_NETWORK);//强制请求网络
                }
                return chain.proceed(builder.build());
            }
        };
    }

    /**
     * 请求回调
     */
    private class MyCallBack implements Callback<HomeMode> {

        private int loadType;

        public MyCallBack(int loadType) {
            this.loadType = loadType;
        }

        @Override
        public void onResponse(Response<HomeMode> response) {
            HomeMode mode = response.body();
            if (mode != null) {
                if (loadType == LOAD_TYPE_REFRESH || loadType == LOAD_TYPE_CACHE) {//下拉刷新
                    //设置头部轮播图信息
                    mHeadAdapter = null;
                    mHeadAdapter = new MainHeadAdapter(mode.getTop_stories(),MainFragment.this.getActivity());
                    mViewPager.setAdapter(mHeadAdapter);
                    initPointView(mode.getTop_stories().size());

                    //初始化ViewPager的显示
                    mAdapter = null;
                    mAdapter = new MainAdapter(mode);
                    mAdapter.setOnItemClickListener(new MyItemClick(mode));
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setHeaderEnable(true);
                    mRecyclerView.setHeaderView(mHeadView);
                    //轮播
                    if (headThread == null) {
                        headThread = new Thread(new ViewPagerRunnable());
                        headThread.start();
                    }
                    isStop = false;
                } else {
                    //加载更多
                    if (mAdapter != null && mRefreshLayout != null) {
                        mAdapter.loadMore(mode);
                        mRecyclerView.notifyLoadMoreFinish(true);
                    }
                }
                if(loadType == LOAD_TYPE_CACHE){
                    isFinallyShowPage = true;
                    System.out.println("加载缓存成功，设置不需要显示失败页面");
                }
            }
            stopLayoutRefresh();
            mContentPage.notifyDataChange(mode);
        }


        @Override
        public void onFailure(Throwable t) {
            stopLayoutRefresh();
            if(isFinallyShowPage){
                mContentPage.showSuccessPage();
            }else{
                mContentPage.notifyDataChange(null);
            }
        }
    }

    /**
     * 条目点击事件处理
     */
    private class MyItemClick implements BaseAdapter.OnItemClickListener{

        private HomeMode mMode;
        public MyItemClick(HomeMode mode) {
            this.mMode = mode;
        }

        @Override
        public void onItemClick(View view, int position) {
            //跳转页面详情Activity
            Intent intent = new Intent(getActivity(), ContentDetailActivity.class);
            intent.putExtra(Constances.STORY_ID,mMode.getStories().get(position).getId()+"");
            intent.putExtra(Constances.STORY_TITLE,mMode.getStories().get(position).getTitle());
            intent.putExtra(Constances.STORY_TYPE,mMode.getStories().get(position).getType());
            List<String> mImgs = mMode.getStories().get(position).getImages();
            if(null != mImgs && mImgs.size()>0){
                intent.putExtra(Constances.STORY_IMG,mImgs.get(0));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, BaseActivity.TRANS_TYPE_TRANSLATE);
        }
    }

    /**
     * 停止布局显示加载按钮
     */
    private void stopLayoutRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 持续轮播焦点图
     */
    private class ViewPagerRunnable implements Runnable {

        @Override
        public void run() {
            while (!isStop) {
                SystemClock.sleep(4000);
                if(isScroll){
                    focusPageToNext();
                }
            }
        }
    }

    private void focusPageToNext() {
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mViewPager != null
                        && mHeadAdapter != null
                        && mViewPager.getCurrentItem() + 1 < mHeadAdapter.getCount()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        isScroll = true;//start 和 stop 仅仅是让他不滚动
    }

    @Override
    public void onStop() {
        super.onStop();
        isScroll = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isStop = true;//如果不依附activity 则销毁线程
    }

    interface MainService {
        @GET(Apis.ZH_HOME_LATEST)
        Call<HomeMode> requestHomeData();
    }
}
