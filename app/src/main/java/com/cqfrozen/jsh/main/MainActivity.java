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
import com.cqfrozen.jsh.ad.SplashAdActivity;
import com.cqfrozen.jsh.adapter.HomeAdapter2;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.entity.UserInfo;
import com.cqfrozen.jsh.util.JSONUtil;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.util.UMengUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MainActivity extends MyActivity implements MyHttp.MyHttpResult, Handler.Callback {

    private static final int REFRESHTOKEN = 1;
    private static final int GETUSERINFO = 2;
    private boolean hasAd = false;
    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos = new ArrayList<>();

    private Handler handler = new Handler(this);

    @Override
    public boolean handleMessage(Message msg) {
        if(hasAd){//有广告
            if(TextUtils.isEmpty(SPUtils.getToken()) && SPUtils.getFirst()){
                startActivity(new Intent(this, IndexActivity.class));
            }else if(TextUtils.isEmpty(SPUtils.getToken()) && !SPUtils.getFirst()){
                startActivity(new Intent(this, LoginActivity.class));
            }else {
                startActivity(new Intent(this, SplashAdActivity.class));
            }
        }else {//没有广告
            if (SPUtils.getFirst()) {//首次安装
                startActivity(new Intent(this, IndexActivity.class));
            } else {
                if(isLogined()){
                    startActivity(new Intent(this, HomeActivity.class));
                }else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        overridePendingTransition(R.anim.activity_ani_alpha_enter, 0);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        hasAdData();//检测是否有广告数据
        setStopHttp(false);
        setSwipeBackEnable(false);
        initView();
    }

    private void hasAdData() {
        //启动广告数据
        MyHttp.adBannerList(http, HomeAdapter2.TYPE_BANNER, 4, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    hasAd = false;
                    return;
                }
                bannerAdInfos.clear();
                bannerAdInfos.addAll(((HomeBannerAdResultInfo)bean).data1);
                if(bannerAdInfos.size() == 0){
                    hasAd = false;
                    return;
                }
                hasAd = true;
            }
        });
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
        handler.sendEmptyMessageDelayed(1, 3500);
//        String userInfoCache = SPUtils.getUserInfo();
//        UserInfo userInfoSave = JSONUtil.fromJson(userInfoCache, UserInfo.class);
//        MyApplication.userInfo = userInfoSave;
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
                UserInfo userInfo = (UserInfo) bean;
                MyApplication.userInfo = userInfo;
                String userJson = JSONUtil.toJson(userInfo);
                SPUtils.setUserInfo(userJson);
                UMengUtils.setSignIn();
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

    @Override
    public void finish() {
        super.finish();
    }
}
