package com.cqfrozen.jsh.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.activity.IndexActivity;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.entity.UserInfo;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MainActivity extends MyActivity implements MyHttp.MyHttpResult, Handler.Callback {

    private static final int REFRESHTOKEN = 1;
    private static final int GETUSERINFO = 2;

    private static MainActivity instance;
    private int netResponseCode = -1;

    private Handler handler = new Handler(this);

    @Override
    public boolean handleMessage(Message msg) {
        if (SPUtils.getFirst()) {
            startActivity(new Intent(this, IndexActivity.class));
        } else {
//            if(MyApplication.userInfo == null){
//                startActivity(new Intent(this, LoginActivity.class));
//            }else {
            if(netResponseCode == 0){
                startActivity(new Intent(this, HomeActivity.class));
            }else {
                startActivity(new Intent(this, LoginActivity.class));
            }
//            }
        }
        finish();
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        instance = this;
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
                AutoLogin();
            }
        }.start();
    }

    /**
     * 自动登录
     */
    private void AutoLogin() {
        handler.sendEmptyMessageDelayed(1, 2000);
        String token = SPUtils.getToken();
        long expireTime = SPUtils.getExpireTime();
        long curTime = System.currentTimeMillis() / 1000;

        if (token.isEmpty() || token.length() < 2) {
            return;
        }
        MyHttp.refreshToken(http, REFRESHTOKEN, token, this);
//        if(curTime < expireTime - 5 * 60){//不刷新token
//            MyApplication.token = SPUtils.getToken();
//            MyHttp.user(http, GETUSERINFO, this);
//        }else {//刷新token
//        }


    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        netResponseCode = code;
        if(code == 404){//网络错误
            return;
        }
        if (code != 0) {
//            SPUtils.setToken("");
            if(!TextUtils.isEmpty(SPUtils.getToken())){
                MyApplication.token = SPUtils.getToken();
                MyHttp.user(http, GETUSERINFO, this);
            }else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            return;
        }

//        if(code == 2){
//            MyHttp.refreshToken(http, REFRESHTOKEN, SPUtils.getToken(), this);
//            return;
//        }

        switch (which) {
            case REFRESHTOKEN: //刷新token
                SigninInfo signinInfo = (SigninInfo) bean;
                MyApplication.signinInfo = signinInfo;
                MyApplication.token = signinInfo.getToken();
                SPUtils.setToken(signinInfo.getToken());
                MyHttp.user(http, GETUSERINFO, this);
                break;
            case GETUSERINFO: //获得userinfo
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
