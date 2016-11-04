package com.cqfrozen.jsh.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.appraise.AppraiseActivity;
import com.cqfrozen.jsh.entity.OrderDetailPageInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.MeasureUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * intent.putExtra("o_id", order_id);
 * intent.putExtra("from", OrderDetailActivity.FROM.FROM_ORDER_BUY);
 */
public class OrderDetailActivity extends MyActivity implements View.OnClickListener {

    private LinearLayout ll_btns;
    private Button btn_cancel_nopay;
    private Button btn_go_pay;
    private Button btn_cancel_noout;
    private Button btn_confirm_get;
    private Button btn_go_say;
    private Button btn_delete;
    private int btn_type = BTN_TYPE.TYPE_NONE;
    private View v_divider;
    private ListView lv_order;
    private OrderDetailLvAdapter orderDetailLvAdapter;
    private ScrollView scrollview;
    private TextView tv_ex_desc;
    private ImageView iv_ex;

    public interface FROM{
        int FROM_ORDER_DEFAULT = 0;
        int FROM_ORDER_BUY = 1;
        int FROM_ORDER_LIST = 2;
    }

    public interface BTN_TYPE{
        int TYPE_NONE = 0;
        int TYPE_GO_PAY = 1;
        int TYPE_NO_OUT = 2;
        int TYPE_CONFIRM_GET = 3;
        int TYPE_GO_SAY = 4;
        int TYPE_DELETE = 5;
    }


    private String o_id;
    private TextView tv_order_status;
    private TextView tv_receiver;
    private TextView tv_phone;
    private TextView tv_address;
    private TextView tv_user_msg;
    private TextView tv_send_price;
    private TextView tv_use_huibi;
    private TextView tv_goods_money;
    private TextView tv_pay_money;
    private TextView tv_pay_style;
    private TextView tv_order_num;
    private TextView tv_order_time;
    private TextView tv_pay_time;
    private TextView tv_count;

    private int from = FROM.FROM_ORDER_DEFAULT;

    private AlertDialog deleteDialog;
    private AlertDialog cancelNoOutDialog;
    private AlertDialog cancelNoPayDialog;

    private List<OrderDetailPageInfo.OrderDetailPageBean> orderDetailPageBeanList = new ArrayList<>();

    private TextView tv_add_time;
    private RelativeLayout rl_order;
    private OrderDetailPageInfo orderDetailPageInfo;

