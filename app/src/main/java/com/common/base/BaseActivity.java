package com.common.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.common.http.HttpForVolley;
import com.common.swipbacklayout.SwipeBackActivity;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BaseActivity extends SwipeBackActivity {

    private Toast toast;
    public HttpForVolley  http;
    public boolean isStopHttp = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        http = new HttpForVolley(this);
        setTransparencyBar(true);
    }

    //打印吐司
    public void showToast(String msg){
        try {
            if(toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            Log.e("error", "error:" + e.getMessage());
        }

    }

    //是否透明化状态栏
    public void setTransparencyBar(boolean transparency){
        if(transparency){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//				getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }


    /**
     * stop页面的时候是否执行取消网络请求
     */
    public void setStopHttp(boolean isStopHttp) {
        this.isStopHttp = isStopHttp;
    }
}
