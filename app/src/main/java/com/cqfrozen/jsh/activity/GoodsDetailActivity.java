package com.cqfrozen.jsh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.GoodsDetailVPAdapter;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.SharePop;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.cqfrozen.jsh.widget.NumberAddSubView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 * GoodsAdapter 的itemview点击跳转
 * intent.putExtra("g_id", goodsInfo.g_id);
 */
public class GoodsDetailActivity extends MyActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ImageView iv_share;
    private ImageView iv_back;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        getIntentData();
        initView();
        initVP();
        getData();
    }

    private void getIntentData() {
        g_id = getIntent().getLongExtra("g_id", 0L);
    }

    private void initView() {
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_back = (ImageView) findViewById(R.id.iv_back);
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
        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        asv_num.setCurValue(1);
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
            default:
                break;
        }
    }

    private void setShare() {
        SharePop.getInstance(this).showPop(iv_share);
    }


    private void getData() {
        MyHttp.ginfo(http, null, g_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                GoodDetailResultInfo resultInfo = (GoodDetailResultInfo) bean;
                showToast(resultInfo.data2.size() + "");
                picsInfos.clear();
                if(resultInfo == null || resultInfo.data2.size() == 0){
                    return;
                }
                GoodDetailResultInfo.GoodDetailInfo goodDetailInfo = resultInfo.data1;
                setViewInfo(goodDetailInfo);
                picsInfos.addAll(resultInfo.data2);
                vpAdapter.notifyDataSetChanged();
                initPoints(picsInfos);
                rb_goods[0].setChecked(true);
                vp_goodspics.setCurrentItem(0, false);

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
        for(int i = 0; i < picsInfos.size(); i++){
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
