package com.cqfrozen.jsh.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.cart.CartManager;
import com.cqfrozen.jsh.entity.OrderBuyResultInfo;
import com.cqfrozen.jsh.entity.OrderInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.MeasureUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 *  intent.putExtra("orderInfo", orderInfo);
 *  intent.putExtra("carDataAry", carDataAry);
 */
public class OrderConfirmActivity extends MyActivity implements View.OnClickListener {

    private static final int REUEST_CODE_ADDRESS = 1;

    private OrderInfo orderInfo;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_address;
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
    private TextView tv_default_address;
    private Button btn_buy;

    private String curAddressId = "";
    private int pay_mode = 1;//支付方式 1货到付款，2微信支付，3支付宝
    private int is_use_hb = 0;//是否使用汇币(0否，1是)
    private String msg_content = "";//用户留言
    private String carDataAry = "";//购物车数组 2002，2003,1001
    private TextView tv_shop;
    private ListView lv_goods;
    private TextView tv_ex_desc;
    private ImageView iv_ex;
    private OrderConfirmLvAdapter orderConfirmLvAdapter;
    private int exState = 1;//收缩状态 1收起状态 2展开状态
    private ScrollView scrollview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirm);
        getIntentData();
        initView();
        initLv();
        initData();
    }

    private void getIntentData() {
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        carDataAry =  getIntent().getStringExtra("carDataAry");
        addressList = orderInfo.address;
        if(addressList != null){
            for(int i = 0; i < addressList.size(); i++){
                if(addressList.get(i).is_default == 1){
                    defaultAddress = addressList.get(i);
                }
            }
        }
        curAddressId = defaultAddress.a_id;
    }

    private void initView() {
        setMyTitle("确认订单");
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_default_address = (TextView) findViewById(R.id.tv_default_address);
        lv_goods = (ListView) findViewById(R.id.lv_goods);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_ex_desc = (TextView) findViewById(R.id.tv_ex_desc);
        iv_ex = (ImageView) findViewById(R.id.iv_ex);
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
        btn_buy = (Button) findViewById(R.id.btn_buy);

        rl_address.setOnClickListener(this);
        rl_order.setOnClickListener(this);
        btn_buy.setOnClickListener(this);


//        rv_order.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        GridLayoutManager manager = new GridLayoutManager(this, 1);
//        rv_order.setLayoutManager(manager);
//        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
//                .dp2px(0), getResources().getColor(R.color.mybg), false);
//        rv_order.addItemDecoration(decoration);
        gbtn_huibi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    tv_order_sum.setText(orderInfo == null ? "" : "￥" + orderInfo.order_amount);
                    tv_freight.setText(orderInfo == null ? "" : "￥" + orderInfo.weight_amount);
                    tv_goods_price.setText(orderInfo == null ? "" : "￥" + orderInfo.goods_amount);
                    tv_freight_again.setText(orderInfo == null ? "" : "运费:￥" + orderInfo.weight_amount);
                    is_use_hb = 0;//不使用汇币
                }else {
                    tv_order_sum.setText(orderInfo == null ? "" : "￥" + orderInfo.order_amount_hb);
                    tv_freight.setText(orderInfo == null ? "" : "￥" + orderInfo.weight_amount_hb);
                    tv_goods_price.setText(orderInfo == null ? "" : "￥" + orderInfo.goods_amount_hb);
                    tv_freight_again.setText(orderInfo == null ? "" : "运费:￥" + orderInfo.weight_amount_hb);
                    is_use_hb = 1;//使用汇币
                }
            }
        });
    }

    private void initLv() {
        lv_goods.setOverScrollMode(View.OVER_SCROLL_NEVER);
        orderConfirmLvAdapter = new OrderConfirmLvAdapter(this, goodsList);
        lv_goods.setAdapter(orderConfirmLvAdapter);
    }


    private void initData() {
        tv_shop.setText(defaultAddress == null ? "店铺:" : "店铺:" + defaultAddress.store_name);
        tv_name.setText(defaultAddress == null ? "" : defaultAddress.china_name);
        tv_phone.setText(defaultAddress == null ? "" : defaultAddress.mobile_num);
        tv_address.setText(defaultAddress == null ? "收货地址:" : "收货地址:" + defaultAddress.address);
        if(goodsList != null){
            goodsList.add(orderInfo.goods.get(0));
            orderConfirmLvAdapter.notifyDataSetChanged();
            MeasureUtil.setListViewHeightBasedOnChildren(lv_goods);
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
            case R.id.rl_address://订单选择地址列表
                Intent intent1 = new Intent(this, OrderAddressListActivity.class);
                intent1.putExtra("orderInfo", orderInfo);
                startActivityForResult(intent1, REUEST_CODE_ADDRESS);
                break;
            case R.id.rl_order://订单商品列表
                if(exState == 1){
                    exLvList();
                }else if(exState == 2){
                    unexLvList();
                }
                break;
            case R.id.btn_buy://订单商品列表
                goBuy();
                break;
            default:
                break;
        }
    }

    /**
     * 展开列表
     */
    private void exLvList() {
        goodsList.clear();
        goodsList.addAll(orderInfo.goods);
        orderConfirmLvAdapter.notifyDataSetChanged();
        MeasureUtil.setListViewHeightBasedOnChildren(lv_goods);
        tv_ex_desc.setText("收起全部商品");
        iv_ex.setImageResource(R.mipmap.list_gengduo_up);
        exState = 2;
    }

    /**
     * 收起列表
     */
    private void unexLvList() {
        goodsList.clear();
        goodsList.add(orderInfo.goods.get(0));
        orderConfirmLvAdapter.notifyDataSetChanged();
        MeasureUtil.setListViewHeightBasedOnChildren(lv_goods);
        tv_ex_desc.setText("显示全部商品");
        iv_ex.setImageResource(R.mipmap.list_gengduo_h);
        exState = 1;
    }

    private void goBuy() {
        btn_buy.setEnabled(false);//防止重复提交
        long timestamp = System.currentTimeMillis();
        msg_content = et_words.getText().toString().trim();
        MyHttp.addOrder(http, null, carDataAry, timestamp, curAddressId,
                msg_content, is_use_hb, pay_mode, new MyHttp.MyHttpResult() {
                    @Override
                    public void httpResult(Integer which, int code, String msg, Object bean) {
                        btn_buy.setEnabled(true);//防止重复提交
                        showToast(msg);
                        if(code != 0){
                            return;
                        }
                        CartManager.getInstance(OrderConfirmActivity.this).delete(carDataAry);
                        OrderBuyResultInfo orderBuyResultInfo = (OrderBuyResultInfo) bean;
                        Intent intent = new Intent(OrderConfirmActivity.this, OrderBuyResultActivity.class);
                        intent.putExtra("order_id", orderBuyResultInfo.o_id);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REUEST_CODE_ADDRESS && resultCode == RESULT_OK){
            int position = data.getIntExtra("position", 0);
            OrderInfo.OrderAddressBean orderAddressBean = addressList.get(position);
            tv_shop.setText("店铺:" + orderAddressBean.store_name);
            tv_name.setText(orderAddressBean.china_name);
            tv_phone.setText(orderAddressBean.mobile_num);
            tv_address.setText("收货地址:" + orderAddressBean.address);
            if(orderAddressBean.is_default == 1){
                tv_default_address.setVisibility(View.VISIBLE);
            }else {
                tv_default_address.setVisibility(View.GONE);
            }
            curAddressId = orderAddressBean.a_id;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lv_goods.setFocusable(false);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.requestFocus();
    }
}
