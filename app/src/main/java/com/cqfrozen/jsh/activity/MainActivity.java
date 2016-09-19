package com.cqfrozen.jsh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.SPUtils;

/**
 * 程序入口，闪屏页
 */
public class MainActivity extends MyActivity {

    private static MainActivity instance;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(SPUtils.getFirst()){
                startActivity(new Intent(MainActivity.this, IndexActivity.class));
            }else {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        setStopHttp(false);
        setSwipeBackEnable(false);
        initView();
    }

    private void initView() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(!MyApplication.isMyInit){}
                autoLogin();
            }
        }.start();
    }

    private void autoLogin() {
        handler.sendEmptyMessageDelayed(1, 2000);
        String token = SPUtils.getToken();
        if(token.isEmpty() || token.length() < 2){
            return;
        }
        //TODO 进入软件，执行登陆逻辑，token有效，拿token登陆，token失效就进入主界面
        Log.d(TAG, "登陆业务");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
