package com.cqfrozen.jsh.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.common.widget.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.entity.CartNotifyInfo;
import com.cqfrozen.jsh.entity.OrderCartBean;
import com.cqfrozen.jsh.entity.OrderInfo;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.order.OrderConfirmActivity;
import com.cqfrozen.jsh.util.ShortcutPop;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 * 导航栏 购物车页面 fragment
 */
public class CartFragment extends MyFragment implements View.OnClickListener, MyFragment.HttpFail,RefreshLayout.OnRefreshListener, RefreshLayout.TopOrBottom {

    private static final int TAG_EIDT = 1;
    private static final int TAG_FINISH = 2;

    private static CartFragment fragment;
    private Button btn_edit;
    private RecyclerView rv_cart;
    private CheckBox cb_all;
    private TextView tv_total;
    private Button btn_order;
    private Button btn_del;
    private List<CartGoodsInfo> cartGoodsInfos = new ArrayList<>();
    private CartRVAdapter cartAdapter;
    private CartManager cartManager;
    private int page = 1;
    private int is_page = 0;//是否有下一页数据
    private TextView tv_carr;
    private LinearLayout include_cartnodatalayout;
    private Button include_cartnodata_btn;
    private PopupWindow popupWindow;
    private ImageView iv_shotcut;
    private LinearLayout ll_notify;
    private TextView tv_notify;
    private RefreshLayout refresh_cart;
    private List<OrderCartBean> orderCartBeanList = new ArrayList<>();
//    private int page = 1;
//    private int is_page = 1;

    public static CartFragment getInstance(){
        if(fragment == null){
            fragment = new CartFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = this;
        if(view == null){
            view = inflater.inflate(R.layout.fragment_cart, null);
            cartManager = CartManager.getInstance(mActivity);
            initView();
            initTitle();
            initRV();
            getData();
        }
        return view;
    }

    private void initTitle() {
        btn_edit = (Button) view.findViewById(R.id.btn_edit);

        btn_edit.setTag(TAG_EIDT);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int)v.getTag();
                if(tag == TAG_EIDT){//当前是编辑，点击就变成完成
                    doFinish();
                }else if(tag == TAG_FINISH){//当前是完成，点击就变成编辑
                    doEdit();
                }
            }
        });
    }

    private void initView() {
        include_cartnodatalayout = (LinearLayout) view.findViewById(R.id.include_cartnodatalayout);
        ll_notify = (LinearLayout) view.findViewById(R.id.ll_notify);
        tv_notify = (TextView) view.findViewById(R.id.tv_notify);
        include_cartnodata_btn = (Button) view.findViewById(R.id.include_cartnodata_btn);
        refresh_cart = (RefreshLayout) view.findViewById(R.id.refresh_cart);
        rv_cart = (RecyclerView) view.findViewById(R.id.rv_cart);
        iv_shotcut = (ImageView) view.findViewById(R.id.iv_shotcut);
        cb_all = (CheckBox) view.findViewById(R.id.cb_all);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        btn_order = (Button) view.findViewById(R.id.btn_order);
        btn_del = (Button) view.findViewById(R.id.btn_del);
        tv_carr = (TextView) view.findViewById(R.id.tv_carr);
        btn_del.setOnClickListener(this);
        iv_shotcut.setOnClickListener(this);
        refresh_cart.setOnRefreshListener(this);
        btn_order.setOnClickListener(this);
//        cb_all.setChecked(true);
        if(MyApplication.userInfo == null){
            ll_notify.setVisibility(View.GONE);
        }else {
            ll_notify.setVisibility(View.VISIBLE);
        }

        if(cartManager.isNull()){
            setNoDataView();
        }

    }

    private void initRV() {
        rv_cart.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 1);
        cartAdapter = new CartRVAdapter(mActivity, cartGoodsInfos, tv_total, cb_all);
        rv_cart.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_cart.addItemDecoration(decoration);
        rv_cart.setAdapter(cartAdapter);
        refresh_cart.setRC(rv_cart, this);

        cartAdapter.setPriceChangeListener(new CartRVAdapter.PriceChangeListener() {
            @Override
            public void priceChange(float price) {
                if("0.0".equals(price + "")){
                    btn_order.setEnabled(false);
                    btn_order.setTextColor(getResources().getColor(R.color.myblack));
                    btn_order.setBackgroundColor(getResources().getColor(R.color.mygray));
                }else {
                    btn_order.setEnabled(true);
                    btn_order.setTextColor(getResources().getColor(R.color.sl_cart_cb_color));
                    btn_order.setBackgroundColor(getResources().getColor(R.color.mygray));
                    btn_order.setBackgroundResource(R.drawable.sl_btn_red_bg);
                }
            }
        });
    }

    private void getData() {

        //获取购物车提示消息
        MyHttp.freightTips(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
//                    showToast(msg);
                    return;
                }
                CartNotifyInfo cartNotifyInfo = (CartNotifyInfo) bean;
                if(cartNotifyInfo == null){
                    return;
                }
                if(cartNotifyInfo.f_tip.isEmpty()){
                    ll_notify.setVisibility(View.GONE);
                }else {
                    ll_notify.setVisibility(View.VISIBLE);
                    tv_notify.setText(cartNotifyInfo.f_tip);
                }
            }
        });

//        List<CartGoodsInfo> localData = cartManager.loadAllFromSp();//从缓存中读购物车数据
//        if(localData == null || localData.size() == 0){//缓存中无数据,从网络中获取
            getDataFromServer();
