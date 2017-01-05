package com.common.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Toast;

import com.common.http.HttpForVolley;
import com.common.swipbacklayout.SwipeBackActivity;
import com.cqfrozen.jsh.netstate.NetChangeObserver;
import com.cqfrozen.jsh.netstate.NetStateReceiver;
import com.cqfrozen.jsh.netstate.NetUtils;
import com.cqfrozen.jsh.util.UIUtils;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BaseActivity extends SwipeBackActivity {

    private Toast toast;
    public HttpForVolley  http;
    public boolean isStopHttp = true;
    private NetChangeObserver mNetChangeObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication)UIUtils.getContext()).addActivity(this);
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        http = new HttpForVolley(this);
        setTransparencyBar(true);
        // 网络改变的一个回掉类
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                onNetworkDisConnected();
            }
        };

        //开启广播去监听 网络 改变事件
        NetStateReceiver.registerObserver(mNetChangeObserver);
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
            e.printStackTrace();
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
    }

    protected void onNetworkConnected(NetUtils.NetType type){

    }

    protected void onNetworkDisConnected(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        ((BaseApplication)UIUtils.getContext()).removeActivity(this);
    }
}
