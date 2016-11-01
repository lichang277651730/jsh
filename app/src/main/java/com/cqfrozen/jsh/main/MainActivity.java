package com.cqfrozen.jsh.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
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

    private Handler handler = new Handler(this);


    @Override
    public boolean handleMessage(Message msg) {
        //TODO 要改！
        if (!SPUtils.getFirst()) {
//            startActivity(new Intent(this, IndexActivity.class));
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if(MyApplication.userInfo == null){
                startActivity(new Intent(this, LoginActivity.class));
            }else {
                startActivity(new Intent(this, HomeActivity.class));
            }
        }
        finish();
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        long curTime = System.currentTimeMillis();

        if (token.isEmpty() || token.length() < 2) {
            return;
        }
        //TODO token没失效就不刷新token
//        MyHttp.refreshToken(http, REFRESHTOKEN, token, this);
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if (code != 0) {
            SPUtils.setToken("");
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
