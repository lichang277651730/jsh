package com.cqfrozen.jsh.volleyhttp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.cart.CartResultInfo;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.entity.AddressInfo;
import com.cqfrozen.jsh.entity.AppraiseInfo;
import com.cqfrozen.jsh.entity.AreaStreetInfo;
import com.cqfrozen.jsh.entity.CartCountInfo;
import com.cqfrozen.jsh.entity.CartNotifyInfo;
import com.cqfrozen.jsh.entity.CategoryInfo;
import com.cqfrozen.jsh.entity.CommentResultInfo;
import com.cqfrozen.jsh.entity.FansResultInfo;
import com.cqfrozen.jsh.entity.GoodDetailResultInfo;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.GoodsResultInfo;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.entity.HomeNotifyInfo;
import com.cqfrozen.jsh.entity.HttpUrlInfo;
import com.cqfrozen.jsh.entity.HuibiResultInfo;
import com.cqfrozen.jsh.entity.LocationInfo;
import com.cqfrozen.jsh.entity.MyFansPageInfo;
import com.cqfrozen.jsh.entity.OrderBuyResultInfo;
import com.cqfrozen.jsh.entity.OrderDetailInfo;
import com.cqfrozen.jsh.entity.OrderDetailPageInfo;
import com.cqfrozen.jsh.entity.OrderInfo;
import com.cqfrozen.jsh.entity.OrderResultInfo;
import com.cqfrozen.jsh.entity.SearchKwdInfo;
import com.cqfrozen.jsh.entity.ShopInfo;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.entity.UpdateInfo;
import com.cqfrozen.jsh.entity.UserInfo;
import com.cqfrozen.jsh.entity.UserTypeInfo;
import com.cqfrozen.jsh.main.MainActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.MD5Util;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.util.SignUtil;
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
    private static final int p_type = 1;

    private static HashMap<String, String> params = new HashMap<String, String>();

    private MyHttp(){}

    /**
     * 首页banner数据
     */
    public static void homeBanner(HttpForVolley http, Integer which,
                                  MyHttpResult myHttpResult) {
        String url = SERVER + "Home/banner";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<List<HomeBannerInfo>>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 首页特价推荐商品
     */
    public static void homePriceGoods(HttpForVolley http, Integer which, String type_id, MyHttpResult myHttpResult) {
        String url = SERVER + "Home/homegoods";
        params.clear();
        params.put("token", MyApplication.token);
        params.put("type", type_id);
        Type type = new TypeToken<List<GoodsInfo>>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 商品分类数据
     */
    public static void goodsType(HttpForVolley http,  Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/goodstype";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<List<CategoryInfo>>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 根据商品类型id获取相对应的商品数据
     */
    public static void goodstypeList(HttpForVolley http, Integer which, int page,
                                     String g_type_id, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/goodstypelist";
        params.clear();
        params.put("page", page + "");
        params.put("g_type_id", g_type_id + "");
        params.put("token", MyApplication.token);
        Type type = new TypeToken<GoodsResultInfo>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
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
        Log.d("addAddress_params", "page:"+ page + "," +
                "k_w:"+ k_w + "," +
                "px_v:"+ px_v + "," +
                "asc_desc:"+ asc_desc + "," +
                "token:"+ MyApplication.token);
        Type type = new TypeToken<GoodsResultInfo>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 热门搜索关键字
     */
    public static void hotkw(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/hotkw";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<List<SearchKwdInfo>>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 获取消息通知
     */
    public static void noticeList(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Home/noticelist";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<List<HomeNotifyInfo>>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 刷新token
     */
    public static void refreshToken(HttpForVolley http, Integer which, String token, MyHttpResult myHttpResult) {
        String url = SERVER + "User/refreshtoken";
        params.clear();
        params.put("token", token);
//        Log.d("addAddress_params", "token:"+ token);
        toBean(GET, http, which, params, url, myHttpResult, SigninInfo.class);
    }


    /**
     * 用户登陆
     */
    public static void userLogin(HttpForVolley http, Integer which, String mobile_num, String password,
                                 MyHttpResult myHttpResult) {
        String url = SERVER + "User/login";
        params.clear();
        params.put("mobile_num", mobile_num);
        params.put("p_type", 1 + "");//android端登陆
        params.put("pass_word", MD5Util.encodeMD5(password));
//        Log.d("addAddress_params", "mobile_num:"+ mobile_num + "," +
//                "p_type:"+ 1 + "," +
//                "pass_word:"+ MD5Util.encodeMD5(password));
        Type type = new TypeToken<SigninInfo>(){}.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 获取用户信息
     */
    public static void user(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Personal/user";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<UserInfo>(){}.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }


    /**
     * 添加购物车要调用的拦截接口，如果用户没审核
     */
    public static void addcart(HttpForVolley http, Integer which, Long g_id,
                               int count, HttpForVolley.HttpTodo
                                       httpTodo) {
        String url = SERVER + "Cart/addcart";
        params.clear();
        params.put("g_id", g_id + "");
        params.put("count", count + "");
        params.put("token", MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 查询购物车数据
     */
    public static void queryCart(HttpForVolley http, Integer which, int page, MyHttpResult myHttpResult) {
        String url = SERVER + "Cart/querycart";
        params.clear();
        params.put("page", page + "");
        params.put("token", MyApplication.token);
        Type type = new TypeToken<CartResultInfo>() {
        }.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 修改购物车数量
     */
    public static void editCount(HttpForVolley http, Integer which, String c_id, int count, HttpForVolley.HttpTodo
            httpTodo) {
        String url = SERVER + "Cart/editcount";
        params.clear();
        params.put("c_id", c_id + "");
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
    public static void queryCartCount(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
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
        params.put("token", MyApplication.token);
        Type type = new TypeToken<GoodDetailResultInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 对应g_id商品的评价列表
     */
    public static void pjList(HttpForVolley http, Integer which, int page, Long g_id, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/pjlist";
        params.clear();
        params.put("g_id", g_id + "");
        params.put("page", page + "");
        params.put("token", MyApplication.token);
        Type type = new TypeToken<CommentResultInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }


    /**
     * 获取定位区域数据列表
     */
    public static void areaList(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Public/arealist";
        params.clear();
        params.put("token", MyApplication.token);
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
     * 查看商铺列表
     */
    public static void shopList(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Personal/storelist";
        params.clear();
        params.put("token", MyApplication.token);
//        Log.d("shopListtoken", MyApplication.token);
        Type type = new TypeToken<List<ShopInfo>>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 删除地址
     */
    public static void deleteStore(HttpForVolley http, Integer which, String s_id, HttpForVolley.HttpTodo
            httpTodo) {
        String url = SERVER + "Personal/deletestore";
        params.clear();
        params.put("s_id", s_id);
        params.put("token", MyApplication.token);
        http.goTo(GET, which, params, url, httpTodo);
    }

    /**
     * 增加店铺
     */
    public static void addStore(HttpForVolley http, Integer which, String china_name, String mobile_num,
                                String st_id, String area_id, String store_name, String address,
                                HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Personal/addstore";
        params.clear();
        params.put("china_name", china_name);
        params.put("mobile_num", mobile_num);
        params.put("address", address);
        params.put("st_id", st_id);
        params.put("area_id", area_id);
        params.put("store_name", store_name);
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params", "china_name:"+ china_name + "," +
//                "mobile_num:"+ mobile_num + "," +
//                "address:"+ address + "," +
//                "s_id:"+ s_id + "," +
//                "area_id:"+ area_id + "," +
//                "st_id:"+ st_id + "," +
//                "is_default:"+ is_default + "," +
//                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 修改店铺
     */
    public static void updateStore(HttpForVolley http, Integer which, String s_id, String china_name, String mobile_num,
                                String st_id, String area_id, String store_name, String address,
                                HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Personal/updatestore";
        params.clear();
        params.put("china_name", china_name);
        params.put("mobile_num", mobile_num);
        params.put("address", address);
        params.put("st_id", st_id);
        params.put("s_id", s_id);
        params.put("area_id", area_id);
        params.put("store_name", store_name);
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params", "china_name:"+ china_name + "," +
//                "mobile_num:"+ mobile_num + "," +
//                "address:"+ address + "," +
//                "s_id:"+ s_id + "," +
//                "area_id:"+ area_id + "," +
//                "st_id:"+ st_id + "," +
//                "is_default:"+ is_default + "," +
//                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
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
     * 获取 商户类型
     */
    public static void userTypeList(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Public/usertypelist";
        params.clear();
        params.put("token", MyApplication.token);
        Type type = new TypeToken<List<UserTypeInfo>>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 注册
     */
    public static void register(HttpForVolley http, Integer which, String mobile_num, String password,
                                String store_name, String contacts, String area_id, String st_id, String u_t_id,
                                String address, String msg_code, String code, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "User/register";
        params.clear();
        params.put("mobile_num", mobile_num);
        params.put("password", MD5Util.encodeMD5(password));
        params.put("store_name", store_name);
        params.put("contacts", contacts);
        params.put("area_id", area_id);
        params.put("st_id", st_id);
        params.put("u_t_id", u_t_id);
        params.put("address", address);
        params.put("msg_code", msg_code);
        params.put("code", code);
        params.put("p_type", 1 + "");//android端注册
//        Log.d("register_params", "mobile_num:"+ mobile_num + "," +
//                "password:"+ password + "," +
//                "store_name:"+ store_name + "," +
//                "contacts:"+ contacts + "," +
//                "area_id:"+ area_id + "," +
//                "st_id:"+ st_id + "," +
//                "u_t_id:"+ u_t_id + "," +
//                "address:"+ address + "," +
//                "code:"+ code + "," +
//                "msg_code:"+ msg_code);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 退出
     */
    public static void loginout(HttpForVolley http, Integer which, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "User/login_out";
        params.clear();
        params.put("p_type", p_type + "");
        params.put("token", MyApplication.token);
        //        Log.d("register_params", "p_type:"+ p_type + "," +
//                "token:"+ token + "," +);
        http.goTo(POST, which, params, url, httpTodo);
    }


    /**
     * 用户修改密码
     */
    public static void pd(HttpForVolley http, Integer which, String pass_word,
                          String new_pass_word, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "User/pd";
        params.clear();
        params.put("pass_word", MD5Util.encodeMD5(pass_word));
        params.put("new_pass_word", MD5Util.encodeMD5(new_pass_word));
        params.put("token", MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 用户在登陆
     */
    public static void editPwd(HttpForVolley http, Integer which, String mobile_num, String msg_code,
                               String pass_word, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "User/pd";
        params.clear();
        params.put("pass_word", MD5Util.encodeMD5(pass_word));
        params.put("mobile_num", mobile_num);
        params.put("msg_code", msg_code);
        http.goTo(POST, which, params, url, httpTodo);
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
     * 添加收货地址
     */
    public static void addAddress(HttpForVolley http, Integer which, String china_name,
                                   String mobile_num, String address, int is_default, String s_id,
                                   String st_id,String area_id, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Personal/addraddress";
        params.clear();
        params.put("china_name", china_name);
        params.put("mobile_num", mobile_num);
        params.put("address", address);
        params.put("s_id", s_id);
        params.put("st_id", st_id);
        params.put("area_id", area_id);
        params.put("is_default", is_default + "");
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params", "china_name:"+ china_name + "," +
//                "mobile_num:"+ mobile_num + "," +
//                "address:"+ address + "," +
//                "s_id:"+ s_id + "," +
//                "area_id:"+ area_id + "," +
//                "st_id:"+ st_id + "," +
//                "is_default:"+ is_default + "," +
//                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 修改指定id的地址
     */
    public static void editrAddress(HttpForVolley http, Integer which, String a_id, String china_name,
                                    String mobile_num, String address, int is_default, String s_id,
                                    String st_id, String area_id,  HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Personal/editraddress";
        params.clear();
        params.put("a_id", a_id);
        params.put("china_name", china_name);
        params.put("mobile_num", mobile_num);
        params.put("address", address);
        params.put("s_id", s_id);
        params.put("st_id", st_id);
        params.put("area_id", area_id);
        params.put("is_default", is_default + "");
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params", "china_name:"+ china_name + "," +
//                "a_id:"+ a_id + "," +
//                "mobile_num:"+ mobile_num + "," +
//                "address:"+ address + "," +
//                "s_id:"+ s_id + "," +
//                "area_id:"+ area_id + "," +
//                "st_id:"+ st_id + "," +
//                "is_default:"+ is_default + "," +
//                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 删除地址
     */
    public static void deleterAddress(HttpForVolley http, Integer which, String a_id, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Personal/deleteraddress";
        params.clear();
        params.put("a_id", a_id);
        params.put("token", MyApplication.token);
        http.goTo(GET, which, params, url, httpTodo);
    }


    /**
     * 设置默认地址
     */
    public static void setDefault(HttpForVolley http, Integer which, String a_id, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Personal/setdefault";
        params.clear();
        params.put("a_id", a_id);
        params.put("token", MyApplication.token);
//                Log.d("addAddress_params", "a_id:"+ a_id + "," +
//                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 获取区域街道信息
     */
    public static void searchStreet(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Personal/searchstreet";
        Type type = new TypeToken<List<AreaStreetInfo>>() {
        }.getType();
        toBean(GET, http, which, null, url, myHttpResult, type);
    }


    /**
     * 购物车点击去结算
     */
    public static void settlement(HttpForVolley http, Integer which, String cartdata, long timestamp,
                                 MyHttpResult myHttpResult) {
        String url = SERVER + "Order/settlement";
        params.clear();
        params.put("cartdata", cartdata);
        params.put("timestamp", timestamp + "");
        params.put("token", MyApplication.token);
        String sign = SignUtil.getOrderSignInfo(cartdata, timestamp, MyApplication.token);
        params.put("sign", sign);
//        Log.d("addAddress_params", "cartdata:"+ cartdata + "," +
//                "timestamp:"+ timestamp + "," +
//                "sign:"+ sign + "," +
//                "token:"+  MyApplication.token);
        Type type = new TypeToken<OrderInfo>() {
        }.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 去付款，即添加订单
     */
    public static void addOrder(HttpForVolley http, Integer which, String cartdata, long timestamp,
                                String a_id, String msg_content, int is_use_hb,
                                int pay_mode, MyHttpResult myHttpResult) {
        String url = SERVER + "Order/addorder";
        params.clear();
        params.put("cartdata", cartdata);
        params.put("timestamp", timestamp + "");
        params.put("a_id", a_id);
        params.put("msg_content", msg_content);
        params.put("is_use_hb", is_use_hb + "");
        params.put("pay_mode", pay_mode + "");
        params.put("ptype", p_type + "");//0微信 1 android, 2 ios，3其他
        params.put("token", MyApplication.token);
        String sign = SignUtil.getOrderBuySignInfo(a_id, cartdata, is_use_hb, msg_content, pay_mode, p_type, timestamp, MyApplication.token);
        params.put("sign", sign);
//        Log.d("addAddress_params", "a_id:"+ a_id + "," +
//                "cartdata:"+ cartdata + "," +
//                "is_use_hb:"+ is_use_hb + "," +
//                "msg_content:"+ msg_content + "," +
//                "pay_mode:"+ pay_mode + "," +
//                "p_type:"+ p_type + "," +
//                "timestamp:"+ timestamp + "," +
//                "token:"+  MyApplication.token +
//                "sign:"+ sign);
        Type type = new TypeToken<OrderBuyResultInfo>() {
        }.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 查询订单
     */
    public static void searchOrder(HttpForVolley http, Integer which, int tv, int page, MyHttpResult myHttpResult) {
        String url = SERVER + "Order/searchorder";
        params.clear();
        params.put("tv", tv + "");
        params.put("page", page + "");
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params", "tv:"+ tv + "," +
//                "page:"+ page + "," +
//                "token:"+  MyApplication.token);
        Type type = new TypeToken<OrderResultInfo>() {
        }.getType();
        toBean(POST, http, which, params, url, myHttpResult, type);
    }

    /**
     * 通过订单id查询订单
     */
    public static void orderSuccess(HttpForVolley http, Integer which, String o_id,
                                    MyHttpResult myHttpResult) {
        String url = SERVER + "Order/ordersuccess";
        params.clear();
        params.put("o_id", o_id);
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params", "o_id:"+ o_id + "," +
//                "token:"+  MyApplication.token);
        Type type = new TypeToken<OrderDetailInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }


    /**
     * 订单详情页
     */
    public static void orderInfo(HttpForVolley http, Integer which, String o_id,  MyHttpResult myHttpResult) {
        String url = SERVER + "Order/orderinfo";
        params.clear();
        params.put("o_id", o_id);
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params", "o_id:"+ o_id + "," +
//                "token:"+  MyApplication.token);
        Type type = new TypeToken<OrderDetailPageInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, type);
    }

    /**
     * 取消订单
     */
    public static void cancelOrder(HttpForVolley http, Integer which, String o_id,  HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Order/cancelorder";
        params.clear();
        params.put("o_id", o_id);
        params.put("token", MyApplication.token);
//                Log.d("addAddress_params", "o_id:"+ o_id + "," +
//                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 删除订单
     */
    public static void orderDelete(HttpForVolley http, Integer which, String o_id, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Order/orderdelete";
        params.clear();
        params.put("o_id", o_id);
        params.put("token", MyApplication.token);
//                Log.d("addAddress_params", "o_id:"+ o_id + "," +
//                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 确认收货
     */
    public static void orderConfirm(HttpForVolley http, Integer which, String o_id, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Order/orderconfirm";
        params.clear();
        params.put("o_id", o_id);
        params.put("token", MyApplication.token);
                Log.d("addAddress_params", "o_id:"+ o_id + "," +
                "token:"+  MyApplication.token);
        http.goTo(GET, which, params, url, httpTodo);
    }

    /**
     * 填写评价页面调用接口
     */
    public static void pj(HttpForVolley http, Integer which, String o_id, MyHttpResult myHttpResult) {
        String url = SERVER + "Goods/pj";
        params.clear();
        params.put("o_id", o_id);
        params.put("token", MyApplication.token);
        Log.d("addAddress_params", "o_id:"+ o_id + "," +
                "token:"+  MyApplication.token);
        Type beanType = new TypeToken<List<AppraiseInfo>>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, beanType);
    }

    /**
     * 添加评价
     */
    public static void addPJ(HttpForVolley http, Integer which, String o_id, String star_count_list,
                             String order_info_id_list, String goods_id_list, String content_list,
                             int is_anonymou, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Goods/addpj";
        params.clear();
        params.put("o_id", o_id);
        params.put("star_count_list", star_count_list);
        params.put("order_info_id_list", order_info_id_list);
        params.put("goods_id_list", goods_id_list);
        params.put("content_list", content_list);
        params.put("is_anonymou", is_anonymou + "");
        params.put("token", MyApplication.token);
        Log.d("addAddress_params", "o_id:"+ o_id + "," +
                "star_count_list:"+ star_count_list + "," +
                "order_info_id_list:"+ order_info_id_list + "," +
                "goods_id_list:"+ goods_id_list + "," +
                "content_list:"+ content_list + "," +
                "is_anonymou:"+ is_anonymou + "," +
                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 汇币明细
     * type = 1 收入
     * type = 2 支出
     */
    public static void searchHBinfo(HttpForVolley http, Integer which, int type, int page, MyHttpResult myHttpResult) {
        String url = SERVER + "HbPersonal/searchhbinfo";
        params.clear();
        params.put("type", type + "");
        params.put("page", page + "");
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params", "page:"+ page + "," +
//                "type:"+ type + "," +
//                "token:"+  MyApplication.token);
        Type beanType = new TypeToken<HuibiResultInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, beanType);
    }

    /**
     * 我的兄弟伙上半页面信息
     */
    public static void myFans(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "HbPersonal/myfans";
        params.clear();
        params.put("p_type", p_type + "");
        params.put("token", MyApplication.token);
        Type beanType = new TypeToken<MyFansPageInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, beanType);
    }

    /**
     * 一级粉丝 二级粉丝
     */
    public static void searchFans(HttpForVolley http, Integer which, int page, int level, MyHttpResult myHttpResult) {
        String url = SERVER + "HbPersonal/searchfans";
        params.clear();
        params.put("page", page + "");
        params.put("level", level + "");
        params.put("token", MyApplication.token);
//        Log.d("addAddress_params","page:"+  page +
//                  "level:"+  level +
//                  "token:"+  MyApplication.token);
        Type beanType = new TypeToken<FansResultInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, beanType);
    }

    /**
     * 上传头像
     */
    public static void updateHead(HttpForVolley http, Integer which, String path, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Personal/updatehead";
        params.clear();
        params.put("token", MyApplication.token);
        http.postBase64(POST, null, params, path, url, httpTodo);
    }

    /**
     * 获取Url链接
     */
    public static void searchHttpUrl(HttpForVolley http, Integer which, MyHttpResult myHttpResult) {
        String url = SERVER + "Public/searchhttpurl";
        Type beanType = new TypeToken<List<HttpUrlInfo>>() {
        }.getType();
        toBean(GET, http, which, null, url, myHttpResult, beanType);
    }

    /**
     * 版本升级接口
     */
    public static void appVersion(HttpForVolley http, Integer which, int version_num, MyHttpResult myHttpResult) {
        String url = SERVER + "Public/appversion";
        params.clear();
        params.put("version_num", version_num + "");
        params.put("p_type", p_type + "");
        params.put("token", MyApplication.token);
        Type beanType = new TypeToken<UpdateInfo>() {
        }.getType();
        toBean(GET, http, which, params, url, myHttpResult, beanType);
    }

    /**
     * 更改手机号码
     */
    public static void updateMobile(HttpForVolley http, Integer which, String mobile_num, String
            code, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "Personal/updatemobile";
        params.clear();
        params.put("mobile_num", mobile_num);
        params.put("code", code);
        params.put("token", MyApplication.token);
        Log.d("addAddress_params", "mobile_num:"+ mobile_num + "," +
                "code:"+ code + "," +
                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }

    /**
     * 提交反馈意见
     */
    public static void feedBack(HttpForVolley http,Integer which, String content, HttpForVolley.HttpTodo httpTodo) {
        String url = SERVER + "User/feedback";
        params.clear();
        params.put("content", content);
        params.put("p_type", p_type + "");
        params.put("token", MyApplication.token);
        Log.d("addAddress_params", "content:"+ content + "," +
                "p_type:"+ p_type + "," +
                "token:"+  MyApplication.token);
        http.goTo(POST, which, params, url, httpTodo);
    }



    private static void toBean(int method, final HttpForVolley http, Integer which,
                               HashMap<String, String> httpMap, String url,
                               final MyHttpResult myHttpResult, final Type bean) {
        http.goTo(method, which, httpMap, url, new HttpForVolley.HttpTodo(){

            @Override
            public void httpTodo(Integer which, JSONObject response) {
//                Log.d("addAddress_params", response.toString());
                //统一处理登录逻辑  code 1请求失败  2 登录失败  0请求成功s
                int code = response.optInt("code", 1);
//                if(code == 2){
//                    refreshToken(http, null, SPUtils.getToken(), new MyHttpResult() {
//                        @Override
//                        public void httpResult(Integer which, int code, String msg, Object bean) {
//                            if(code != 0){
//                                return;
//                            }
//                            SigninInfo signinInfo = (SigninInfo) bean;
//                            MyApplication.signinInfo = signinInfo;
//                            MyApplication.token = signinInfo.getToken();
//                            SPUtils.setToken(signinInfo.getToken());
//                            user(http, null, new MyHttpResult() {
//                                @Override
//                                public void httpResult(Integer which, int code, String msg, Object bean) {
//                                    if(code != 0){
//                                        return;
//                                    }
//                                    MyApplication.userInfo = (UserInfo) bean;
//                                }
//                            });
//                        }
//                    });
//
//                }
                if(code == 3 && (http.getContext().getClass() != MainActivity.class)){
                    Context context = http.getContext();
                    Toast.makeText(context, "该账号在其他手机登陆过，需重新登录", Toast.LENGTH_SHORT).show();
                    MyApplication.userInfo = null;
                    MyApplication.token = "";
                    SPUtils.setToken("");
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return;
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
