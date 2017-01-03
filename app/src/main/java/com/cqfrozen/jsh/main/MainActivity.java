package com.cqfrozen.jsh.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.activity.IndexActivity;
import com.cqfrozen.jsh.adapter.HomeAdapter2;
import com.cqfrozen.jsh.adapter.HomeBannerVPAdapter;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.entity.UserInfo;
import com.cqfrozen.jsh.util.CountDownAdTimer;
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

    private int netResponseCode = -1;

    private Handler handler = new Handler(this);
    private ViewPager vp_ad;
    private TextView tv_count_down;
    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos = new ArrayList<>();
    private boolean hasAd = false;
    private boolean isCanGoHome = false;

    @Override
    public boolean handleMessage(Message msg) {
        if (SPUtils.getFirst()) {
            startActivity(new Intent(this, IndexActivity.class));
        } else {
            if(netResponseCode == 0){
                startActivity(new Intent(this, HomeActivity.class));
            }else {
                startActivity(new Intent(this, LoginActivity.class));
            }
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
        setStopHttp(false);
        setSwipeBackEnable(false);
        initAd();
        initView();
    }

    private void initAd() {
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        tv_count_down = (TextView) findViewById(R.id.tv_count_down);
        tv_count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(isCanGoHome){
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                }
            }
        });
        CountDownAdTimer downTimer = new CountDownAdTimer(6000, 100);
        downTimer.going();
        downTimer.setTextView(tv_count_down);
        getAdData();
    }

    private void getAdData() {
        //启动广告数据
        MyHttp.adBannerList(http, HomeAdapter2.TYPE_BANNER, 4, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    hasAd = true;
                    return;
                }
                bannerAdInfos.clear();
                bannerAdInfos.addAll(((HomeBannerAdResultInfo)bean).data1);
                if(bannerAdInfos.size() == 0){
                    return;
                }
                final AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(300);
                vp_ad.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vp_ad.setAnimation(animation);
                        vp_ad.setAdapter(new HomeBannerVPAdapter(MainActivity.this, bannerAdInfos));
                        animation.start();
                        hasAd = true;
                    }
                }, 1000);
            }
        });
    }

    private void initView() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!MyApplication.isMyInit && !hasAd) {
                }
                autoLogin();
            }
        }.start();
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        handler.sendEmptyMessageDelayed(1, 6000);
        String token = SPUtils.getToken();
        if (token.isEmpty() || token.length() < 2) {
            return;
        }
        MyApplication.token = token;
        MyHttp.refreshToken(http, REFRESHTOKEN, token, this);
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        netResponseCode = code;

        if (code != 0) {
//            Log.e("tokentoken", "HandlerHandler");
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    }
//                }, 2000);
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
        isCanGoHome = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
