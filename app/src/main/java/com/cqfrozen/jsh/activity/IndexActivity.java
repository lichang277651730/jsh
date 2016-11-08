package com.cqfrozen.jsh.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.IndexAdapter;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.util.SPUtils;

/**
 * Created by Administrator on 2016/9/18.
 * 索引页
 */
public class IndexActivity extends Activity{

    private ViewPager viewpager;
    private Button btn_go;
    private FrameLayout fl_index_root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //通知栏和虚拟按键透明(xml需要设置属性)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //通知栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟按键透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_index);
        initView();
        initVP();
    }

    private void initView() {
        fl_index_root = (FrameLayout) findViewById(R.id.fl_index_root);
        btn_go = (Button) findViewById(R.id.btn_go);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.setFirst(false);
                startActivity(new Intent(IndexActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void initVP() {
        viewpager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewpager.setAdapter(new IndexAdapter(this));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fl_index_root.setBackgroundColor(getResources().getColor(R.color.guide1));
                        break;
                    case 1:
                        fl_index_root.setBackgroundColor(getResources().getColor(R.color.guide2));
                        break;
                    case 2:
                        fl_index_root.setBackgroundColor(getResources().getColor(R.color.guide3));
                        break;
                    default:
                        break;
                };
                if (position == 2) { //显示按钮
                    btn_go.setVisibility(View.VISIBLE);
                } else {
                    btn_go.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
