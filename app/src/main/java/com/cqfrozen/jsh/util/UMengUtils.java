package com.cqfrozen.jsh.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.cqfrozen.jsh.main.MyApplication;
import com.umeng.analytics.MobclickAgent;

/**
 * 友盟数据统计方法
 */
public class UMengUtils {
    /**
     * 注册友盟数据统计
     */
    public static void setUMeng(Context context) {
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL); //注册友盟
        MobclickAgent.enableEncrypt(true);//友盟加密
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.openActivityDurationTrack(true); //不使用自带的统计,使用自定义统计页面
    }

    /**
     * 开启页面
     */
    public static void setOnPageStart(Context context) {
        try {
            MobclickAgent.onResume(context);
            MobclickAgent.onPageStart(context.getClass().getName());
        } catch (Exception e) {
        }
    }
    public static void setOnPageStart(Fragment context) {
        try {
            MobclickAgent.onResume(context.getActivity());
            MobclickAgent.onPageStart(context.getClass().getName());
        } catch (Exception e) {
        }
    }
    /**
     * 关闭页面
     */
    public static void setOnonPageEnd(Context context) {
        try {
            MobclickAgent.onPause(context);
            MobclickAgent.onPageEnd(context.getClass().getName());
        } catch (Exception e) {
        }
    }
    public static void setOnonPageEnd(Fragment context) {
        try {
            MobclickAgent.onPause(context.getActivity());
            MobclickAgent.onPageEnd(context.getClass().getName());
        } catch (Exception e) {
        }
    }

    /**
     * 统计帐号登入
     */
    public static void setSignIn() {
        try {
            if (null != MyApplication.userInfo && !TextUtils.isEmpty(MyApplication.userInfo
                    .mobile_num)) {
                MobclickAgent.onProfileSignIn(MyApplication.userInfo.mobile_num);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 统计帐号登出
     */
    public static void setSignOff() {
        try {
            if (null != MyApplication.userInfo && !TextUtils.isEmpty(MyApplication.userInfo
                    .mobile_num)) {
                MobclickAgent.onProfileSignOff();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 保存友盟的数据
     */
    public static void setKillProcess(Context context) {
        try {
            MobclickAgent.onKillProcess(context);//保存友盟的数据
        } catch (Exception e) {
        }
    }
}
