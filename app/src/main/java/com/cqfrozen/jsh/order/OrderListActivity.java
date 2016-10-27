package com.cqfrozen.jsh.order;

import android.content.Context;
import android.content.Intent;
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
 * intent.putExtra("page_index", OrderListActivity.PAGE_NO_PAY);
 */
public class OrderListActivity extends MyActivity {

    public static final int PAGE_ALL = 0;//全部
    public static final int PAGE_NO_PAY = 1;//代付款
    public static final int PAGE_NO_RECEIVE = 2;//待收货
    public static final int PAGE_NO_SAY = 3;//待评价

    private ScrollIndicatorView indicator_order;
    private static ViewPager vp_order;
    private int page_index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        getIntentData();
        initView();
        initVP();
    }

    private void getIntentData() {
        page_index = getIntent().getIntExtra("page_index", PAGE_ALL);
    }

    private void initView() {
        setMyTitle("我的订单");
        indicator_order = (ScrollIndicatorView) findViewById(R.id.indicator_order);
        vp_order = (ViewPager) findViewById(R.id.vp_order);
    }

    private void initVP() {
        vp_order.setOverScrollMode(View.OVER_SCROLL_NEVER);
        vp_order.setOffscreenPageLimit(1);
        indicator_order.setScrollBar(new ColorBar(this, UIUtils.getColor(R.color.main), BaseValue.dp2px(4)){

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - BaseValue.dp2px(30);
            }
        });
        // 设置滚动监听
        indicator_order.setOnTransitionListener(new OnTransitionTextListener().setColor(UIUtils.getColor(R.color.main), Color.BLACK));

        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator_order, vp_order);
        indicatorViewPager.setClickIndicatorAnim(false);
        indicatorViewPager.setAdapter(new OrderIndicatorAdapter(getSupportFragmentManager(), this));
        vp_order.setCurrentItem(page_index, false);
    }

    public static void startActivity(Context context, int position){
        Intent intent = new Intent(context, OrderListActivity.class);
        context.startActivity(intent);
    }

}
