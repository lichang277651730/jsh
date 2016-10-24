package com.cqfrozen.jsh.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.common.base.BaseValue;
import com.common.widget.MyEditText;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.OrderRVAdapter;
import com.cqfrozen.jsh.entity.OrderInfo;
import com.cqfrozen.jsh.main.MyActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 *  intent.putExtra("orderInfo", orderInfo);
 */
public class OrderConfirmActivity extends MyActivity implements View.OnClickListener {

    private OrderInfo orderInfo;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_address;
    private RecyclerView rv_order;
    private TextView tv_count;
    private TextView tv_freight;
    private TextView tv_huibi;
    private ToggleButton gbtn_huibi;
    private TextView tv_order_count;
    private TextView tv_freight_again;
    private TextView tv_order_sum;
    private MyEditText et_words;
    private List<OrderInfo.OrderAddressBean> addressList;
    private List<OrderInfo.OrderGoodsBean> goodsList = new ArrayList<>();
    private OrderInfo.OrderAddressBean defaultAddress;
    private TextView tv_goods_price;
    private RelativeLayout rl_address;
    private RelativeLayout rl_order;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirm);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        addressList = orderInfo.address;
        goodsList.add(orderInfo.goods.get(0));
        if(addressList != null){
            for(int i = 0; i < addressList.size(); i++){
                if(addressList.get(i).is_default == 1){
                    defaultAddress = addressList.get(i);
                }
            }
        }

    }

    private void initView() {
        setMyTitle("确认订单");
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        rv_order = (RecyclerView) findViewById(R.id.rv_order);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_freight = (TextView) findViewById(R.id.tv_freight);
        tv_huibi = (TextView) findViewById(R.id.tv_huibi);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        gbtn_huibi = (ToggleButton) findViewById(R.id.gbtn_huibi);
        tv_order_count = (TextView) findViewById(R.id.tv_order_count);
        tv_freight_again = (TextView) findViewById(R.id.tv_freight_again);
        tv_order_sum = (TextView) findViewById(R.id.tv_order_sum);
        et_words = (MyEditText) findViewById(R.id.et_words);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        rl_order = (RelativeLayout) findViewById(R.id.rl_order);

        rl_address.setOnClickListener(this);
        rl_order.setOnClickListener(this);

        rv_order.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_order.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_order.addItemDecoration(decoration);

        initData();

        gbtn_huibi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    tv_order_sum.setText(orderInfo == null ? "" : "￥" + orderInfo.order_amount);
                    tv_freight.setText(orderInfo == null ? "" : "￥" + orderInfo.weight_amount);
                    tv_goods_price.setText(orderInfo == null ? "" : "￥" + orderInfo.goods_amount);
                    tv_freight_again.setText(orderInfo == null ? "" : "运费:￥" + orderInfo.weight_amount);
                }else {
                    tv_order_sum.setText(orderInfo == null ? "" : "￥" + orderInfo.order_amount_hb);
                    tv_freight.setText(orderInfo == null ? "" : "￥" + orderInfo.weight_amount_hb);
                    tv_goods_price.setText(orderInfo == null ? "" : "￥" + orderInfo.goods_amount_hb);
                    tv_freight_again.setText(orderInfo == null ? "" : "运费:￥" + orderInfo.weight_amount_hb);
                }
            }
        });
    }

    private void initData() {
        tv_name.setText(defaultAddress == null ? "收货人:" : "收货人:" + defaultAddress.china_name);
        tv_phone.setText(defaultAddress == null ? "" : defaultAddress.mobile_num);
        tv_address.setText(defaultAddress == null ? "收货地址:" : "收货地址:" + defaultAddress.address);
        if(goodsList != null){
            OrderRVAdapter orderRVAdapter = new OrderRVAdapter(this, goodsList);
            rv_order.setAdapter(orderRVAdapter);
        }
        tv_count.setText(goodsList == null ? "共0件商品" : "共" +orderInfo.goods.size() + "件商品");
        tv_freight.setText(orderInfo == null ? "" : "￥" + orderInfo.weight_amount);
        tv_huibi.setText(orderInfo == null ? "" : "￥" + orderInfo.use_hb_count);
        tv_goods_price.setText(orderInfo == null ? "" : "￥" + orderInfo.goods_amount);
        tv_order_count.setText(goodsList == null ? "0" : orderInfo.goods.size() + "");
        tv_freight_again.setText(orderInfo == null ? "" : "运费:￥" + orderInfo.weight_amount);
        tv_order_sum.setText(orderInfo == null ? "" : "￥" + orderInfo.order_amount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_address:
                Intent intent1 = new Intent(this, OrderAddressListActivity.class);
                intent1.putExtra("orderInfo", orderInfo);
                startActivity(intent1);
                break;
            case R.id.rl_order:
                Intent intent2 = new Intent(this, OrderGooodsListActivity.class);
                intent2.putExtra("orderInfo", orderInfo);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
