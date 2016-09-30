package com.cqfrozen.jsh.volleyhttp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.activity.GoodDetailResultInfo;
import com.cqfrozen.jsh.activity.MainActivity;
import com.cqfrozen.jsh.cart.CartResultInfo;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.entity.CartCountInfo;
import com.cqfrozen.jsh.entity.CategoryInfo;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.GoodsResultInfo;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.entity.HomeNotifyInfo;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.MD5Util;
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

    /**
     * 商品分类数据
     */
    public static void goodsType(HttpForVolley http,  Integer which, String area_id, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/goodstype/area_id/" + area_id;
        Type type = new TypeToken<List<CategoryInfo>>(){}.getType();
        toBean(GET, http, which, null, url, myHttpResult, type);
    }

    /**
     * 根据商品类型id获取相对应的商品数据
     */
    public static void goodstypeList(HttpForVolley http, Integer which, String area_id, int page,
                                     String g_type_id, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/goodstypelist/area_id/" + area_id +
                "/page/" + page +"/g_type_id/" + g_type_id;
        Type type = new TypeToken<GoodsResultInfo>(){}.getType();
        toBean(GET, http, which, null, url, myHttpResult, type);
    }

    /**
     * 获取消息通知
     */
    public static void noticeList(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Home/noticelist/area_id/5";
        Type type = new TypeToken<List<HomeNotifyInfo>>(){}.getType();
        toBean(GET, http, which, null, url, myHttpResult, type);
    }

    /**
     * 用户登陆
     */
    public static void userLogin(HttpForVolley http,Integer which, String mobile_num, String password,
                                 MyHttpResult myHttpResult) {
        String url = SERVER + "User/login";
        params.clear();
        params.put("mobile_num", mobile_num);
        params.put("pass_word", MD5Util.encodeMD5(password));
        Type type = new TypeToken<SigninInfo>(){}.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 添加购物车要调用的拦截接口，如果用户没审核
     */
    public static void addcart(HttpForVolley http, Integer which, Long g_id, String area_id,
                               int count, HttpForVolley.HttpTodo
                                       httpTodo) {
        String url = SERVER + "Cart/addcart";
        params.clear();
//        Log.d("UserInfoData", g_id + ":" + area_id  + ":" + count + ":" + MyApplication.token);
        params.put("g_id", g_id + "");
        params.put("area_id", area_id + "");
        params.put("count", count + "");
        params.put("token", MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 查询购物车数据
     */
    public static void queryCart(HttpForVolley http, Integer which, int page, int area_id, MyHttpResult myHttpResult) {
        String url = SERVER + "Cart/querycart";
        params.clear();
        params.put("page", page + "");
        params.put("area_id", area_id + "");
        params.put("token", MyApplication.token);
        Type type = new TypeToken<CartResultInfo>() {
        }.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 修改购物车数量
     */
    public static void editCount(HttpForVolley http, Integer which, Long c_id, int area_id, int count, HttpForVolley.HttpTodo
            httpTodo) {
        String url = SERVER + "Cart/editcount";
        params.clear();
        Log.d("UserInfoData", c_id + ":" + 5  + ":" + count + ":" + MyApplication.token);
        params.put("c_id", c_id + "");
        //TODO 改5为area_id
        params.put("area_id", 5 + "");
        params.put("count", count + "");
        params.put("token", MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 删除购物车商品
     */
    public static void deleteCart(HttpForVolley http, Integer which, Long c_id,  HttpForVolley.HttpTodo
            httpTodo) {
        String url = SERVER + "Cart/deletecart";
        params.clear();
        params.put("c_id", c_id + "");
        params.put("token", MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 查询购物车商品数量
     */
    public static void cartCount(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Cart/querycartcount";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<CartCountInfo>() {
        }.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 根据商品id查询商品详情页数据
     */
    public static void ginfo(HttpForVolley http, Integer which, Long g_id, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/ginfo";
        params.clear();
        params.put("g_id", g_id + "");
        params.put("area_id", 5 + "");
        params.put("token", MyApplication.token);
        Type type = new TypeToken<GoodDetailResultInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
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
