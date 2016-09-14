package com.cqfrozen.jsh;

import android.content.Context;
import android.content.SharedPreferences;

import com.cqfrozen.jsh.base.BaseApplication;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.entity.UserInfo;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MyApplication extends BaseApplication {

    public static SharedPreferences userSp;
    public static String token = "";
    public static UserInfo userInfo;
    public static SigninInfo signinInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        userSp = getSharedPreferences("jsh_sp", Context.MODE_PRIVATE);
    }
}
