package com.cqfrozen.jsh.center;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.UIUtils;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

/**
 * Created by Administrator on 2016/9/20.
 */
public class OrderListActivity extends MyActivity {

    private ScrollIndicatorView indicator_order;
    private ViewPager vp_order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        initView();
        initVP();
        getData();
    }

    private void initView() {
        setMyTitle("我的订单");
        indicator_order = (ScrollIndicatorView) findViewById(R.id.indicator_order);
        vp_order = (ViewPager) findViewById(R.id.vp_order);
    }

    private void initVP() {
        vp_order.setOverScrollMode(View.OVER_SCROLL_NEVER);
        indicator_order.setScrollBar(new ColorBar(this, UIUtils.getColor(R.color.main), BaseValue.dp2px(4)){

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - BaseValue.dp2px(30);
            }
        });
        // 设置滚动监听
        indicator_order.setOnTransitionListener(new OnTransitionTextListener().setColor(UIUtils.getColor(R.color.main), Color.BLACK));
        vp_order.setOffscreenPageLimit(2);
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator_order, vp_order);
        indicatorViewPager.setClickIndicatorAnim(false);
        indicatorViewPager.setAdapter(new OrderIndicatorAdapter(getSupportFragmentManager(), this));
    }

    private void getData() {

    }
}
