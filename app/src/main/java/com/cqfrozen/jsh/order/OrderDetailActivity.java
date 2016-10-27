package com.cqfrozen.jsh.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderDetailPageInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * intent.putExtra("o_id", order_id);
 */
public class OrderDetailActivity extends MyActivity {

    private String o_id;
    private TextView tv_order_status;
    private TextView tv_receiver;
    private TextView tv_phone;
    private TextView tv_address;
    private TextView tv_user_msg;
    private RecyclerView rv_order;
    private TextView tv_send_price;
    private TextView tv_use_huibi;
    private TextView tv_goods_money;
    private TextView tv_pay_money;
    private TextView tv_pay_style;
    private TextView tv_order_num;
    private TextView tv_order_time;
    private TextView tv_pay_time;
    private TextView tv_count;

    private List<OrderDetailPageInfo.OrderDetailPageBean> orderDetailPageBeanList = new ArrayList<>();

    private OrderDetailRVAdapter detailRVAdapter;
    private TextView tv_add_time;
    private RelativeLayout rl_order;
    private OrderDetailPageInfo orderDetailPageInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        getIntentData();
        initView();
        initRC();
        getData();
    }

    private void getIntentData() {
        o_id = getIntent().getStringExtra("o_id");
    }

    private void initView() {
        setMyTitle("订单详情");
        tv_order_status = (TextView) findViewById(R.id.tv_order_status);
        tv_add_time = (TextView) findViewById(R.id.tv_add_time);
        tv_receiver = (TextView) findViewById(R.id.tv_receiver);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_user_msg = (TextView) findViewById(R.id.tv_user_msg);
        rv_order = (RecyclerView) findViewById(R.id.rv_order);
        tv_send_price = (TextView) findViewById(R.id.tv_send_price);
        tv_use_huibi = (TextView) findViewById(R.id.tv_use_huibi);
        tv_goods_money = (TextView) findViewById(R.id.tv_goods_money);
        tv_pay_money = (TextView) findViewById(R.id.tv_pay_money);
        tv_pay_style = (TextView) findViewById(R.id.tv_pay_style);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
        tv_count = (TextView) findViewById(R.id.tv_count);
        rl_order = (RelativeLayout) findViewById(R.id.rl_order);
        rl_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(OrderDetailActivity.this, OrderGooodsDetailListActivity.class);
                intent2.putExtra("orderDetailPageInfo", orderDetailPageInfo);
                startActivity(intent2);
            }
        });
    }

    private void initRC() {
        rv_order.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_order.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_order.addItemDecoration(decoration);
        detailRVAdapter = new OrderDetailRVAdapter(this,
                orderDetailPageBeanList);
        rv_order.setAdapter(detailRVAdapter);
    }

    private void getData() {
        MyHttp.orderInfo(http, null, o_id, new MyHttp.MyHttpResult() {

            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                orderDetailPageInfo = (OrderDetailPageInfo) bean;
                initViewData(orderDetailPageInfo);
            }
        });
    }

    private void initViewData(OrderDetailPageInfo orderDetailPageInfo) {
        orderDetailPageBeanList.add(orderDetailPageInfo.oinfo.get(0));
        detailRVAdapter.notifyDataSetChanged();
        tv_order_status.setText(orderDetailPageInfo.status_name);
        tv_add_time.setText(orderDetailPageInfo.add_time);
        tv_receiver.setText("收货人:" + orderDetailPageInfo.china_name);
        tv_phone.setText(orderDetailPageInfo.mobile_num);
        tv_address.setText("收货地址:" + orderDetailPageInfo.address);
        tv_user_msg.setText("买家留言:" + orderDetailPageInfo.msg_content);
        tv_send_price.setText("￥" + orderDetailPageInfo.weight_amount);
        tv_use_huibi.setText("￥" + orderDetailPageInfo.use_hb_count);
        tv_goods_money.setText("￥" + orderDetailPageInfo.g_amount);
        tv_pay_money.setText("￥" + orderDetailPageInfo.order_amount);
        tv_pay_style.setText("￥" + orderDetailPageInfo.pay_mode_str);
        tv_pay_style.setText("￥" + orderDetailPageInfo.pay_mode_str);
        tv_order_num.setText(orderDetailPageInfo.o_num);
        tv_order_time.setText(orderDetailPageInfo.add_time);
        tv_pay_time.setText(orderDetailPageInfo.pay_time);
        tv_count.setText("共" + orderDetailPageInfo.count +"件商品");
    }
}
