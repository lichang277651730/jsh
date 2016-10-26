package com.cqfrozen.jsh.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.common.base.BaseActivity;
import com.common.base.BaseFragment;
import com.common.widget.MyViewPager;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.cart.CartFragment;
import com.cqfrozen.jsh.cart.CartManager;
import com.cqfrozen.jsh.center.MineFragment;
import com.cqfrozen.jsh.classify.ClassifyFragment;
import com.cqfrozen.jsh.home.HomeFragment;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 * 导航首页
 */
public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, MyViewPager.OnMyPageChangeListener{

    private static MyViewPager vp_home;
    private RadioGroup rg_home;
    private List<BaseFragment> fragments;
    private static RadioButton[] rb_homes = new RadioButton[4];
    private BaseFragment curFragment;//当前选择页面

    private static HomeActivity instance;
    protected int clickCount;
    protected long clickFirstTime;
    protected long clickSecondTime;
    private ImageView iv_cart;
    private CartManager cartManager;
    private BadgeView badgeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setSwipeBackEnable(true);
        instance = this;
        initView();
        initFragment();
        cartManager = MyApplication.cartManager;
        initBadgeView();
    }

    private void initBadgeView() {
        badgeView = new BadgeView(this, iv_cart);
        badgeView.setEnabled(false);
        badgeView.setFocusable(false);
        if(cartManager != null){
            badgeView.setVisibility(View.VISIBLE);
            badgeView.setText(cartManager.getCartGoodsNum() + "");
            badgeView.setText(0 + "");
            badgeView.setTextSize(10);
            badgeView.setBadgeMargin(70, 0);
            badgeView.show();
            cartManager.setOnNumChangeListener(new CartManager.OnNumChangeListener() {
                @Override
                public void onNumChangeListener(int curNum) {
                    if(!badgeView.isShown()){
                        badgeView.show();
                    }
                    if(badgeView != null && badgeView.isShown()){
                        if(curNum >= 100){
                            badgeView.setText("99+");
                        }else {
                            badgeView.setText(curNum + "");
                        }
                    }
                }
            });
        }else {
            badgeView.setVisibility(View.GONE);
        }
    }

    private void initView() {
        vp_home = (MyViewPager) findViewById(R.id.vp_home);
        rg_home = (RadioGroup) findViewById(R.id.rg_home);
        iv_cart = (ImageView) findViewById(R.id.iv_cart);
        rb_homes[0] = (RadioButton) findViewById(R.id.rb_home);
        rb_homes[1] = (RadioButton) findViewById(R.id.rb_classify);
        rb_homes[2] = (RadioButton) findViewById(R.id.rb_cart);
        rb_homes[3] = (RadioButton) findViewById(R.id.rb_mine);
        vp_home.setOnMyPageChangeListener(this);
        rg_home.setOnCheckedChangeListener(this);
        vp_home.setOffscreenPageLimit(1);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(HomeFragment.getInstance());
        fragments.add(ClassifyFragment.getInstance());
        fragments.add(CartFragment.getInstance());
        fragments.add(MineFragment.getInstance());
        vp_home.setFragemnt(getSupportFragmentManager(), fragments);
    }

    /**
     * 点击底部导航栏切换页面
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for(int i = 0; i < 4; i++){
            if(rb_homes[i].getId() == checkedId){
                curFragment = fragments.get(i);
                vp_home.setCurrentItem(i, false);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        if(curFragment != null){
            curFragment.onShow();
        }
        super.onResume();
    }




    @Override
    public void OnMyPageSelected(int position) {
        rb_homes[position].setChecked(true);
        //TODO 根据不同的页面改变状态栏的颜色
    }

    @Override
    public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void OnMyPageScrollStateChanged(int arg0) {

    }

    /**
     * 两秒内双击退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //TODO 如果有更新登陆框就要先取消
        if(vp_home.getCurrentItem() != 0){
            vp_home.setCurrentItem(0, false);
            rb_homes[0].setChecked(true);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            clickCount++;
            if (clickCount == 1) {
                clickFirstTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else if (clickCount == 2) {
                clickSecondTime = System.currentTimeMillis();
                if (clickSecondTime - clickFirstTime <= 2000) {
                    instance = null;
                    System.exit(0);
                } else {
                    clickCount = 1;
                    clickFirstTime = System.currentTimeMillis();
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                }
            } else {
                clickCount = 1;
                clickFirstTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public MyViewPager getViewPager(){
        return this.vp_home;
    }

    public void setHomeFragment(){
        vp_home.setCurrentItem(0, false);
        rb_homes[0].setChecked(true);
    }

    public void setClassifyFragment(){
        vp_home.setCurrentItem(1, false);
        rb_homes[1].setChecked(true);
    }

    public void setCartFragment(){
        vp_home.setCurrentItem(2, false);
        rb_homes[2].setChecked(true);
    }

    /**
     * 设置购物车角标数字
     * @param cartGoodsNum
     */
    public void setCartNum(int cartGoodsNum) {
        if(badgeView != null){
            if(cartGoodsNum >= 100){
                badgeView.setText("99+");
            }else {
                badgeView.setText(cartGoodsNum + "");
            }
        }
    }

    public static void startActivity(Context context, int position){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        vp_home.setCurrentItem(position, false);
        rb_homes[position].setChecked(true);
    }
}
