package com.cqfrozen.jsh.volleyhttp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.activity.MainActivity;
import com.cqfrozen.jsh.cart.CartResultInfo;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.entity.AddressInfo;
import com.cqfrozen.jsh.entity.CartCountInfo;
import com.cqfrozen.jsh.entity.CartNotifyInfo;
import com.cqfrozen.jsh.entity.CategoryInfo;
import com.cqfrozen.jsh.entity.GoodDetailResultInfo;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.GoodsResultInfo;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.entity.HomeNotifyInfo;
import com.cqfrozen.jsh.entity.LocationInfo;
import com.cqfrozen.jsh.entity.SearchKwdInfo;
import com.cqfrozen.jsh.entity.ShopInfo;
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
     * 商品搜索
     */
    public static void goodsSearch(HttpForVolley http,Integer which, int page, String k_w, int px_v,
                                 int asc_desc, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/goodssearch";
        params.clear();
        params.put("page", page + "");
        params.put("k_w", k_w);
        params.put("px_v", px_v + "");
        params.put("asc_desc", asc_desc + "");
        params.put("token", MyApplication.token);
        Type type = new TypeToken<GoodsResultInfo>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 热门搜索关键字
     */
    public static void hotkw(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/hotkw";
        Type type = new TypeToken<List<SearchKwdInfo>>(){}.getType();
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
    public static void editCount(HttpForVolley http, Integer which, String c_id, int area_id, int count, HttpForVolley.HttpTodo
            httpTodo) {
        String url = SERVER + "Cart/editcount";
        params.clear();
        //TODO 删除log
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
    public static void deleteCart(HttpForVolley http, Integer which, int type, String c_id,  HttpForVolley.HttpTodo
            httpTodo) {
        String url = SERVER + "Cart/deletecart";
        params.clear();
        if(type == 1){
            params.put("c_id", c_id + "");
        }else if(type == 2){
            params.put("c_id", "");
        }else if(type == 3){
            params.put("c_id", c_id + "");
        }
        params.put("type", type + "");
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
        Log.d("ginfoginfo", g_id + ":" + 5  + ":" + MyApplication.token);
        Type type = new TypeToken<GoodDetailResultInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 获取定位区域数据列表
     */
    public static void areaList(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Public/arealist";
        Type type = new TypeToken<List<LocationInfo>>() {
        }.getType();
        toBean(GET, http, which, null, url, myHttpResult, type);
    }

    /**
     * 购物车提示消息，购物车运费信息
     */
    public static void freightTips(HttpForVolley http, Integer which,  MyHttpResult myHttpResult) {
        String url = SERVER + "Cart/freighttips";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<CartNotifyInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 添加 或 取消常用采购
     */
    public static void addCancelComm(HttpForVolley http, Integer which, int type, Long g_id, HttpForVolley.HttpTodo
            httpTodo) {
        String url = SERVER + "Goods/addcancelcomm";
        params.clear();
        params.put("type", type + "");
        params.put("g_id", g_id + "");
        params.put("token", MyApplication.token);
        http.goTo(GET, which, params, url, httpTodo);
    }

    /**
     * 查看常用采购商品
     */
    public static void commonGoodsList(HttpForVolley http, Integer which, int page, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/commongoodslist";
        params.clear();
        params.put("page", page + "");
        params.put("token", MyApplication.token);
        Type type = new TypeToken<GoodsResultInfo>() {
        }.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 查看地址列表
     */
    public static void addressList(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Personal/addresslist";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<List<AddressInfo>>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 查看商铺列表
     */
    public static void storeList(HttpForVolley http, Integer which,  MyHttpResult myHttpResult) {
        String url = SERVER + "Personal/storelist";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<List<ShopInfo>>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 请求验证码：
     * type = 1 注册请求
     */
    public static void sendCode(HttpForVolley http, Integer which, int type, String mobile_num,  HttpForVolley.HttpTodo
            httpTodo) {
        String url = SERVER + "User/sendcode";
        params.clear();
        params.put("type", type + "");
        params.put("mobile_num", mobile_num);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 注册
     */
    public static void register(HttpForVolley http, Integer which, String mobile_num, String password,
                                String store_name, String contacts, String area_id, String address,
                                String msg_code, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "User/register";
        params.clear();
        params.put("mobile_num", mobile_num);
        params.put("password", MD5Util.encodeMD5(password));
        params.put("store_name", store_name);
        params.put("contacts", contacts);
        params.put("area_id", area_id);
        params.put("address", address);
        params.put("msg_code", msg_code);
//        Log.d("register_params", "mobile_num:"+ mobile_num + "," +
//                "password:"+ password + "," +
//                "store_name:"+ store_name + "," +
//                "contacts:"+ contacts + "," +
//                "area_id:"+ area_id + "," +
//                "address:"+ address + "," +
//                "msg_code:"+ msg_code);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 用户修改密码
     */
    public static void pd(HttpForVolley http, Integer which, String pass_word, String new_pass_word, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "User/pd";
        params.clear();
        params.put("pass_word", MD5Util.encodeMD5(pass_word));
        params.put("new_pass_word", MD5Util.encodeMD5(new_pass_word));
        params.put("token", MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 添加收货地址
     */
    public static void addAddress(HttpForVolley http,Integer which, String china_name,
                                   String mobile_num, String address, int is_default, String s_id,
                                   String area_id,  HttpForVolley.HttpTodo httpTodo) {

        String url = SERVER + "Personal/addraddress";
        params.clear();
        params.put("china_name", china_name);
        params.put("mobile_num", mobile_num);
        params.put("address", address);
        params.put("s_id", s_id);
        params.put("area_id", area_id);
        params.put("is_default", is_default + "");
        params.put("token", MyApplication.token);
        Log.d("addAddress_params", "china_name:"+ china_name + "," +
                "mobile_num:"+ mobile_num + "," +
                "address:"+ address + "," +
                "s_id:"+ s_id + "," +
                "area_id:"+ area_id + "," +
                "is_default:"+ is_default + "," +
                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
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
