package com.cqfrozen.jsh.center;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.common.util.PhotoPopupWindow;
import com.common.util.PhotoUtil;
import com.common.widget.MyHeadImageView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HttpUrlInfo;
import com.cqfrozen.jsh.entity.UserInfo;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.order.OrderListActivity;
import com.cqfrozen.jsh.util.ShortcutPop;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.cqfrozen.jsh.widget.BadgeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/12.
 * 个人中心页面 fragment
 */
public class MineFragment extends MyFragment implements View.OnClickListener{

    private static MineFragment fragment;
    private TextView tv_lookall;
    private ImageView iv_setting;
    private ImageView iv_shotcut;
    private TextView tv_address;
    private MyHeadImageView iv_head;
    private TextView tv_login;
    private TextView tv_name;
    private TextView tv_normal_buy;
    private TextView tv_shop;
    private TextView tv_phone;
    private TextView tv_verify;
    private TextView tv_huibi;
    private TextView tv_fans;
    private LinearLayout ll_huibi;
    private LinearLayout ll_fans;
    private LinearLayout ll_table1;
    private LinearLayout ll_table2;
    private LinearLayout ll_table3;
    private ImageView iv_table1;
    private ImageView iv_table2;
    private ImageView iv_table3;
    private BadgeView badgeView1;
    private BadgeView badgeView2;
    private BadgeView badgeView3;
    private ImageView iv_verify;
    private float hb_count_new;

    private List<HttpUrlInfo> httpUrlInfos = new ArrayList<HttpUrlInfo>();
    private Map<Integer, String> urlMap = new HashMap<>();
    private String huibi_rule_url = "";
    private String user_protocol_url = "";
    private String about_us_url = "";
    private String after_sale_url = "";
    private TextView tv_after_sale;
    private TextView tv_about_us;
    private PhotoUtil photoUtil;
    private PhotoPopupWindow photoPopupWindow;
    private TextView tv_server_phone;
    private UserInfo userInfo;
    private LinearLayout ll_server_phone;
    private Uri photoUri;

    public interface UrlType{
        int huibi_rule = 4;
        int user_protocol = 5;
        int about_us = 6;
        int after_sale = 7;
    }


