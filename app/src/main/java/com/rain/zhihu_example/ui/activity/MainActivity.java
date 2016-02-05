package com.rain.zhihu_example.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.OnClick;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.ui.base.BaseActivity;
import com.rain.zhihu_example.ui.fragment.MainFragment;
import com.rain.zhihu_example.util.SnackUtil;
import com.rain.zhihu_example.util.ToastUtil;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton mFab;//浮动按钮
    @SuppressWarnings("FieldCanBeLocal") private Toolbar mToolbar;
    @SuppressWarnings("FieldCanBeLocal") private DrawerLayout mDrawer;
    @SuppressWarnings("FieldCanBeLocal") private NavigationView mNavigationView;
    private MainFragment mFragment;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();
    }

    /**
     * 启动主页面Fragment
     */
    private void initFragment() {
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
    void fabClick(){
        SnackUtil.showMessage(mFab, "点击浮动按钮");
    }


/*****************************整体界面设置************************************/
    /**
     * 设置ToolBar上面的文字
     * @param text 所要显示的文字
     */
    public void setToolbarText(String text){
        mToolbar.setTitle(text);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 右侧设置点击
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ToastUtil.showToast("点击设置");
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camara) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
