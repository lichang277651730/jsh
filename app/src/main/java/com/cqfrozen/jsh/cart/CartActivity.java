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
import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.entity.CartNotifyInfo;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.OrderInfo;
import com.cqfrozen.jsh.home.SearchActivity;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.order.OrderConfirmActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 * 通过商品详情页点击购物车跳转至此页面
 */
public class CartActivity extends MyActivity implements View.OnClickListener,
        MyActivity.HttpFail, CartManager.OnDeleteCartGoodsActivityListener, SupportLayout.RefreshListener, SupportLayout.LoadMoreListener {

    private static final int TAG_EIDT = 1;
    private static final int TAG_FINISH = 2;

    private static final int REQUEST_CODE_CART_ACTIVITY = 1;
    private static final int REQUEST_CODE_CART_ACTIVITY_ITEM_CLICK = 102;

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
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTransparencyBar(true);
//        cartManager = CartManager.getInstance(this);
        cartManager = MyApplication.cartManager;
        cartManager.setOnDeleteCartGoodsActivityListener(this);
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
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_shotcut = (ImageView) findViewById(R.id.iv_shotcut);
        cb_all = (CheckBox) findViewById(R.id.cb_all);
        tv_total = (TextView) findViewById(R.id.tv_total);
        btn_order = (Button) findViewById(R.id.btn_order);
        btn_del = (Button) findViewById(R.id.btn_del);
        tv_carr = (TextView)findViewById(R.id.tv_carr);
        btn_del.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        iv_shotcut.setOnClickListener(this);

        iv_back.setOnClickListener(this);
//        cb_all.setChecked(true);
        if(MyApplication.userInfo == null){
            ll_notify.setVisibility(View.GONE);
        }else {
            ll_notify.setVisibility(View.VISIBLE);
        }

        if(cartManager.isNull()){
            setNoDataView();
        }

        refresh_cart.setOnRefreshListener(this);
        refresh_cart.setOnLoadMoreListener(this);

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
        cartAdapter = new CartRVAdapter(this, cartGoodsInfos, tv_total, cb_all, btn_order);
        rv_cart.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_cart.addItemDecoration(decoration);
        rv_cart.setAdapter(cartAdapter);

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
                    btn_order.setBackgroundResource(R.drawable.sl_btn_blue_bg);
                }
            }
        });

        cartAdapter.setOnItemClickListener(new CartRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsInfo goodsInfo, int position) {
                Intent intent = new Intent(CartActivity.this, GoodsDetailActivity.class);
//                intent.putExtra("goodsInfo", goodsInfo);
                intent.putExtra("g_id", goodsInfo.g_id);
                startActivityForResult(intent, REQUEST_CODE_CART_ACTIVITY_ITEM_CLICK);
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
                    setHttpFail(CartActivity.this);
                    refresh_cart.setLoadFailed();
                    refresh_cart.setRefreshFailed();
                    return;
                }

                if(code != 0){
//                    showToast(msg);
                    setHttpFail(CartActivity.this);
                    refresh_cart.setLoadFailed();
                    refresh_cart.setRefreshFailed();
                    return;
                }
                refresh_cart.setLoadSuccess();
                refresh_cart.setRefreshSuccess();
                CartResultInfo cartResultInfo = (CartResultInfo) bean;
                is_page = cartResultInfo.is_page;
                if(cartResultInfo == null || cartResultInfo.data1.size() == 0){
                    setNoDataView();//购物车为空
                    return;
                }
                setHttpSuccess();
                //过滤掉下架和缺货商品
                for(int i = 0; i < cartResultInfo.data1.size(); i++){
                    CartGoodsInfo cartGoodsInfo = cartResultInfo.data1.get(i);
                    if(cartGoodsInfo.is_oos == 0 && cartGoodsInfo.status == 1){
                        cartGoodsInfos.add(cartGoodsInfo);
                    }
                }
//                cartGoodsInfos.addAll(cartResultInfo.data1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_del://点击删除按钮
                deleteCart();
                break;
            case R.id.btn_order://点击去结算
                goOrder();
                break;
            case R.id.iv_shotcut://点击shotcut图标
                showPop(iv_shotcut);
                break;
            case R.id.iv_back://点击返回图标
                finish();
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
                Intent intent = new Intent(CartActivity.this, OrderConfirmActivity.class);
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
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CART_ACTIVITY && resultCode == RESULT_OK){
            getDataFromServer();
        }
        if(requestCode == REQUEST_CODE_CART_ACTIVITY_ITEM_CLICK && resultCode == RESULT_OK){
            boolean isAddMore = data.getBooleanExtra("isAddMore", false);
            if(isAddMore){
                is_page = 0;
                page = 1;
                cartGoodsInfos.clear();
                getData();
            }else {
                cartAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDeleteCartGoods(List<CartGoodsInfo> deleteCartGoodsInfos) {
        if(cartManager != null && cartGoodsInfos != null){
            for (CartGoodsInfo goodsinfo : deleteCartGoodsInfos){
                cartGoodsInfos.remove(goodsinfo);
                cartAdapter.showTotalPrice();
                if(cartAdapter.isNull()){
                    setNoDataView();
                }
                cartAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void refresh() {
        is_page = 0;
        page = 1;
        cartGoodsInfos.clear();
        getData();
    }

    @Override
    public void loadMore() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
//            showToast("没有更多数据了!~");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(cartAdapter != null){
            cartAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        is_page = 0;
        page = 1;
        cartGoodsInfos.clear();
        getData();
    }
}
