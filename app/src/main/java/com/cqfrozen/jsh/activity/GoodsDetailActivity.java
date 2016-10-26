package com.cqfrozen.jsh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.CommentAdapter;
import com.cqfrozen.jsh.adapter.GoodsDetailVPAdapter;
import com.cqfrozen.jsh.cart.CartActivity;
import com.cqfrozen.jsh.cart.CartManager;
import com.cqfrozen.jsh.cart.CartResultInfo;
import com.cqfrozen.jsh.entity.CommentResultInfo;
import com.cqfrozen.jsh.entity.GoodDetailResultInfo;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.MeasureUtil;
import com.cqfrozen.jsh.util.SharePop;
import com.cqfrozen.jsh.util.ToastUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.cqfrozen.jsh.widget.BadgeView;
import com.cqfrozen.jsh.widget.NumberAddSubView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 * 商品详情页
 * GoodsAdapter NormalBuyAdapter的itemview点击跳转
 * intent.putExtra("g_id", goodsInfo.g_id);
 * intent.putExtra("goodsInfo", goodsInfo);
 */
public class GoodsDetailActivity extends MyActivity implements View.OnClickListener, ViewPager
        .OnPageChangeListener {


    private ImageView iv_share;
    private ImageView iv_back;
    private ImageView iv_cart;
    private Long g_id;
    private ViewPager vp_goodspics;
    private List<GoodDetailResultInfo.PicsInfo> picsInfos = new ArrayList<>();
    private GoodsDetailVPAdapter vpAdapter;
    private RadioGroup rg_goods;
    private RadioButton[] rb_goods;
    private TextView tv_name;
    private TextView tv_price;
    private TextView tv_size;
    private NumberAddSubView asv_num;
    private TextView tv_brand;
    private TextView tv_detail;
    private TextView tv_server;
    private TextView tv_send;
    private TextView tv_sendprice;
    private LinearLayout ll_collect;
    private LinearLayout ll_cart;
    private ImageView iv_collect;

    private boolean canClick = true;//添加常用采购控件的是否能点击
    private TextView tv_collect;
    private int is_common = 0;//常用采购
    private int type;//type=1添加常用采购  type=2取消常用采购
    private TextView tv_add_cart;
    private CartManager cartManager;
    private GoodsInfo goodsInfo;
    private BadgeView badgeView;
    private int addCount = 1;
    private ListView lv_comment;
    private List<CommentResultInfo.CommentInfo> commentInfos = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private TextView tv_comment_count;
    private int page = 1;
    private TextView tv_all_comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        setSwipeBackEnable(false);
        cartManager = CartManager.getInstance(this);
        getIntentData();
        initView();
        initBadgeView();
        initVP();
        initLV();
        getData();
    }

    private void getIntentData() {
        goodsInfo = (GoodsInfo) getIntent().getSerializableExtra("goodsInfo");
        g_id = goodsInfo.g_id;
    }

    private void initView() {
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_cart = (ImageView) findViewById(R.id.iv_cart);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_size = (TextView) findViewById(R.id.tv_size);
        asv_num = (NumberAddSubView) findViewById(R.id.asv_num);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_server = (TextView) findViewById(R.id.tv_server);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_sendprice = (TextView) findViewById(R.id.tv_sendprice);
        vp_goodspics = (ViewPager) findViewById(R.id.vp_goodspics);
        rg_goods = (RadioGroup) findViewById(R.id.rg_goods);
        ll_collect = (LinearLayout) findViewById(R.id.ll_collect);
        ll_cart = (LinearLayout) findViewById(R.id.ll_cart);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        tv_collect = (TextView) findViewById(R.id.tv_collect);
        tv_add_cart = (TextView) findViewById(R.id.tv_add_cart);
        tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);
        tv_all_comment = (TextView) findViewById(R.id.tv_all_comment);
        tv_all_comment.setVisibility(View.GONE);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        ll_cart.setOnClickListener(this);
        tv_add_cart.setOnClickListener(this);
        tv_all_comment.setOnClickListener(this);
        asv_num.setCurValue(1);
    }

    private void initLV() {
        lv_comment.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void initBadgeView() {
        getCartNumFromServer();
        badgeView = new BadgeView(this, iv_cart);
        badgeView.setEnabled(false);
        badgeView.setFocusable(false);
        if(cartManager != null){
            badgeView.setVisibility(View.VISIBLE);
//            badgeView.setText(cartManager.getCartGoodsNum() + "");
            badgeView.setText(0 + "");
            badgeView.setTextSize(10);
            badgeView.setBadgeMargin(10, 0);
            badgeView.show();
//            cartManager.setOnNumChangeListener(new CartManager.OnNumChangeListener() {
//                @Override
//                public void onNumChangeListener(int curNum) {
//                    if(!badgeView.isShown()){
//                        badgeView.show();
//                    }
//                    if(badgeView != null && badgeView.isShown()){
//                        if(curNum >= 100){
//                            badgeView.setText("99+");
//                        }else {
//                            badgeView.setText(curNum + "");
//                        }
//                    }
//                }
//            });
        }else {
            badgeView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(badgeView != null){
            getCartNumFromServer();
        }
    }

    /**
     * 获取购物车数量
     */
    private void getCartNumFromServer() {
        MyHttp.queryCart(http, null, 1, new MyHttp.MyHttpResult() {

            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {

                if(code == 404){
                    return;
                }

                if(code != 0){
                    return;
                }
                CartResultInfo cartResultInfo = (CartResultInfo) bean;
                if(cartResultInfo == null || cartResultInfo.data1.size() == 0){
                    return;
                }
                int count = 0;
                for(int i = 0; i < cartResultInfo.data1.size(); i++){
                    count += cartResultInfo.data1.get(i).count;
                }
                if(count >= 100){
                    badgeView.setText("99+");
                }else {
                    badgeView.setText(count + "");
                }
            }
        });
    }

    private void initVP() {
        vp_goodspics.setOverScrollMode(View.OVER_SCROLL_NEVER);
        vpAdapter = new GoodsDetailVPAdapter(this, picsInfos);
        vp_goodspics.setAdapter(vpAdapter);
        vp_goodspics.addOnPageChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                setShare();
                break;
            case R.id.ll_collect:
                if (!needLogin()) {
                    return;
                }
                setNormalBuy();//添加常用采购
                break;
            case R.id.ll_cart://跳转到购物车页面
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
//                finish();
                break;
            case R.id.tv_add_cart:
                if (!needLogin()) {
                    return;
                }
                addCart();//添加常用采购
                break;
            case R.id.tv_all_comment:
                Intent intent2 = new Intent(this, CommentListActivity.class);
                intent2.putExtra("g_id", g_id);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    /**
     * 添加到购物车
     */
    private void addCart() {
        int curValue = asv_num.getCurValue();
        if(curValue != 0){
            addCount = curValue;
        }

        MyHttp.addcart(http, null, g_id, addCount, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                ToastUtil.showToast(GoodsDetailActivity.this, response.optString("msg"));
                int code = response.optInt("code");
                if(code != 0){
                    return;
                }
                getCartNumFromServer();
                cartManager.add(goodsInfo);
            }
        });
    }

    /**
     * 添加或取消常用采购
     */
    private void setNormalBuy() {
        if (!canClick) {
            return;
        }
        canClick = false;
        if (is_common == 0) {//添加常用采购
            type = 1;
        } else if (is_common == 1) {//取消常用采购
            type = 2;
        }
        MyHttp.addCancelComm(http, null, type, g_id, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                canClick = true;
                showToast(response.optString("msg"));
                int code = response.optInt("code");
                if (code != 0) {
                    return;
                }
                if (type == 1) {
                    iv_collect.setImageResource(R.mipmap.icon_normal_buy_yes);
                    tv_collect.setText("取消常用");
                    is_common = 1;
                } else if (type == 2) {
                    iv_collect.setImageResource(R.mipmap.icon_normal_buy_no);
                    tv_collect.setText("常用采购");
                    is_common = 0;
                }
            }
        });
    }

    private void setShare() {
        SharePop.getInstance(this).showPop(iv_share);
    }

    private void getData() {
        MyHttp.ginfo(http, null, g_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                GoodDetailResultInfo resultInfo = (GoodDetailResultInfo) bean;
                if (resultInfo == null || resultInfo.data2.size() == 0) {
                    return;
                }
                GoodDetailResultInfo.GoodDetailInfo goodDetailInfo = resultInfo.data1;
                setViewInfo(goodDetailInfo);
                picsInfos.clear();
                picsInfos.addAll(resultInfo.data2);
                vpAdapter.notifyDataSetChanged();
                initPoints(picsInfos);
                rb_goods[0].setChecked(true);
                vp_goodspics.setCurrentItem(0, false);

            }
        });

        MyHttp.pjList(http, null, page, g_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                CommentResultInfo commentResultInfo = (CommentResultInfo) bean;
                commentInfos.addAll(commentResultInfo.data1);

                Log.d("commentInfos", commentInfos.size() + "");
                if(commentInfos.size() == 0){
                    tv_all_comment.setVisibility(View.GONE);
                    return;
                }
//                if(commentInfos.size() > 5){
//                    tv_all_comment.setVisibility(View.VISIBLE);
//                    commentInfos = commentInfos.subList(0, 5);
//                }
                if(commentInfos.size() > 2){
                    tv_all_comment.setVisibility(View.VISIBLE);
                    commentInfos = commentInfos.subList(0, 1);
                }

                commentAdapter = new CommentAdapter(GoodsDetailActivity.this, commentInfos);
                lv_comment.setAdapter(commentAdapter);
                MeasureUtil.setListViewHeightBasedOnChildren(lv_comment);
            }
        });
    }

    /**
     * 设置页面组件的信息
     */
    private void setViewInfo(GoodDetailResultInfo.GoodDetailInfo goodDetailInfo) {
        tv_name.setText(goodDetailInfo.g_name);
        tv_price.setText("￥" + goodDetailInfo.now_price);
        tv_size.setText("规格 " + goodDetailInfo.weight + "kg/件");
        tv_brand.setText(goodDetailInfo.brand_name);
        tv_detail.setText(goodDetailInfo.g_introduction);
        tv_server.setText(goodDetailInfo.shelf_life);
        tv_send.setText("满399包邮");
        tv_sendprice.setText(goodDetailInfo.c_mode);
        is_common = goodDetailInfo.is_common;
        if("0".equals(goodDetailInfo.pj_count)){
            tv_comment_count.setText("商品评论(暂无评论)");
        }else {
            tv_comment_count.setText("商品评论(" + goodDetailInfo.pj_count + ")");
        }
        if (goodDetailInfo.is_common == 0) {
            iv_collect.setImageResource(R.mipmap.icon_normal_buy_no);
            tv_collect.setText("常用采购");
        } else if (goodDetailInfo.is_common == 1) {
            iv_collect.setImageResource(R.mipmap.icon_normal_buy_yes);
            tv_collect.setText("取消常用");
        }

    }

    /**
     * 生成轮播图的圆点
     */
    private void initPoints(List<GoodDetailResultInfo.PicsInfo> picsInfos) {
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(BaseValue.dp2px(4),
                BaseValue.dp2px(4));
        rb_goods = new RadioButton[picsInfos.size()];
        params.leftMargin = BaseValue.dp2px(2);
        rg_goods.removeAllViews();
        for (int i = 0; i < picsInfos.size(); i++) {
            RadioButton rb = new RadioButton(this);
            rb.setLayoutParams(params);
            rb.setPadding(BaseValue.dp2px(2), 0, 0, 0);
            rb.setBackgroundResource(R.drawable.sl_viewpager_dot);
            rb.setButtonDrawable(R.color.transparency);
            rb.setEnabled(false);
            rb_goods[i] = rb;
            rg_goods.addView(rb);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        rb_goods[position].setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
