package com.cqfrozen.jsh.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.cqfrozen.jsh.MainActivity;
import com.cqfrozen.jsh.MyApplication;
import com.cqfrozen.jsh.activity.LoginActivity;
import com.cqfrozen.jsh.base.BaseValue;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
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

    public static void homeBanner(HttpForVolley http, Integer which,
                                  MyHttpResult myHttpResult) {
        String url = SERVER + "Home/index";
        Type type = new TypeToken<List<HomeBannerInfo>>(){}.getType();
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