//        }else {
//            cartGoodsInfos.clear();
//            cartGoodsInfos.addAll(localData);
//        }
//        cartAdapter.showTotalPrice();
//        cartAdapter.allCheckedListen();
//        cartAdapter.notifyDataSetChanged();
    }

    private void getDataFromServer() {
        MyHttp.queryCart(http, null, page, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {

                if(code == 404){
                    setHttpFail(CartFragment.this);
                    refresh_cart.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }

                if(code != 0){
//                    showToast(msg);
//                    setNoDataView();
                    setHttpFail(CartFragment.this);
                    refresh_cart.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                refresh_cart.setResultState(RefreshLayout.ResultState.success);
                CartResultInfo cartResultInfo = (CartResultInfo) bean;
                is_page = cartResultInfo.is_page;
                if(cartResultInfo == null || cartResultInfo.data1.size() == 0){
                    setNoDataView();//购物车为空
//                    setHttpNotData(CartFragment.this);
                    return;
                }
//                cartGoodsInfos.clear();
                setHttpSuccess();
                cartGoodsInfos.addAll(cartResultInfo.data1);
                if(cartGoodsInfos == null || cartGoodsInfos.size() == 0){
                    setNoDataView();//购物车为空
                    return;
                }
                showDataView();
                cartAdapter.notifyDataSetChanged();
                cartManager.add(cartGoodsInfos);
                cartAdapter.showTotalPrice();
                cartAdapter.allCheckedListen();
                page++;
            }
        });
    }

    /**
     * 购物车有数据时的页面
     */
    private void showDataView() {
        include_cartnodatalayout.setVisibility(View.GONE);
        rv_cart.setVisibility(View.VISIBLE);
    }

    /**
     * 购物车没数据时的页面
     */
    private void setNoDataView() {
        include_cartnodatalayout.setVisibility(View.VISIBLE);
        rv_cart.setVisibility(View.GONE);
        include_cartnodata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转去逛逛页面
                ((HomeActivity)mActivity).setClassifyFragment();
            }
        });
    }

    @Override
    public void onShow() {
        super.onShow();
        cartGoodsInfos.clear();
        is_page = 0;
        page = 1;
        getData();
        doEdit();
        ((HomeActivity)mActivity).setCartNum(cartManager.getCartGoodsNum());
        cartAdapter.showTotalPrice();
    }

    private void doFinish() {
        btn_edit.setText(mActivity.getString(R.string.cart_finish));
        btn_order.setVisibility(View.GONE);
        btn_del.setVisibility(View.VISIBLE);
        tv_total.setVisibility(View.GONE);
        tv_carr.setVisibility(View.GONE);
        btn_edit.setTag(TAG_FINISH);
        cartAdapter.checkAllNone(false);
    }

    private void doEdit() {
        btn_edit.setText(mActivity.getString(R.string.cart_edit));
        btn_order.setVisibility(View.VISIBLE);
        btn_del.setVisibility(View.GONE);
        tv_total.setVisibility(View.VISIBLE);
        tv_carr.setVisibility(View.VISIBLE);
        btn_edit.setTag(TAG_EIDT);
        cartAdapter.checkAllNone(true);
        if(cartAdapter.isNull()){
            setNoDataView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_del://点击删除按钮
                deleteCart();
                break;
            case R.id.iv_shotcut://点击shotcut图标
                ShortcutPop.getInstance(mActivity).showPop(iv_shotcut);
                break;
            case R.id.btn_order://去结算
                goOrder();
                break;
            default:
                break;
        }
    }

    private void goOrder() {
        List<CartGoodsInfo> checkedGoods = cartManager.getCheckedGoods();
        if(checkedGoods.size() == 0){
            showToast("未选择任何商品");
            return;
        }
        btn_order.setEnabled(false);//防止重复提交
        final String carDataAry = parseCartData(checkedGoods);
        long timestamp = System.currentTimeMillis();

        MyHttp.settlement(http, null, carDataAry, timestamp, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                btn_order.setEnabled(true);//防止重复提交
                if(code != 0){
                    showToast(msg);
                    return;
                }
                OrderInfo orderInfo = (OrderInfo) bean;
                Intent intent = new Intent(mActivity, OrderConfirmActivity.class);
                intent.putExtra("orderInfo", orderInfo);
                intent.putExtra("carDataAry", carDataAry);
                startActivity(intent);
            }
        });
    }

    private String parseCartData(List<CartGoodsInfo> checkedGoods) {
        String cartString = "";
        for (CartGoodsInfo goodsInfo : checkedGoods){
            cartString = cartString + goodsInfo.c_id + ",";
        }
        if(cartString.endsWith(",")){
            cartString = cartString.substring(0, cartString.length() - 1);
        }
        return cartString;
    }

    /**
     * 点击删除按钮
     */
    private void deleteCart() {
        cartAdapter.delete();
        if(cartAdapter.isNull()){
            setNoDataView();
        }
    }

    @Override
    public void onRefresh() {
        is_page = 0;
        page = 1;
        cartGoodsInfos.clear();
        getData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(refresh_cart != null && refresh_cart.isRefreshing){
            refresh_cart.setResultState(RefreshLayout.ResultState.close);
        }
    }

    @Override
    public void toHttpAgain() {
        getData();
    }

    @Override
    public void gotoBottom() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
//            showToast("没有更多数据了!~");
        }
    }

    @Override
    public void gotoTop() {

    }

    @Override
    public void move() {

    }

    @Override
    public void stop() {

    }

}
