package com.rain.zhihu_example.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.OnClick;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.ui.fragment.CollectionFragment;
import com.rain.zhihu_example.ui.fragment.MainFragment;
import com.rain.zhihu_example.ui.fragment.SubscribeFragment;
import com.rain.zhihu_example.ui.fragment.UserInfoFragment;
import com.rain.zhihu_example.util.LoginUtil;
import com.rain.zhihu_example.util.ThemeUtil;
import com.rain.zhihu_example.util.ToastUtil;
import com.rain.zhihu_example.util.ViewUtil;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final int CODE_LOGIN = 1001;//登录的requestCode
    public static final int CODE_COLLECTION = 1002;//收藏的requestCode

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private LinearLayout mLayoutCollection;//收藏布局
    private LinearLayout mLayoutNothing;//没用布局
    private LinearLayout mLayoutLogin;//登录布局
    private ImageView mImgLoginIco;//登录用户ico
    private TextView mTXLoginNickName;//登录用户昵称
    private Fragment mFragment;
    private ThemeUtil mThemeUtil;

    private boolean isSubscribeMenu;//是否显示订阅的菜单
    private boolean isNoRightMenu;//是否显示收藏的菜单
    private boolean isAttention;//是否关注（在订阅标签下）
    private ImageView windowImg;
    private WindowManager wm;
    private WeakReference<Bitmap> bmp;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThemeUtil = new ThemeUtil(this);
        initView();
        initListener();
        initFragment();
        checkLogin();//检查登录
    }

    private void checkLogin(){
        if(LoginUtil.getInstance(this).checkLogin()){
            Picasso.with(this).load(LoginUtil.getInstance(this).getIco()).into(mImgLoginIco);
            mTXLoginNickName.setText(LoginUtil.getInstance(this).getUserName());
        }
    }
    /**
     * 登录界面返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == LoginActivity.CODE_LOGIN_FINISH && data != null){
            String nickName = data.getStringExtra(LoginActivity.RESULT_NICKNAME);
            String ico = data.getStringExtra(LoginActivity.RESULT_ICON);
            Picasso.with(this).load(ico).into(mImgLoginIco);
            mTXLoginNickName.setText(nickName);
            if(requestCode == CODE_COLLECTION){
                replaceCollectionFragment();
            }
        }
    }

    /**
     * 启动主页面Fragment
     */
    private void initFragment() {
        isSubscribeMenu = false;
        isNoRightMenu = false;
        mFragment = MainFragment.newInstance(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mFragment, MainFragment.TAG)
                .commit();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mLayoutCollection = (LinearLayout) mNavigationView.getHeaderView(0).findViewById(R.id.ll_collection);
        mLayoutNothing = (LinearLayout) mNavigationView.getHeaderView(0).findViewById(R.id.ll_nothing);
        mLayoutLogin = (LinearLayout) mNavigationView.getHeaderView(0).findViewById(R.id.ll_login);
        mImgLoginIco = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.img_ico);
        mTXLoginNickName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_nickName);

        setSupportActionBar(mToolbar);
        //设置选中首页
        mNavigationView.setCheckedItem(R.id.nav_head);
    }

    private void initListener(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        mLayoutCollection.setOnClickListener(this);
        mLayoutNothing.setOnClickListener(this);
        mLayoutLogin.setOnClickListener(this);
    }
    /**
     * 浮动按钮点击
     */
    @OnClick(R.id.fab)
    void fabClick() {
        showExitDialog();
    }

    @SuppressWarnings("deprecation")
    private void showExitDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();

        //        /**
        //         * 设置显示位置
        //         */
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); //设置宽度
        lp.height = (ViewUtil.dp2px(MainActivity.this, 200)); //设置宽度
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        dialog.setCanceledOnTouchOutside(true);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }


    /*****************************整体界面设置************************************/
    /**
     * 设置ToolBar上面的文字
     *
     * @param text 所要显示的文字
     */
    public void setToolbarText(String text) {
        mToolbar.setTitle(text);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitDialog();
        }
    }

    /**
     * 右侧设置点击
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                ToastUtil.showToast("点击设置");
                break;
            case R.id.action_theme_mode:
                //截取屏幕显示
                showFullImg();
                //停0.5秒
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(400);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mThemeUtil.changeTheme();
                                //图片变淡
                                shadeWindowImg();
                            }
                        });
                    }
                }).start();
                break;
            case R.id.action_subscribe:
                isAttention = !isAttention;
                break;
        }
        //重新请求菜单
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 渐渐变淡
     */
    private void shadeWindowImg() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(windowImg, "alpha", 1.0f, 0.0f)
                .setDuration(300);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
               wm.removeViewImmediate(windowImg);
               windowImg.destroyDrawingCache();
               bmp.clear();
               windowImg = null;
               wm = null;
               bmp = null;
               System.gc();
            }
        });
        alpha.start();
    }

    /**
     * 展示全屏图片
     */
    private void showFullImg() {
        windowImg = new ImageView(this);
        windowImg.setImageBitmap(myShot(this).get());
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE//不能点击
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.START + Gravity.TOP;
        wm.addView(windowImg, params);
    }

    /**
     * 截取屏幕
     */
    @SuppressWarnings("deprecation")
    public WeakReference<Bitmap> myShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        bmp = new WeakReference<>(Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights));

        // 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 侧拉菜单点击
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_head) {//首页
            initFragment();
        } else if (id == R.id.nav_recommend) {//每日推荐 api/4/theme/12
            replaceSubscribeFragment("12", "用户推荐日报");
        } else if (id == R.id.nav_psychology) {//心理学 api/4/theme/13
            replaceSubscribeFragment("13", "日常心理学");
        } else if (id == R.id.nav_unbored) {//不许无聊api/4/theme/11
            replaceSubscribeFragment("11", "不许无聊");
        } else if (id == R.id.nav_move) {//电影api/4/theme/3
            replaceSubscribeFragment("3", "电影日报");
        } else if (id == R.id.nav_design) {//设计api/4/theme/4
            replaceSubscribeFragment("4", "设计日报");
        } else if (id == R.id.nav_company) {//大公司api/4/theme/5
            replaceSubscribeFragment("5", "大公司日报");
        } else if (id == R.id.nav_financial) {//金融 api/4/theme/6
            replaceSubscribeFragment("6", "财经日报");
        } else if (id == R.id.nav_net_safe) {//互联网安全api/4/theme/10
            replaceSubscribeFragment("10", "互联网安全日报");
        } else if (id == R.id.nav_start_game) {//游戏api/4/theme/2
            replaceSubscribeFragment("2", "开始游戏");
        } else if (id == R.id.nav_music) {//音乐api/4/theme/7
            replaceSubscribeFragment("7", "音乐日报");
        } else if (id == R.id.nav_cartoon) {//卡通api/4/theme/9
            replaceSubscribeFragment("9", "动漫日报");
        } else if (id == R.id.nav_sport) {//体育api/4/theme/8
            replaceSubscribeFragment("8", "体育日报");
        }

        closeDrawLayout();
        return true;
    }

    /**
     * 关闭侧滑菜单
     */
    private void closeDrawLayout(){
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }
    /**
     * 跳转用户信息Fragment
     */
    private void replaceUserInfoFragment(){
        isSubscribeMenu = false;
        isNoRightMenu = true;

        mFragment = UserInfoFragment.newInstance(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mFragment, UserInfoFragment.TAG)
                .commit();
        //重新请求菜单
        invalidateOptionsMenu();
    }
    /**
     * 替换fragment 显示订阅标签
     */
    private void replaceSubscribeFragment(String subscribeId, String subscribeName) {
        isSubscribeMenu = true;
        isNoRightMenu = false;

        isAttention = false;
        Bundle extras = getIntent().getExtras();
        if (null == extras) {
            extras = new Bundle();
        }
        extras.putString(Constances.ID_SUBSCRIBE, subscribeId);
        extras.putString(Constances.NAME_SUBSCRIBE, subscribeName);
        mFragment = SubscribeFragment.newInstance(extras);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mFragment, SubscribeFragment.TAG)
                .commit();
        //重新请求菜单
        invalidateOptionsMenu();
    }
    /**
     * 跳转登录界面Fragment
     */
    private void replaceCollectionFragment(){
        isSubscribeMenu = false;
        isNoRightMenu = true;

        mFragment = CollectionFragment.newInstance(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mFragment, CollectionFragment.TAG)
                .commit();
        //重新请求菜单
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isSubscribeMenu) {
            menu.setGroupVisible(R.id.group_main, false);
            menu.setGroupVisible(R.id.group_subscribe, true);
        } else if(isNoRightMenu){
            menu.setGroupVisible(R.id.group_main, false);
            menu.setGroupVisible(R.id.group_subscribe, false);
        }else{
            menu.setGroupVisible(R.id.group_main, true);
            menu.setGroupVisible(R.id.group_subscribe, false);
        }
        if (isAttention) {
            menu.getItem(3).setIcon(R.mipmap.ic_menu_block);
        } else {
            menu.getItem(3).setIcon(android.R.drawable.ic_menu_add);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_nothing:
                //点击没用按钮
                ToastUtil.showToast("跟你说没用了。。。");
                break;
            case R.id.ll_collection:
                //点击收藏
                if(LoginUtil.getInstance(this).checkLogin()){
                    replaceCollectionFragment();
                }else{
                    startLoginActivity(CODE_COLLECTION);
                }
                break;
            case  R.id.ll_login:
                //登录跳转
                if(LoginUtil.getInstance(MainActivity.this).checkLogin()){
                    replaceUserInfoFragment();
                }else{
                    startLoginActivity(CODE_LOGIN);
                }
                break;
        }
        closeDrawLayout();

    }
    /**
     * 启动登录页
     */
    private void startLoginActivity(int requestCode) {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivityForResult(intent,requestCode,MainActivity.TRANS_TYPE_TRANSLATE);
    }

    /**
     * 登出用户
     */
    public void loginOut(){
        mImgLoginIco.setImageResource(R.mipmap.user_default_avatar);
        mTXLoginNickName.setText(getResources().getString(R.string.login_def_name));
        initFragment();
    }
}
