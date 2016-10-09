package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.widget.MyHeadImageView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.home.SearchActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.main.MyFragment;

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
    private PopupWindow popupWindow;
    private TextView tv_normal_buy;
    private TextView tv_shop;

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
        if(view == null){
            view = inflater.inflate(R.layout.fragment_mine, null);
            initView();
        }
        return view;
    }

    private void initView() {
        tv_lookall = (TextView) view.findViewById(R.id.tv_lookall);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        iv_shotcut = (ImageView) view.findViewById(R.id.iv_shotcut);
        iv_head = (MyHeadImageView) view.findViewById(R.id.iv_head);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_shop = (TextView) view.findViewById(R.id.tv_shop);
        tv_normal_buy = (TextView) view.findViewById(R.id.tv_normal_buy);
        tv_lookall.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        iv_shotcut.setOnClickListener(this);
        tv_shop.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_normal_buy.setOnClickListener(this);
        createPop();
    }



    //每次切换到个人中心fragment时调用此方法
    @Override
    public void onShow() {
        super.onShow();
        Log.d("MineFragment", "MineFragment123123123");
        if(isLogined()){//已经登陆的用户，就初始化用户数据
            showLogined();
        }else {//没有登陆就将页面置为没有登陆的状态
            showUnLogined();
        }
    }

    private void showUnLogined() {
        //将登陆字符显示
        tv_login.setVisibility(View.VISIBLE);
        //将name隐藏
        tv_name.setVisibility(View.GONE);
        //TODO 用户头像设为默认图片
    }

    private void showLogined() {
        //将登陆字符gone
        tv_login.setVisibility(View.GONE);
        //将name显示，并设置店铺名
        tv_name.setVisibility(View.VISIBLE);
        tv_name.setText(getUserInfo().store_name);
        //TODO 显示用户头像
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting:
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.iv_shotcut:
                popupWindow.showAsDropDown(iv_shotcut, BaseValue.dp2px(-6), BaseValue.dp2px(8));
                break;
            case R.id.pop_shortcut_search:
                startActivity(new Intent(mActivity, SearchActivity.class));
                popupWindow.dismiss();
                break;
            case R.id.pop_shortcut_home:
                ((HomeActivity)mActivity).setHomeFragment();
                popupWindow.dismiss();
                break;
            case R.id.tv_lookall:
                startActivity(new Intent(mActivity, OrderListActivity.class));
                break;
            case R.id.tv_normal_buy://跳转至常用采购商品列表页面
                //TODO 登陆拦截
                if(!MyApplication.token.isEmpty()){
                    startActivity(new Intent(getActivity(), NormalBuyActivity.class));
                }
                break;
            case R.id.tv_address://收货地址
                //TODO 登陆拦截
                if(!MyApplication.token.isEmpty()){
                    Log.d("MyApplication", "token:" + MyApplication.token);
                    startActivity(new Intent(mActivity, AddressListActivity.class));
                }
                break;
            case R.id.tv_shop://店铺管理
                //TODO 登陆拦截
                if(!MyApplication.token.isEmpty()){
                    startActivity(new Intent(mActivity, ShopListActivity.class));
                }
                break;
            case R.id.iv_head:
            case R.id.tv_name:
                if(needLogin()){
                    //TODO 点击头像和登录名，不需要登陆，执行的业务
                }
                break;
            case R.id.tv_login:
                needLogin();
                break;

            default:
                break;
        }
    }


    private void createPop() {
        View popView = LayoutInflater.from(mActivity).inflate(R.layout.pop_shortcut, null);
        View pop_shortcut_search = popView.findViewById(R.id.pop_shortcut_search);
        View pop_shortcut_home = popView.findViewById(R.id.pop_shortcut_home);
        pop_shortcut_search.setOnClickListener(this);
        pop_shortcut_home.setOnClickListener(this);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

}
