package com.cqfrozen.jsh.ad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.activity.IndexActivity;
import com.cqfrozen.jsh.adapter.HomeAdapter2;
import com.cqfrozen.jsh.adapter.SplashVPAdapter;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.center.WebUrlActivity;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.CountDownAdTimer;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class SplashAdActivity extends MyActivity implements Handler.Callback {

    private ViewPager vp_ad;
    private TextView tv_count_down;
    private TextView tv_skip;
    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos = new ArrayList<>();
    private SplashVPAdapter vpAdapter;
    private LinearLayout ll_count_time;
    private boolean hasAd = false;
    private Handler handler = new Handler(this);

    @Override
    public boolean handleMessage(Message message) {
        if (SPUtils.getFirst()) {
            startActivity(new Intent(this, IndexActivity.class));
        } else {
            if(isLogined()){
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
        setContentView(R.layout.activity_splash_ad);
        setStopHttp(false);
        setSwipeBackEnable(false);
        initView();
        initVP();
        getData();
        handler.sendEmptyMessageDelayed(1, 4000);
    }

    private void initView() {
        ll_count_time = (LinearLayout) findViewById(R.id.ll_count_time);
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        tv_count_down = (TextView) findViewById(R.id.tv_count_down);
        tv_skip = (TextView) findViewById(R.id.tv_skip);
        CountDownAdTimer downTimer = new CountDownAdTimer(4000, 100);
        downTimer.going();
        downTimer.setTextView(tv_count_down);

        ll_count_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);
                if(isLogined()){
                    startActivity(new Intent(SplashAdActivity.this, HomeActivity.class));
                }else {
                    startActivity(new Intent(SplashAdActivity.this, LoginActivity.class));
                }
                finish();
            }
        });
    }

    private void initVP() {
        vpAdapter = new SplashVPAdapter(this, bannerAdInfos);
        vp_ad.setAdapter(vpAdapter);
        vpAdapter.setOnItemClickListener(new SplashVPAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                handler.removeCallbacksAndMessages(null);
                HomeBannerAdResultInfo.HomeBannerAdInfo bannerAdInfo = bannerAdInfos.get
                        (position);
                int content_type = bannerAdInfo.content_type;
                startActivity(new Intent(SplashAdActivity.this, HomeActivity.class));
                // 1图文，2外链接，3不看详情，4粮票（图文），5粮票（外链），6购买[返回商品id]
                switch (content_type) {
                    case 1:
                    case 2:
                        MyHttp.adRead(http, null, bannerAdInfo.ad_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                            }
                        });
                        Intent intent2 = new Intent(SplashAdActivity.this, WebUrlActivity.class);
                        intent2.putExtra("title", bannerAdInfo.title);
                        intent2.putExtra("url", bannerAdInfo.content);
                        startActivity(intent2);
                        break;
                    case 3:
                        break;
                    case 4:
                    case 5:
                        MyHttp.adRead(http, null, bannerAdInfo.ad_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                            }
                        });
                        Intent intent = new Intent(SplashAdActivity.this, BannerDetailActivity.class);
                        intent.putExtra("ad_id", bannerAdInfo.ad_id);
                        startActivity(intent);
                        break;
                    case 6:
                        MyHttp.adRead(http, null, bannerAdInfo.ad_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                            }
                        });
                        Intent intent1 = new Intent(SplashAdActivity.this, GoodsDetailActivity.class);
                        intent1.putExtra("g_id", Long.parseLong(bannerAdInfo.content));
                        startActivity(intent1);
                        break;
                }
                finish();
            }
        });
    }

    private void getData() {
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
                vpAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
