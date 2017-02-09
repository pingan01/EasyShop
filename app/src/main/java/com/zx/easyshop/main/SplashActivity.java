package com.zx.easyshop.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zx.easyshop.R;
import com.zx.easyshop.commons.ActivityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    protected ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activityUtils = new ActivityUtils(this);

        // TODO: 2017/2/7 0007 环信登录相关
        // TODO: 2017/2/7 0007 判断用户是否登录，自动登录

        //1.5秒后跳转到主界面
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activityUtils.startActivity(MainActivity.class);
                finish();//结束SplashActivity界面
            }
        }, 1500);
    }
}