    public static MineFragment getInstance(){
        if(fragment == null){
            fragment = new MineFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = this;
        if(view == null){
            view = inflater.inflate(R.layout.fragment_mine, null);
            initView();
            getUrlData();
        }
        return view;
    }

    private void initView() {
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        iv_shotcut = (ImageView) view.findViewById(R.id.iv_shotcut);
        iv_table1 = (ImageView) view.findViewById(R.id.iv_table1);
        iv_table2 = (ImageView) view.findViewById(R.id.iv_table2);
        iv_table3 = (ImageView) view.findViewById(R.id.iv_table3);
        iv_verify = (ImageView) view.findViewById(R.id.iv_verify);
        iv_head = (MyHeadImageView) view.findViewById(R.id.iv_head);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_lookall = (TextView) view.findViewById(R.id.tv_lookall);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_verify = (TextView) view.findViewById(R.id.tv_verify);
        ll_huibi = (LinearLayout) view.findViewById(R.id.ll_huibi);
        tv_huibi = (TextView) view.findViewById(R.id.tv_huibi);
        ll_fans = (LinearLayout) view.findViewById(R.id.ll_fans);
        ll_table1 = (LinearLayout) view.findViewById(R.id.ll_table1);
        ll_table2 = (LinearLayout) view.findViewById(R.id.ll_table2);
        ll_table3 = (LinearLayout) view.findViewById(R.id.ll_table3);
        tv_fans = (TextView) view.findViewById(R.id.tv_fans);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_shop = (TextView) view.findViewById(R.id.tv_shop);
        tv_after_sale = (TextView) view.findViewById(R.id.tv_after_sale);
        tv_about_us = (TextView) view.findViewById(R.id.tv_about_us);
        tv_normal_buy = (TextView) view.findViewById(R.id.tv_normal_buy);
        tv_server_phone = (TextView) view.findViewById(R.id.tv_server_phone);
        ll_server_phone = (LinearLayout) view.findViewById(R.id.ll_server_phone);

        tv_lookall.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        iv_shotcut.setOnClickListener(this);
        tv_shop.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_normal_buy.setOnClickListener(this);
        ll_huibi.setOnClickListener(this);
        ll_fans.setOnClickListener(this);
        ll_table1.setOnClickListener(this);
        ll_table2.setOnClickListener(this);
        ll_table3.setOnClickListener(this);
        tv_after_sale.setOnClickListener(this);
        tv_about_us.setOnClickListener(this);
        ll_server_phone.setOnClickListener(this);

        initBadgeViews();
    }

    private void initBadgeViews() {

        badgeView1 = new BadgeView(mActivity, iv_table1);
        badgeView2 = new BadgeView(mActivity, iv_table2);
        badgeView3 = new BadgeView(mActivity, iv_table3);
        badgeView1.setEnabled(false);
        badgeView2.setEnabled(false);
        badgeView3.setEnabled(false);
        badgeView1.setTextSize(10);
        badgeView2.setTextSize(10);
        badgeView3.setTextSize(10);
        badgeView1.setBadgeMargin(BaseValue.dp2px(0), 0);
        badgeView2.setBadgeMargin(BaseValue.dp2px(0), 0);
        badgeView3.setBadgeMargin(BaseValue.dp2px(0), 0);

    }

    //每次切换到个人中心fragment时调用此方法
    @Override
    public void onShow() {
        super.onShow();
        if(isLogined()){//已经登陆的用户，就初始化用户数据
            getUserDataFormServer();
        }else {//没有登陆就将页面置为没有登陆的状态
            showUnLogined();
        }
    }

    private void getUserDataFormServer() {
        MyHttp.user(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
//                    showToast(msg);
                    return;
                }
                userInfo = (UserInfo) bean;
                showLogined(userInfo);
            }
        });
    }

    private void showUnLogined() {
        //将登陆字符显示
        tv_login.setVisibility(View.VISIBLE);
        //将name隐藏
        tv_name.setVisibility(View.GONE);
        tv_huibi.setText(0.00 + "");
        tv_fans.setText(0 + "");
        tv_server_phone.setText("");
        tv_phone.setVisibility(View.INVISIBLE);
        tv_verify.setVisibility(View.INVISIBLE);
        tv_verify.setVisibility(View.INVISIBLE);
        iv_verify.setVisibility(View.INVISIBLE);
        badgeView1.hide();
        badgeView2.hide();
        badgeView3.hide();
        iv_head.setImageResource(R.mipmap.icon_mine_head_default);
    }

    private void showLogined(UserInfo userInfo) {
        hb_count_new = userInfo.hb_count;
        tv_login.setVisibility(View.GONE);
        tv_name.setVisibility(View.VISIBLE);
        tv_phone.setVisibility(View.VISIBLE);
        tv_verify.setVisibility(View.VISIBLE);
        tv_verify.setVisibility(View.VISIBLE);
        iv_verify.setVisibility(View.VISIBLE);
        if(userInfo.verify_status == 0 || userInfo.verify_status == 2){
            iv_verify.setImageResource(R.mipmap.icon_verify_no);
        }else if(userInfo.verify_status == 1){
            iv_verify.setImageResource(R.mipmap.icon_verify_yes);
        }
        tv_name.setText(userInfo.store_name);
        tv_phone.setText(userInfo.mobile_num);
        tv_verify.setText(userInfo.verify_name);
        tv_huibi.setText(userInfo.hb_count + "");
        tv_fans.setText(userInfo.inotal_fans_count + "");
        tv_server_phone.setText(userInfo.c_phone_num);

        badgeView1.setText(userInfo.df_count >= 100 ? "99+" : userInfo.df_count + "");
        badgeView2.setText(userInfo.ds_count >= 100 ? "99+" : userInfo.ds_count + "");
        badgeView3.setText(userInfo.dp_count >= 100 ? "99+" : userInfo.dp_count + "");

        if(userInfo.df_count == 0){
            badgeView1.hide();
        }else {
            badgeView1.show();
        }
        if(userInfo.ds_count == 0){
            badgeView2.hide();
        }else {
            badgeView2.show();
        }
        if(userInfo.dp_count == 0){
            badgeView3.hide();
        }else {
            badgeView3.show();
        }

        DisplayImageOptions build = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).showImageOnFail(R.mipmap.icon_mine_head_default)
                .showImageForEmptyUri(R.mipmap.icon_mine_head_default).build();
        ImageLoader.getInstance().displayImage(userInfo.head_url, iv_head, build);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting://设置
                if(needLogin()){
                    startActivity(new Intent(mActivity, SettingActivity.class));
                }
                break;
            case R.id.iv_shotcut:
                ShortcutPop.getInstance(mActivity).showPop(iv_shotcut);
                break;
            case R.id.tv_normal_buy://跳转至常用采购商品列表页面
                if(needLogin()){
                    startActivity(new Intent(getActivity(), NormalBuyActivity.class));
                }
                break;
            case R.id.tv_address://收货地址
                if(needLogin()){
                    startActivity(new Intent(mActivity, AddressListActivity.class));
                }
                break;
            case R.id.tv_shop://店铺管理
                if(needLogin()){
                    startActivity(new Intent(mActivity, ShopListActivity.class));
                }
                break;
            case R.id.iv_head:
            case R.id.tv_name:
                if(needLogin()){
                    photoUtil = new PhotoUtil(this, 3, 3);
                    photoPopupWindow = new PhotoPopupWindow(getActivity(), photoUtil);
                    photoPopupWindow.showpop(v);
                }
                break;
            case R.id.tv_login:
                needLogin();
                break;
            case R.id.ll_huibi://粮票列表
                if(needLogin()){
                    Intent intent = new Intent(mActivity, HuibiListActivity.class);
                    intent.putExtra("hb_count", hb_count_new);
                    intent.putExtra("url", huibi_rule_url);
                    startActivity(intent);
                }
                break;
            case R.id.ll_fans://粉丝列表
                if(needLogin()){
                    startActivity(new Intent(mActivity, FansListActity.class));
                }
                break;
            case R.id.tv_lookall://查看所有订单
                if(needLogin()){
                    Intent intent = new Intent(mActivity, OrderListActivity.class);
                    intent.putExtra("page_index", OrderListActivity.PAGE_ALL);
                    startActivity(intent);
                }
                break;
            case R.id.ll_table1://待付款
                if(needLogin()){
                    Intent intent = new Intent(mActivity, OrderListActivity.class);
                    intent.putExtra("page_index", OrderListActivity.PAGE_NO_PAY);
                    startActivity(intent);
                }
                break;
            case R.id.ll_table2://待收货
                if(needLogin()){
                    Intent intent = new Intent(mActivity, OrderListActivity.class);
                    intent.putExtra("page_index", OrderListActivity.PAGE_NO_RECEIVE);
                    startActivity(intent);
                }
                break;
            case R.id.ll_table3://待评价
                if(needLogin()){
                    Intent intent = new Intent(mActivity, OrderListActivity.class);
                    intent.putExtra("page_index", OrderListActivity.PAGE_NO_SAY);
                    startActivity(intent);
                }
                break;
            case R.id.tv_after_sale://售后规则
                if(needLogin()){
                    Intent intent = new Intent(mActivity, WebUrlActivity.class);
                    intent.putExtra("title", "售后规则");
                    intent.putExtra("url", after_sale_url);
                    startActivity(intent);
                }
                break;
            case R.id.tv_about_us://关于我们
                if(needLogin()){
                    Intent intent = new Intent(mActivity, WebUrlActivity.class);
                    intent.putExtra("title", "关于我们");
                    intent.putExtra("url", about_us_url);
                    startActivity(intent);
                }
                break;
            case R.id.ll_server_phone://关于我们
                if(needLogin()){
                    Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + userInfo.c_phone_num));
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void getUrlData() {
        MyHttp.searchHttpUrl(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
//                    showToast(msg);
                    return;
                }
                httpUrlInfos.addAll((List<HttpUrlInfo>)bean);
                if(httpUrlInfos.size() == 0){
                    return;
                }
                for(int i = 0; i < httpUrlInfos.size(); i++){
                    HttpUrlInfo httpUrlInfo = httpUrlInfos.get(i);
                    urlMap.put(httpUrlInfo.type, httpUrlInfo.http_url);
                }
                huibi_rule_url = urlMap.get(UrlType.huibi_rule);
                user_protocol_url = urlMap.get(UrlType.user_protocol);
                about_us_url = urlMap.get(UrlType.about_us);
                after_sale_url = urlMap.get(UrlType.after_sale);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        switch (requestCode) {
//            case ImageUtils.GET_IMAGE_BY_CAMERA:
//                //从拍照返回的结果  要考虑用户没有拍照，但是我们已经在数据库里已经创建了一条空数据了，我们要把它删除
//                if(resultCode == Activity.RESULT_CANCELED){//拍照取消，将已经存在数据库里的图片地址删除
//                    ImageUtils.deleteImageUri(getActivity(), ImageUtils.imageUriFromCamera);
//                }else{//拍照成功，将图片添加到页面的九宫格里
//                    ImageUtils.cropImage(getActivity(), ImageUtils.imageUriFromCamera);
//                }
//                break;
//            case ImageUtils.GET_IMAGE_FROM_PHONE://从相册放回的结果
//                photoUri = data.getData();
////                mImgUrls.add(data.getData());
////                updateImgs();
//                break;
//            case ImageUtils.CROP_IMAGE://裁剪返回的结果
//                if(resultCode != Activity.RESULT_CANCELED){//已经裁剪,将裁剪好的图片地址存在一个集合里面
//                    photoUri = ImageUtils.cropImageUri;
////                    mImgUrls.add(ImageUtils.cropImageUri);
////                    updateImgs();
//                }else{
//                    ImageUtils.deleteImageUri(getActivity(), ImageUtils.cropImageUri);
//                }
//                break;
//
//            default:
//                break;
//        }
//        Log.d("photoUriPath", photoUri.toString() + ":" + photoUri.getPath());
//        if (resultCode == Activity.RESULT_OK) {
//            MyHttp.updateHead(http, null, Uri2PathUtil.getImgFilePath(photoUri, getActivity()), new HttpForVolley.HttpTodo() {
//
//                @Override
//                public void httpTodo(Integer which, JSONObject response) {
//                    if (response.optInt("code", 1) != 0) {
//                        showToast("上传图片发生错误!");
//                        return;
//                    }
//                    showToast("修改头像成功!");
//                    String filename = response.optJSONObject("data").optString("head_url");
//                    ImageLoader.getInstance().displayImage(filename, iv_head);
//                    getUserInfo().head_url = filename;
//                }
//            });
        switch (requestCode) {
            case PhotoUtil.FromWhere.camera:
            case PhotoUtil.FromWhere.photo:
                photoUtil.onActivityResult(requestCode, resultCode, data);
                break;
            case PhotoUtil.FromWhere.forfex:
                if (resultCode == Activity.RESULT_OK) {
                    MyHttp.updateHead(http, null, photoUtil.getForfexPath(), new HttpForVolley.HttpTodo() {
                        @Override
                        public void httpTodo(Integer which, JSONObject response) {
                            if (response.optInt("code",1)!=0){
                                showToast("上传图片发生错误!");
                                return;
                            }
                            showToast("修改头像成功!");
                            String filename = response.optJSONObject("data").optString("head_url");
                            ImageLoader.getInstance().displayImage(filename, iv_head);
                            getUserInfo().head_url = filename;
                        }
                    });
                }
                break;
        }

    }
}
