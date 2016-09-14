package com.cqfrozen.jsh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.base.BaseActivity;
import com.cqfrozen.jsh.base.BaseFragment;
import com.cqfrozen.jsh.cart.CartFragment;
import com.cqfrozen.jsh.center.MineFragment;
import com.cqfrozen.jsh.classify.ClassifyFragment;
import com.cqfrozen.jsh.home.HomeFragment;
import com.cqfrozen.jsh.widget.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, MyViewPager.OnMyPageChangeListener{

    private MyViewPager vp_home;
    private RadioGroup rg_home;
    private List<BaseFragment> fragments;
    private RadioButton[] rb_homes = new RadioButton[4];
    private BaseFragment curFragment;//当前选择页面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initFragment();
    }

    private void initView() {
        vp_home = (MyViewPager) findViewById(R.id.vp_home);
        rg_home = (RadioGroup) findViewById(R.id.rg_home);
        rb_homes[0] = (RadioButton) findViewById(R.id.rb_home);
        rb_homes[1] = (RadioButton) findViewById(R.id.rb_classify);
        rb_homes[2] = (RadioButton) findViewById(R.id.rb_cart);
        rb_homes[3] = (RadioButton) findViewById(R.id.rb_mine);
        vp_home.setOnMyPageChangeListener(this);
        rg_home.setOnCheckedChangeListener(this);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(HomeFragment.getInstance());
        fragments.add(ClassifyFragment.getInstance());
        fragments.add(CartFragment.getInstance());
        fragments.add(MineFragment.getInstance());
        vp_home.setFragemnt(getSupportFragmentManager(), fragments);
        curFragment = fragments.get(0);
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
        super.onResume();
        if(curFragment != null){
            curFragment.onShow();
        }
    }


    @Override
    public void OnMyPageSelected(int position) {
        rb_homes[position].setChecked(true);
    }

    @Override
    public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void OnMyPageScrollStateChanged(int arg0) {

    }
}
