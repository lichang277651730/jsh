package com.cqfrozen.jsh.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.ad.SplashAdActivity;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.entity.UserInfo;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.util.UMengUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MainActivity extends MyActivity implements MyHttp.MyHttpResult, Handler.Callback {

    private static final int REFRESHTOKEN = 1;
    private static final int GETUSERINFO = 2;

    private Handler handler = new Handler(this);

    @Override
    public boolean handleMessage(Message msg) {
        startActivity(new Intent(this, SplashAdActivity.class));
        finish();
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setStopHttp(false);
        setSwipeBackEnable(false);
        initView();
    }


    private void initView() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!MyApplication.isMyInit) {
                }
                autoLogin();
            }
        }.start();
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        handler.sendEmptyMessageDelayed(1, 3000);
        String token = SPUtils.getToken();
        if (token.isEmpty() || token.length() < 2) {
            return;
        }
        MyHttp.refreshToken(http, REFRESHTOKEN, token, this);
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if (code != 0) {
            return;
        }

        switch (which) {
            case REFRESHTOKEN: //刷新token
                SigninInfo signinInfo = (SigninInfo) bean;
                MyApplication.signinInfo = signinInfo;
                MyApplication.token = signinInfo.getToken();
                SPUtils.setToken(signinInfo.getToken());
                MyHttp.user(http, GETUSERINFO, this);
                break;
            case GETUSERINFO: //获得userinfo
                UMengUtils.setSignIn();
                MyApplication.userInfo = (UserInfo) bean;
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
