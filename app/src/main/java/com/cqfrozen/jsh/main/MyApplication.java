package com.cqfrozen.jsh.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.common.base.BaseApplication;
import com.cqfrozen.jsh.activity.MainActivity;
import com.cqfrozen.jsh.constants.Constants;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.entity.UserInfo;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MyApplication extends BaseApplication {

    protected static MyApplication instance;
    public static boolean isMyInit = false;
    public static SharedPreferences userSp;
    public static String token = "";
    public static UserInfo userInfo;
    public static SigninInfo signinInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setInit();
    }

    private void setInit() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                initMy();
            }
        }.start();
    }

    private void initMy(){
        BaseApplication.initBa();
        userSp = getSharedPreferences(Constants.SP_FILE, Context.MODE_PRIVATE);
        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
        isMyInit = true;
    }

    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Intent intent = new Intent(instance, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };
}
