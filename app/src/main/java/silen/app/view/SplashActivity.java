package silen.app.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import silen.app.R;

/*
 *
 *
 * 版 权 :@Copyright *****有限公司版权所有
 *
 * 作 者 :lonelysilen
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/11/10  10:21
 *
 * 描 述 :*****处理类
 *
 * 修订日期 :
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvImg;
    private Button mBtnFlight;
    private Button mBtnLogin;

    //更换图标专用
    private ComponentName mDefault;
    private ComponentName mDouble11;
    private ComponentName mDouble12;
    private PackageManager mPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initView();
//        Animation animation = new AlphaAnimation(0.5f, 1.0f);
//        animation.setDuration(3000);
//        mIvImg.setAnimation(animation);
        mBtnLogin.setOnClickListener(this);
        mBtnFlight.setOnClickListener(this);

/************************更换图标专用begin**************************************/
        mDefault = getComponentName();
        mDouble11 = new ComponentName(
                getBaseContext(),
                "silen.app.view.Test1");
        mDouble12 = new ComponentName(
                getBaseContext(),
                "silen.app.view.Test2");
        mPm = getApplicationContext().getPackageManager();


/************************更换图标专用end**************************************/

        new Thread(new Runnable() {
            public void run() {
                SystemClock.sleep(2000);
                Intent intent1 = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent1);
                SplashActivity.this.finish();
            }
        }).start();
    }

    private void initView() {
        //mIvImg = (ImageView) findViewById(R.id.id_iv_spalsh);
        mBtnFlight = (Button) findViewById(R.id.id_btn_flight_manager);
        mBtnLogin = (Button) findViewById(R.id.id_btn_login);

        mBtnFlight.setVisibility(View.GONE);
        mBtnLogin.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_flight_manager:
                /*Intent intent = new Intent(this, SignupActivity.class);//FlightActivity.class
                startActivity(intent);*/
                changeIcon2();
                break;
            case R.id.id_btn_login:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /************************更换图标专用begin**************************************/

    public void changeIcon1() {
        disableComponent(mDefault);
        disableComponent(mDouble12);
        enableComponent(mDouble11);
    }

    public void changeIcon2() {
        disableComponent(mDefault);
        disableComponent(mDouble11);
        enableComponent(mDouble12);
    }

    private void enableComponent(ComponentName componentName) {
        mPm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableComponent(ComponentName componentName) {
        mPm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    /************************更换图标专用end**************************************/
}
