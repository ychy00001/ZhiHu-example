package com.rain.zhihu_example.ui.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.rain.zhihu_example.R;
import com.rain.zhihu_example.global.Constances;
import com.rain.zhihu_example.ui.activity.MainActivity;
import com.rain.zhihu_example.ui.activity.SplashActivity;


/**
 * Created by Diagrams on 2015/12/22 17:59
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final int TRANS_TYPE_SHADE = 0x0001;
    public static final int TRANS_TYPE_ZOOM = 0x0002;
    public static final int TRANS_TYPE_TRANSLATE = 0x0003;

    protected SharedPreferences mSharedPreference;

    private int mAnimType = -1;//用于接收启动时候的动画是哪种动画
    private boolean isNightTheme = false;
    private boolean themeCache = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreference = getSharedPreferences(Constances.SP_NAME, MODE_PRIVATE);
        //true 夜间 false日间
        isNightTheme = mSharedPreference.getBoolean(Constances.SP_IS_NIGHT_THEME, false);
        //根据保存白天夜间 设置主题
        if(themeCache != isNightTheme){//若主题缓存与获取的主题不一致 则要重置主题
            themeCache = isNightTheme;//设置缓存
            if(isNightTheme){//夜间
                setTheme(R.style.AppTheme_Dark);
            }else{//日间
                setTheme(R.style.AppTheme_Light);
            }
        }

        setContentView(setContentLayout());
        ButterKnife.bind(this);//绑定ButterKnife
        mAnimType = getIntent().getIntExtra("animType", -1);
    }

    /**
     * 初始化UI
     * @return 需要返回加载界面的id
     */
    protected abstract int setContentLayout();


    /******************Activity动画处理**********************/

    /**
     * activity启动动画
     */
    protected void setPendingTransitionIn(){
        setPendingTransitionIn(TRANS_TYPE_TRANSLATE);
    }

    /**
     * activity启动动画
     * @param transitionType 动画类型
     */
    protected void setPendingTransitionIn(int transitionType) {
        switch (transitionType) {
            case TRANS_TYPE_SHADE:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case TRANS_TYPE_ZOOM:
                overridePendingTransition(R.anim.activity_zoomin, R.anim.activity_zoomout);
                break;
            case TRANS_TYPE_TRANSLATE:
                overridePendingTransition(R.anim.activity_in_translate_horizontal_anim,R.anim.activity_out_translate_horizontal_anim);
                break;
        }

    }

    /**
     * activity结束动画
     * @param transitionType 动画类型
     */
    public void setPendingTransitionOut(int transitionType) {
        switch (transitionType) {
            case TRANS_TYPE_SHADE:
                overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in);
                break;
            case TRANS_TYPE_ZOOM:
                overridePendingTransition(R.anim.activity_zoomout,R.anim.activity_zoomin);
                break;
            case TRANS_TYPE_TRANSLATE:
                overridePendingTransition(R.anim.activity_exit_in_translate_horizontal_anim,R.anim.activity_exit_translate_horizontal_anim);
                break;
        }

    }

    /**********************跳转Activity处理********************/

    /**
     * 无参数跳转主界面
     */
    protected void toMainActivity(){
        toMainActivity(null);
    }

    /**
     * 去主界面
     * @param fromTAG 从哪里请求的 就是一个标记字符串
     *
     */
    protected void toMainActivity(String fromTAG){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        if(fromTAG.equals(SplashActivity.TAG)){//如果是从启动页面来的话独立处理
            setPendingTransitionIn(TRANS_TYPE_ZOOM);
        }else{
            setPendingTransitionIn();
        }
    }

    /**
     *
     * 启动一个activity伴随着动画
     * @param intent 意图
     * @param animType 动画类型
     *
     */
    public void startActivity(Intent intent, int animType){
        intent.putExtra("animType", animType);
        super.startActivity(intent);
        setPendingTransitionIn(animType);
    }

    /**
     * 开启一个activity伴随有返回
     * @param intent 意图
     * @param requestCode 请求码
     * @param animType 动画类型
     */
    public void startActivityForResult(Intent intent, int requestCode, int animType){
        intent.putExtra("animType", animType);
        super.startActivityForResult(intent, requestCode);
        setPendingTransitionIn(animType);
    }

    public void finish(){
        super.finish();
        if(mAnimType != -1){
            setPendingTransitionOut(mAnimType);
        }
    }

    /************************Activity销毁*******************/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
