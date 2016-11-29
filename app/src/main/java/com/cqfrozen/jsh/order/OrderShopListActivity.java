package com.cqfrozen.jsh.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderInfo;
import com.cqfrozen.jsh.main.MyActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class OrderShopListActivity extends MyActivity {

    private List<OrderInfo.OrderAddressBean> orderAddressList;
    private OrderInfo orderInfo;
    private RecyclerView rv_ordershop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordershoplist);
        getIntentData();
        initView();
        initRC();
    }

    private void getIntentData() {
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        orderAddressList = orderInfo.address;
    }

    private void initView() {
        setMyTitle("选择收货店铺");
        rv_ordershop = (RecyclerView) findViewById(R.id.rv_ordershop);
    }

    private void initRC() {
        rv_ordershop.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_ordershop.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(4), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_ordershop.addItemDecoration(decoration);
        OrderShopAdapter adapter = new OrderShopAdapter(this, orderAddressList);
        rv_ordershop.setAdapter(adapter);
        adapter.setOnItemClickListener(new OrderShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, OrderInfo.OrderAddressBean orderAddressBean) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
