package com.cqfrozen.jsh.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.ImgsVPAdapter;
import com.cqfrozen.jsh.entity.GoodDetailResultInfo;
import com.cqfrozen.jsh.widget.HackyViewPager;

import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 * intent.putExtra("position", position);
 * intent.putExtra("pics", (Serializable) picsInfos);
 * intent.putExtra("color", AppUtil.getPaletteColor(bitmap));
 * intent.putExtra("title", goodsName);
 */
public class GoodsVPActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private int position;
    private List<GoodDetailResultInfo.PicsInfo> picsInfos;
    private HackyViewPager vp_pics;
    private RadioGroup rg_goods;
    private RadioButton[] rb_goods;
    private ImageView iv_back;
    private TextView tv_title;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsvp);
        getIntentData();
        initStatusBarColor();
        initView();
        initVP();
        initRadioGroup();
    }

    private void getIntentData() {
        position = getIntent().getIntExtra("position", 0);
        title = getIntent().getStringExtra("title");
        picsInfos = (List<GoodDetailResultInfo.PicsInfo>) getIntent().getSerializableExtra("pics");
    }

    private void initStatusBarColor() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
    }

    private void initView() {

        vp_pics = (HackyViewPager) findViewById(R.id.vp_pics);
        rg_goods = (RadioGroup) findViewById(R.id.rg_goods);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText(title);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initVP() {
        vp_pics.setPageTransformer(true, new CardTransFormer(0.8f));
        ImgsVPAdapter vpAdapter = new ImgsVPAdapter(getSupportFragmentManager(), picsInfos);
        vp_pics.setAdapter(vpAdapter);
        vp_pics.addOnPageChangeListener(this);
    }

    private void initRadioGroup() {
        initPoints(picsInfos);
        rb_goods[position].setChecked(true);
        vp_pics.setCurrentItem(position, false);
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

    private static class CardTransFormer implements ViewPager.PageTransformer{

        private float scaleAmout;
        public CardTransFormer(float scalingStart){
            scaleAmout = 1 - scalingStart;
        }

        @Override
        public void transformPage(View page, float position) {
            if(position >= 0f){
                int width = page.getWidth();
                float scaleFactor = 1 - scaleAmout * position;
                page.setAlpha(1f - position);
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setTranslationX(width * (1 - position) - width);
            }
        }
    }
}
