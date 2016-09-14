package com.cqfrozen.jsh.base;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BaseValue {
    public static final boolean isDebug = false;
    public static int screenWidth;//屏幕高度
    public static int screenHeight;//屏幕宽度
    public static float density;//设备密度
    public static float scaledDensity;//屏幕放大密度
    public static int densityDPI;//屏幕放大密度
    public static Gson gson;
    public static RequestQueue mQueue;
    public static InputMethodManager imm;

    public static void setInit(Application application){
        getDisplayValue(application);
        gson = new Gson();
        mQueue = Volley.newRequestQueue(application);
        imm = (InputMethodManager) application.getBaseContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**获取屏幕参数
     * @param application
     */
    private static void getDisplayValue(Application application) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = application.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        density = displayMetrics.density;
        scaledDensity = displayMetrics.scaledDensity;
        densityDPI = displayMetrics.densityDpi;
    }

    public static int dp2px(float dpVal){
        return (int)(dpVal * density + 0.5f);
    }

    public static DisplayImageOptions getOptions(int defaultimg){
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(defaultimg)
                .showImageForEmptyUri(defaultimg)
                .build();
    }

}
