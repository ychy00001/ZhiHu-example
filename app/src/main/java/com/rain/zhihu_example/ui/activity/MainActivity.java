package com.rain.zhihu_example.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
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
import butterknife.OnClick;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.ui.fragment.MainFragment;
import com.rain.zhihu_example.ui.fragment.SubscribeFragment;
import com.rain.zhihu_example.util.ThemeUtil;
import com.rain.zhihu_example.util.ToastUtil;
import com.rain.zhihu_example.util.ViewUtil;

import java.lang.ref.WeakReference;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton mFab;//浮动按钮
    @SuppressWarnings("FieldCanBeLocal") private Toolbar mToolbar;
    @SuppressWarnings("FieldCanBeLocal") private DrawerLayout mDrawer;
    @SuppressWarnings("FieldCanBeLocal") private NavigationView mNavigationView;
    private Fragment mFragment;
    private ImageView mLoginImg;
    private ThemeUtil mThemeUtil;

    private boolean isHeadMenu;//是否显示主菜单
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
        initFragment();
    }

    /**
     * 启动主页面Fragment
     */
    private void initFragment() {
        isHeadMenu = true;
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
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mLoginImg = (ImageView) findViewById(R.id.imageView);

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        //设置选中首页
        mNavigationView.setCheckedItem(R.id.nav_head);
    }

    /**
     * 浮动按钮点击
     */
    @OnClick(R.id.fab)
    void fabClick() {
        showExitDialog();
    }

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
        lp.height = (int) (ViewUtil.dp2px(MainActivity.this, 200)); //设置宽度
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        dialog.setCanceledOnTouchOutside(true);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    MainActivity.this.finish();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
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
        params.gravity = Gravity.LEFT + Gravity.TOP;
        wm.addView(windowImg, params);
    }

    /**
     * 截取屏幕
     */
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
            replaceFragment("12", "用户推荐日报");
        } else if (id == R.id.nav_psychology) {//心理学 api/4/theme/13
            replaceFragment("13", "日常心理学");
        } else if (id == R.id.nav_unbored) {//不许无聊api/4/theme/11
            replaceFragment("11", "不许无聊");
        } else if (id == R.id.nav_move) {//电影api/4/theme/3
            replaceFragment("3", "电影日报");
        } else if (id == R.id.nav_design) {//设计api/4/theme/4
            replaceFragment("4", "设计日报");
        } else if (id == R.id.nav_company) {//大公司api/4/theme/5
            replaceFragment("5", "大公司日报");
        } else if (id == R.id.nav_financial) {//金融 api/4/theme/6
            replaceFragment("6", "财经日报");
        } else if (id == R.id.nav_net_safe) {//互联网安全api/4/theme/10
            replaceFragment("10", "互联网安全日报");
        } else if (id == R.id.nav_start_game) {//游戏api/4/theme/2
            replaceFragment("2", "开始游戏");
        } else if (id == R.id.nav_music) {//音乐api/4/theme/7
            replaceFragment("7", "音乐日报");
        } else if (id == R.id.nav_cartoon) {//卡通api/4/theme/9
            replaceFragment("9", "动漫日报");
        } else if (id == R.id.nav_sport) {//体育api/4/theme/8
            replaceFragment("8", "体育日报");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //重新请求菜单
        invalidateOptionsMenu();
        return true;
    }

    /**
     * 替换fragment 显示订阅标签
     */
    private void replaceFragment(String subscribeId, String subscribeName) {
        isHeadMenu = false;
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
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isHeadMenu) {
            menu.setGroupVisible(R.id.group_main, true);
            menu.setGroupVisible(R.id.group_subscribe, false);
        } else {
            menu.setGroupVisible(R.id.group_main, false);
            menu.setGroupVisible(R.id.group_subscribe, true);
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
}
