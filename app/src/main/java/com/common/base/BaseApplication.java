package com.common.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;

import com.common.http.SupportHttps;
import com.common.widget.RefreshLayout;
import com.cqfrozen.jsh.netstate.NetStateReceiver;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BaseApplication extends Application {

    private static Context context;
    private static int mainThreadId;
    private static Handler handler;
    private static BaseApplication instance;
    private static List<BaseActivity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        mainThreadId = android.os.Process.myPid();
        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);
        handler = new Handler();
        activities = new LinkedList<>();
    }

    public static void initBa(){
        BaseValue.setInit(instance);//初始化全部变量
        RefreshLayout.setInit();//下拉刷新控件初始化
        createUIL();
        SupportHttps.setInit();//初始化https
    }

    private static void createUIL() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration defaultconfig = new ImageLoaderConfiguration.Builder(instance.getBaseContext())
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .writeDebugLogs()
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(defaultconfig);
    }

    /**
     * 设置字体不随系统改变而改变
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return super.getResources();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
    }

    public static Context getContext(){
        return context;
    }

    public static Handler getHandler(){
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public void addActivity(BaseActivity activity){
        activities.add(activity);
    }

    public void removeActivity(BaseActivity activity){
        activities.remove(activity);
    }

    public static void finishActivities(){
        ListIterator<BaseActivity> iterator = activities.listIterator();
        BaseActivity activity;
        while (iterator.hasNext()){
            activity = iterator.next();
            if(activity != null){
                activity.finish();
            }
        }
    }

    public static void quitApp(){
        finishActivities();
        System.exit(0);
    }
}
