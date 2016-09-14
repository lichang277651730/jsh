package com.cqfrozen.jsh.okhttp;

import com.cqfrozen.jsh.entity.HomeBannerInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/13.
 */
public class MyOkHttp {

    private static final String SERVER = "http://192.168.1.110/dsdapi/index.php/";
    private static Map<String, String> params = new HashMap<>();
    private static OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    private MyOkHttp(){}

    /**
     * 首页轮播条
     * @param callback
     */
    public static void homeBanners(BaseCallback<List<HomeBannerInfo>> callback){
        String url = SERVER + "Home/index";
        httpHelper.doGet(url, callback);
    }
}
