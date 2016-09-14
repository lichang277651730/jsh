package com.cqfrozen.jsh.base;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.cqfrozen.jsh.http.SupportHttps;
import com.cqfrozen.jsh.widget.RefreshLayout;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseValue.setInit(this);//初始化全部变量
        RefreshLayout.setInit();//下拉刷新控件初始化
        createUIL();
        SupportHttps.setInit();//初始化https
    }

    private void createUIL() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration defaultconfig = new ImageLoaderConfiguration.Builder(getBaseContext())
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .writeDebugLogs()
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(defaultconfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return super.getResources();
    }
}
