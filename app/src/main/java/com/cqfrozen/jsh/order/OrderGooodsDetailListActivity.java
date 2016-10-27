package com.cqfrozen.jsh.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderDetailPageInfo;
import com.cqfrozen.jsh.main.MyActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 * intent2.putExtra("orderDetailPageInfo", orderDetailPageInfo);
 */
public class OrderGooodsDetailListActivity extends MyActivity {


    private List<OrderDetailPageInfo.OrderDetailPageBean> oinfoList;
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
        OrderDetailPageInfo orderDetailPageInfo = (OrderDetailPageInfo) getIntent().getSerializableExtra("orderDetailPageInfo");
        oinfoList = orderDetailPageInfo.oinfo;
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
        OrderDetailRVAdapter orderRVAdapter = new OrderDetailRVAdapter(this, oinfoList);
        rv_ordergoods.setAdapter(orderRVAdapter);
    }
}
