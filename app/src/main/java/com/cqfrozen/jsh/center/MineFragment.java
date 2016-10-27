package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.widget.MyHeadImageView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.order.OrderListActivity;
import com.cqfrozen.jsh.util.ShortcutPop;
import com.cqfrozen.jsh.widget.BadgeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        iv_shotcut = (ImageView) view.findViewById(R.id.iv_shotcut);
        iv_table1 = (ImageView) view.findViewById(R.id.iv_table1);
        iv_table2 = (ImageView) view.findViewById(R.id.iv_table2);
        iv_table3 = (ImageView) view.findViewById(R.id.iv_table3);
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
        tv_normal_buy = (TextView) view.findViewById(R.id.tv_normal_buy);

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
        badgeView1.hide();
        badgeView2.hide();
        badgeView3.hide();
        iv_head.setImageResource(R.mipmap.icon_mine_head_default);
    }

    private void showLogined() {
        //将登陆字符gone
        tv_login.setVisibility(View.GONE);
        //将name显示，并设置店铺名
        tv_name.setVisibility(View.VISIBLE);
        tv_name.setText(getUserInfo().store_name);
        tv_phone.setText(getUserInfo().mobile_num);
        tv_verify.setText(getUserInfo().verify_name);
        tv_huibi.setText(getUserInfo().hb_count + "");
        tv_fans.setText(getUserInfo().inotal_fans_count + "");

        badgeView1.setText(getUserInfo().df_count >= 100 ? "99+" : getUserInfo().df_count + "");
        badgeView2.setText(getUserInfo().ds_count >= 100 ? "99+" : getUserInfo().ds_count + "");
        badgeView3.setText(getUserInfo().dp_count >= 100 ? "99+" : getUserInfo().dp_count + "");

        if(getUserInfo().df_count == 0){
            badgeView1.hide();
        }else {
            badgeView1.show();
        }
        if(getUserInfo().ds_count == 0){
            badgeView2.hide();
        }else {
            badgeView2.show();
        }
        if(getUserInfo().dp_count == 0){
            badgeView3.hide();
        }else {
            badgeView3.show();
        }

        DisplayImageOptions build = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).showImageOnFail(R.mipmap.icon_mine_head_default)
                .showImageForEmptyUri(R.mipmap.icon_mine_head_default).build();
        ImageLoader.getInstance().displayImage(getUserInfo().head_url, iv_head, build);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting://设置
                if(isLogined()){
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
                    startActivity(new Intent(mActivity, SettingActivity.class));
                }
                break;
            case R.id.tv_login:
                needLogin();
                break;
            case R.id.ll_huibi://汇币列表
                if(isLogined()){
                    Intent intent = new Intent(mActivity, HuibiListActivity.class);
                    intent.putExtra("hb_count", getUserInfo().hb_count);
                    startActivity(intent);
                }
                break;
            case R.id.ll_fans://粉丝列表
                if(isLogined()){
                    startActivity(new Intent(mActivity, FansListActity.class));
                }
                break;
            case R.id.tv_lookall:
                if(isLogined()){
                    Intent intent = new Intent(mActivity, OrderListActivity.class);
                    intent.putExtra("page_index", OrderListActivity.PAGE_ALL);
                    startActivity(intent);
                }
                break;
            case R.id.ll_table1://待付款
                if(isLogined()){
                    Intent intent = new Intent(mActivity, OrderListActivity.class);
                    intent.putExtra("page_index", OrderListActivity.PAGE_NO_PAY);
                    startActivity(intent);
                }
                break;
            case R.id.ll_table2://待收货
                if(isLogined()){
                    Intent intent = new Intent(mActivity, OrderListActivity.class);
                    intent.putExtra("page_index", OrderListActivity.PAGE_NO_RECEIVE);
                    startActivity(intent);
                }
                break;
            case R.id.ll_table3://待评价
                if(isLogined()){
                    Intent intent = new Intent(mActivity, OrderListActivity.class);
                    intent.putExtra("page_index", OrderListActivity.PAGE_NO_SAY);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
