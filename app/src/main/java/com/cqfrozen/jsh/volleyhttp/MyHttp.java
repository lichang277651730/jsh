package com.cqfrozen.jsh.volleyhttp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.activity.LoginActivity;
import com.cqfrozen.jsh.activity.MainActivity;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.SPUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MyHttp {

    private static final String SERVER = "http://test.cqfrozen.com/api/index.php/";
    private static final int GET = Request.Method.GET;
    private static final int POST = Request.Method.POST;

    private static HashMap<String, String> params = new HashMap<String, String>();

    private MyHttp(){}

    /**
     * 首页banner数据
     */
    public static void homeBanner(HttpForVolley http, Integer which,
                                  MyHttpResult myHttpResult) {
        String url = SERVER + "Home/banner/area_id/5";
        Type type = new TypeToken<List<HomeBannerInfo>>(){}.getType();
        toBean(GET, http, which, null, url, myHttpResult, type);
    }

    /**
     * 首页特价推荐商品
     */
    public static void homePriceGoods(HttpForVolley http, Integer which, String id, MyHttpResult myHttpResult) {
        String url = SERVER + "Home/homegoods/area_id/5/type/" + id;
        Type type = new TypeToken<List<GoodsInfo>>(){}.getType();
        toBean(GET, http, which, null, url, myHttpResult, type);
    }

    private static void toBean(int method, final HttpForVolley http, Integer which,
                               HashMap<String, String> httpMap, String url,
                               final MyHttpResult myHttpResult, final Type bean) {
        http.goTo(method, which, httpMap, url, new HttpForVolley.HttpTodo(){

            @Override
            public void httpTodo(Integer which, JSONObject response) {
                //统一处理登录逻辑  code 1请求失败  2 登录失败  0请求成功s
                int code = response.optInt("code", 1);
                Log.d("codeInt", code + "");
                if(code == 2 && (http.getContext().getClass() != MainActivity.class)){
                    Context context = http.getContext();
                    Toast.makeText(context, "登录失效，请重新登录", Toast.LENGTH_SHORT).show();
                    MyApplication.userInfo = null;
                    MyApplication.token = "";
                    SPUtils.setToken("");
                    context.startActivity(new Intent(context, LoginActivity.class));
                }

                Object data = null;
                if(bean != null && code ==  0){
                    data = BaseValue.gson.fromJson(response.optString("data"), bean);
                }

                if(bean != null && code == 0 && data == null){
                    code = 1;
                }

                if(myHttpResult != null){
                    myHttpResult.httpResult(which, code, response.optString("msg", "发生错误"), data);
                }
            }

        });
    }

    public interface MyHttpResult{
        void httpResult(Integer which, int code, String msg, Object bean);
    }

}
