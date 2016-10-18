package com.cqfrozen.jsh.cart;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import com.cqfrozen.jsh.home.SearchActivity;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 * 通过商品详情页点击购物车跳转至此页面
 */
public class CartActivity extends MyActivity implements View.OnClickListener, RefreshLayout.OnRefreshListener, RefreshLayout.TopOrBottom, MyActivity.HttpFail {

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
    private int area_id = 5;
    private TextView tv_carr;
    private LinearLayout include_cartnodatalayout;
    private Button include_cartnodata_btn;
    private PopupWindow popupWindow;
    private ImageView iv_shotcut;
    private LinearLayout ll_notify;
    private TextView tv_notify;
    private RefreshLayout refresh_cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTransparencyBar(true);
        cartManager = CartManager.getInstance(this);
        initView();
        initTitle();
        initRV();
        getData();
    }

    private void initView() {
        include_cartnodatalayout = (LinearLayout) findViewById(R.id.include_cartnodatalayout);
        ll_notify = (LinearLayout) findViewById(R.id.ll_notify);
        tv_notify = (TextView) findViewById(R.id.tv_notify);
        include_cartnodata_btn = (Button) findViewById(R.id.include_cartnodata_btn);
        refresh_cart = (RefreshLayout) findViewById(R.id.refresh_cart);
        rv_cart = (RecyclerView) findViewById(R.id.rv_cart);
        iv_shotcut = (ImageView) findViewById(R.id.iv_shotcut);
        cb_all = (CheckBox) findViewById(R.id.cb_all);
        tv_total = (TextView) findViewById(R.id.tv_total);
        btn_order = (Button) findViewById(R.id.btn_order);
        btn_del = (Button) findViewById(R.id.btn_del);
        tv_carr = (TextView)findViewById(R.id.tv_carr);
        btn_del.setOnClickListener(this);
        iv_shotcut.setOnClickListener(this);
        refresh_cart.setOnRefreshListener(this);
//        cb_all.setChecked(true);
        //TODO 以后用application里的用户判断
        if(MyApplication.token.isEmpty()){
            ll_notify.setVisibility(View.GONE);
        }else {
            ll_notify.setVisibility(View.VISIBLE);
        }

        if(cartManager.isNull()){
            setNoDataView();
        }
    }

    private void initTitle() {
        btn_edit = (Button) findViewById(R.id.btn_edit);

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

    private void initRV() {
        rv_cart.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        cartAdapter = new CartRVAdapter(this, cartGoodsInfos, tv_total, cb_all);
        rv_cart.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_cart.addItemDecoration(decoration);
        rv_cart.setAdapter(cartAdapter);
        refresh_cart.setRC(rv_cart, this);
    }

    private void getData() {
        //获取购物车提示消息
        MyHttp.freightTips(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
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
        MyHttp.queryCart(http, null, page, area_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {

                if(code == 404){
                    setHttpFail(CartActivity.this);
                    refresh_cart.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }

                if(code != 0){
                    showToast(msg);
//                    setNoDataView();
                    refresh_cart.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                refresh_cart.setResultState(RefreshLayout.ResultState.success);
                CartResultInfo cartResultInfo = (CartResultInfo) bean;
                if(cartResultInfo == null || cartResultInfo.data1.size() == 0){
                    setNoDataView();//购物车为空
//                    setHttpNotData(CartFragment.this);
                    return;
                }
//                cartGoodsInfos.clear();
                setHttpSuccess();
                is_page = cartResultInfo.is_page;
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
                is_page = cartResultInfo.is_page;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_del://点击删除按钮
                deleteCart();
                break;
            case R.id.iv_shotcut://点击shotcut图标
                showPop(iv_shotcut);
                break;
            case R.id.pop_shortcut_search:
                startActivity(new Intent(this, SearchActivity.class));
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case R.id.pop_shortcut_home:
                startActivity(new Intent(this, HomeActivity.class));
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            default:
                break;
        }
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

    private void doFinish() {
        btn_edit.setText(getString(R.string.cart_finish));
        btn_order.setVisibility(View.GONE);
        btn_del.setVisibility(View.VISIBLE);
        tv_total.setVisibility(View.GONE);
        tv_carr.setVisibility(View.GONE);
        btn_edit.setTag(TAG_FINISH);
        cartAdapter.checkAllNone(false);
    }

    private void doEdit() {
        btn_edit.setText(getString(R.string.cart_edit));
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
//                ((HomeActivity)mActivity).setClassifyFragment();
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });
    }

    @Override
    public void onRefresh() {
        is_page = 0;
        page = 1;
        cartGoodsInfos.clear();
        getData();
    }

    @Override
    public void gotoTop() {

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
    public void move() {

    }

    @Override
    public void stop() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(refresh_cart != null && refresh_cart.isRefreshing){
            refresh_cart.setResultState(RefreshLayout.ResultState.close);
        }
    }

    @Override
    public void toHttpAgain() {
        getData();
    }

    public void showPop(View view){
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_shortcut, null);
        View pop_shortcut_search = popView.findViewById(R.id.pop_shortcut_search);
        View pop_shortcut_home = popView.findViewById(R.id.pop_shortcut_home);
        pop_shortcut_search.setOnClickListener(this);
        pop_shortcut_home.setOnClickListener(this);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view, BaseValue.dp2px(-6), BaseValue.dp2px(8));
    }

}
