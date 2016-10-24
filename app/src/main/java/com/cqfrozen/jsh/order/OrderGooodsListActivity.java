package com.cqfrozen.jsh.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.OrderRVAdapter;
import com.cqfrozen.jsh.entity.OrderInfo;
import com.cqfrozen.jsh.main.MyActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 *  intent.putExtra("orderInfo", orderInfo);
 */
public class OrderGooodsListActivity extends MyActivity {

    private OrderInfo orderInfo;
    private List<OrderInfo.OrderGoodsBean> orderGoodsList;
    private RecyclerView rv_ordergoods;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordergoodslist);
        getIntentData();
        initView();
        initRC();
    }

    private void getIntentData() {
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        orderGoodsList = orderInfo.goods;
    }

    private void initView() {
        setMyTitle("商品清单");
        rv_ordergoods = (RecyclerView) findViewById(R.id.rv_ordergoods);
    }

    private void initRC() {
        rv_ordergoods.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_ordergoods.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_ordergoods.addItemDecoration(decoration);
        OrderRVAdapter orderRVAdapter = new OrderRVAdapter(this, orderGoodsList);
        rv_ordergoods.setAdapter(orderRVAdapter);
    }
}