    private int exState = 1;//收缩状态 1收起状态 2展开状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        getIntentData();
        initView();
        initLv();
        getData();
    }

    private void getIntentData() {
        o_id = getIntent().getStringExtra("o_id");
        from = getIntent().getIntExtra("from", FROM.FROM_ORDER_DEFAULT);
    }

    private void initView() {
        setMyTitle("订单详情");
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        tv_order_status = (TextView) findViewById(R.id.tv_order_status);
        tv_add_time = (TextView) findViewById(R.id.tv_add_time);
        tv_receiver = (TextView) findViewById(R.id.tv_receiver);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_user_msg = (TextView) findViewById(R.id.tv_user_msg);
        tv_ex_desc = (TextView) findViewById(R.id.tv_ex_desc);
        iv_ex = (ImageView) findViewById(R.id.iv_ex);
        lv_order = (ListView) findViewById(R.id.lv_order);
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
        v_divider = findViewById(R.id.v_divider);
        ll_btns = (LinearLayout) findViewById(R.id.ll_btns);
        btn_cancel_nopay = (Button) findViewById(R.id.btn_cancel_nopay);
        btn_go_pay = (Button) findViewById(R.id.btn_go_pay);
        btn_cancel_noout = (Button) findViewById(R.id.btn_cancel_noout);
        btn_confirm_get = (Button) findViewById(R.id.btn_confirm_get);
        btn_go_say = (Button) findViewById(R.id.btn_go_say);
        btn_delete = (Button) findViewById(R.id.btn_delete);

//        if(from == FROM.FROM_ORDER_BUY){
//            ll_btns.setVisibility(View.GONE);
//            v_divider.setVisibility(View.GONE);
//        }else if(from == FROM.FROM_ORDER_LIST){
//            ll_btns.setVisibility(View.VISIBLE);
//            v_divider.setVisibility(View.VISIBLE);
//            initBtns();
//        }
        initBtns();

        rl_order.setOnClickListener(this);
        btn_cancel_nopay.setOnClickListener(this);
        btn_go_pay.setOnClickListener(this);
        btn_cancel_noout.setOnClickListener(this);
        btn_confirm_get.setOnClickListener(this);
        btn_go_say.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

    }

    private void initBtns(){
//        if(from != FROM.FROM_ORDER_LIST){
//            return;
//        }

        ll_btns.setVisibility(View.GONE);
        v_divider.setVisibility(View.GONE);
        btn_cancel_nopay.setVisibility(View.GONE);
        btn_go_pay.setVisibility(View.GONE);
        btn_cancel_noout.setVisibility(View.GONE);
        btn_confirm_get.setVisibility(View.GONE);
        btn_go_say.setVisibility(View.GONE);
        btn_delete.setVisibility(View.GONE);
        switch (btn_type) {
            case BTN_TYPE.TYPE_NONE://0所有按钮都不显示
                break;
            case BTN_TYPE.TYPE_GO_PAY://1取消、去支付(未付款),
                v_divider.setVisibility(View.VISIBLE);
                ll_btns.setVisibility(View.VISIBLE);
                btn_cancel_nopay.setVisibility(View.VISIBLE);
                btn_go_pay.setVisibility(View.VISIBLE);
                break;
            case BTN_TYPE.TYPE_NO_OUT://2取消（货到付款未出库）
                v_divider.setVisibility(View.VISIBLE);
                ll_btns.setVisibility(View.VISIBLE);
                btn_cancel_noout.setVisibility(View.VISIBLE);
                break;
            case BTN_TYPE.TYPE_CONFIRM_GET://3确认收货（已发货）
                v_divider.setVisibility(View.VISIBLE);
                ll_btns.setVisibility(View.VISIBLE);
                btn_confirm_get.setVisibility(View.VISIBLE);
                break;
            case BTN_TYPE.TYPE_GO_SAY://4去评价(已收货、未评价)
                v_divider.setVisibility(View.VISIBLE);
                ll_btns.setVisibility(View.VISIBLE);
                btn_go_say.setVisibility(View.VISIBLE);
                break;
            case BTN_TYPE.TYPE_DELETE://5删除（取消订单、已完成评价订单）
                v_divider.setVisibility(View.VISIBLE);
                ll_btns.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void initLv() {
        lv_order.setOverScrollMode(View.OVER_SCROLL_NEVER);
        orderDetailLvAdapter = new OrderDetailLvAdapter(this,
                orderDetailPageBeanList);
        lv_order.setAdapter(orderDetailLvAdapter);
    }

    private void getData() {
        MyHttp.orderInfo(http, null, o_id, new MyHttp.MyHttpResult() {

            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
//                    showToast(msg);
                    return;
                }
                orderDetailPageInfo = (OrderDetailPageInfo) bean;
                btn_type = orderDetailPageInfo.btn_type;
                initViewData(orderDetailPageInfo);
            }
        });
    }

    private void initViewData(OrderDetailPageInfo orderDetailPageInfo) {
        if(orderDetailPageInfo.oinfo.size() == 1){
            rl_order.setVisibility(View.GONE);
            orderDetailPageBeanList.add(orderDetailPageInfo.oinfo.get(0));
            orderDetailLvAdapter.notifyDataSetChanged();
        }else {
            rl_order.setVisibility(View.VISIBLE);
            exLvList();
            MeasureUtil.setListViewHeightBasedOnChildren(lv_order);
        }
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
        tv_pay_style.setText(orderDetailPageInfo.pay_mode_str);
        tv_order_num.setText(orderDetailPageInfo.o_num);
        tv_order_time.setText(orderDetailPageInfo.add_time);
        tv_pay_time.setText(orderDetailPageInfo.pay_time);
        tv_count.setText("共" + orderDetailPageInfo.count +"件商品");
        initBtns();
    }

    // 0所有按钮都不显示，
    // 1取消、去支付(未付款),
    // 2取消（货到付款未出库），
    // 3确认收货（已发货）,
    // 4去评价(已收货、未评价)，
    // 5删除（取消订单、已完成评价订单）
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_order://点击跳转订单详情页  显示 全部订单商品
//                Intent intent = new Intent(OrderDetailActivity.this, OrderGooodsDetailListActivity.class);
//                intent.putExtra("orderDetailPageInfo", orderDetailPageInfo);
//                startActivity(intent);
                if(exState == 1){
                    exLvList();
                }else if(exState == 2){
                    unexLvList();
                }
                break;
            case R.id.btn_cancel_nopay://1取消 未付款订单 点击取消按钮
                cancelNoPayOrder();
                break;
            case R.id.btn_go_pay://未付款订单 点击去付款
                showToast("此功能暂未开放");
                break;
            case R.id.btn_cancel_noout://点击取消 没发货订单
                cancelNoOutOrder();
                break;
            case R.id.btn_confirm_get://确认收货（已发货）
                confirmGet();
                break;
            case R.id.btn_go_say://去评价
                goSay();
                break;
            case R.id.btn_delete:
                delete();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lv_order.setFocusable(false);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.requestFocus();
    }

    /**
     * 收起列表
     */
    private void unexLvList() {
        orderDetailPageBeanList.clear();
        orderDetailPageBeanList.add(orderDetailPageInfo.oinfo.get(0));
        orderDetailLvAdapter.notifyDataSetChanged();
        MeasureUtil.setListViewHeightBasedOnChildren(lv_order);
        tv_ex_desc.setText("显示全部商品");
        iv_ex.setImageResource(R.mipmap.list_gengduo_h);
        exState = 1;
    }

    /**
     * 展开列表
     */
    private void exLvList() {
        orderDetailPageBeanList.clear();
        orderDetailPageBeanList.addAll(orderDetailPageInfo.oinfo);
        orderDetailLvAdapter.notifyDataSetChanged();
        MeasureUtil.setListViewHeightBasedOnChildren(lv_order);
        tv_ex_desc.setText("收起全部商品");
        iv_ex.setImageResource(R.mipmap.list_gengduo_up);
        exState = 2;
    }

    /**
     * 去评价,跳转至评价页面
     */
    private void goSay() {
        Intent intent = new Intent(this, AppraiseActivity.class);
        intent.putExtra("o_id", o_id);
        startActivity(intent);
    }

    //确认收货（已发货）
    private void confirmGet() {
        MyHttp.orderConfirm(http, null, o_id, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                showToast(response.optString("msg"));
                int code = response.optInt("code");
                if(code != 0){
                    return;
                }
                btn_confirm_get.setVisibility(View.GONE);
                btn_go_say.setVisibility(View.VISIBLE);
                goSay();//跳转评论页面
            }
        });
    }

    /**
     * 删除订单
     */
    private void delete() {
        showDeleteDialog();
    }

    private void showDeleteDialog() {
        deleteDialog = new AlertDialog.Builder(this)
                .setMessage("确定要删除该订单吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyHttp.orderDelete(http, null, o_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                showToast(response.optString("msg"));
                                int code = response.optInt("code");
                                if(code != 0){
                                    return;
                                }
                                ll_btns.setVisibility(View.GONE);
                                v_divider.setVisibility(View.GONE);
                                btn_cancel_nopay.setVisibility(View.GONE);
                                btn_go_pay.setVisibility(View.GONE);
                                btn_cancel_noout.setVisibility(View.GONE);
                                btn_confirm_get.setVisibility(View.GONE);
                                btn_go_say.setVisibility(View.GONE);
                                btn_delete.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        deleteDialog.show();
    }

    //点击取消 没发货订单
    private void cancelNoOutOrder() {
        showCancelNoOutDialog();
    }

    private void showCancelNoOutDialog() {
        cancelNoOutDialog = new AlertDialog.Builder(this)
                .setMessage("确定要取消该订单吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyHttp.cancelOrder(http, null, o_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                showToast(response.optString("msg"));
                                int code = response.optInt("code");
                                if(code != 0){
                                    return;
                                }
                                btn_cancel_noout.setVisibility(View.GONE);
                                btn_delete.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        cancelNoOutDialog.show();
    }

    //1取消 未付款订单 点击取消按钮
    private void cancelNoPayOrder() {
        cancelNoPayDialog();
    }

    private void cancelNoPayDialog() {
        cancelNoPayDialog = new AlertDialog.Builder(this)
                .setMessage("确定要取消该订单吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyHttp.cancelOrder(http, null, o_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                showToast(response.optString("msg"));
                                int code = response.optInt("code");
                                if(code != 0){
                                    return;
                                }
                                btn_cancel_nopay.setVisibility(View.GONE);
                                btn_delete.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        cancelNoPayDialog.show();
    }
}
