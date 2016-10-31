package com.cqfrozen.jsh.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.entity.OrderDetailInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

/**
 * Created by Administrator on 2016/10/25.
 * intent.putExtra("order_id", orderBuyResultInfo.o_id);
 */
public class OrderBuyResultActivity extends MyActivity implements View.OnClickListener {

    private TextView tv_order_num;
    private TextView tv_order_time;
    private TextView tv_pay_type;
    private TextView tv_order_money;
    private Button btn_order_detail;
    private Button btn_go_home;
    private String order_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderbuyresult);
        getIntentData();
        initView();
        getData();
    }

    private void getIntentData() {
        order_id = getIntent().getStringExtra("order_id");
    }


    private void initView() {
        setMyTitle("支付成功");
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
        tv_order_money = (TextView) findViewById(R.id.tv_order_money);
        btn_order_detail = (Button) findViewById(R.id.btn_order_detail);
        btn_go_home = (Button) findViewById(R.id.btn_go_home);

        btn_order_detail.setOnClickListener(this);
        btn_go_home.setOnClickListener(this);
    }

    private void getData() {
        MyHttp.orderSuccess(http, null, order_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                OrderDetailInfo orderDetailInfo = (OrderDetailInfo) bean;
                initData(orderDetailInfo);
            }
        });
    }

    private void initData(OrderDetailInfo orderDetailInfo) {
        tv_order_num.setText(orderDetailInfo.o_num);
        tv_order_time.setText(orderDetailInfo.add_time);
        tv_pay_type.setText(orderDetailInfo.pay_mode_str);
        tv_order_money.setText("￥" +orderDetailInfo.order_amount);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_order_detail://订单详情
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("o_id", order_id);
                intent.putExtra("from", OrderDetailActivity.FROM.FROM_ORDER_BUY);
                startActivity(intent);
                break;
            case R.id.btn_go_home://返回首页
                HomeActivity.startActivity(this, 0);
                break;
            default:
                break;
        }
    }
}
