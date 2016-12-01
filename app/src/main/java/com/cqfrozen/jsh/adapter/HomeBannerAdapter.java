package com.cqfrozen.jsh.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cqfrozen.jsh.R;
import com.common.base.BaseValue;
import com.cqfrozen.jsh.entity.HomeBannerInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/9/13.
 *首页 轮播条 数据适配器
 */
public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.MyViewHolder> implements ViewPager.OnPageChangeListener {

    private List<HomeBannerInfo> bannerInfos;
    private RadioButton[] rb_points;
    private Timer timer;
    private Context context;
    public HomeBannerAdapter(Context context, List<HomeBannerInfo> bannerInfos){
        this.context = context;
        this.bannerInfos = bannerInfos;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_homebanner, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(bannerInfos.size() == 0){
            return;
        }
        //给vp_homebanner设置adapter
        holder.vp_homebanner.setAdapter(new HomeBannerVPAdapter(context, bannerInfos));
        //设置圆点数
        setPoints(holder.rg_homebanner);
        //给vp_homebanner设置页面滑动监听事件
        holder.vp_homebanner.addOnPageChangeListener(this);
        //默认第一页 第一个点选中
        holder.vp_homebanner.setCurrentItem(bannerInfos.size() * 1000, false);
        rb_points[0].setChecked(true);
        //设置无限轮播
        setLoopPlay(holder.vp_homebanner);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ViewPager vp_homebanner;
        private RadioGroup rg_homebanner;
        public MyViewHolder(View itemView) {
            super(itemView);
            vp_homebanner = (ViewPager) itemView.findViewById(R.id.vp_homebanner);
            rg_homebanner = (RadioGroup) itemView.findViewById(R.id.rg_homebanner);
        }
    }

    /**
     * 设置圆点
     */
    private void setPoints(RadioGroup rg_homebanner) {
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(BaseValue.dp2px(4),
                BaseValue.dp2px(4));
        rb_points = new RadioButton[bannerInfos.size()];
        params.leftMargin = BaseValue.dp2px(2);
        rg_homebanner.removeAllViews();
        for(int i = 0; i < bannerInfos.size(); i++){
            RadioButton rb = new RadioButton(context);
            rb.setLayoutParams(params);
            rb.setPadding(BaseValue.dp2px(2), 0, 0, 0);
            rb.setBackgroundResource(R.drawable.sl_viewpager_dot);
            rb.setButtonDrawable(R.color.transparency);
            rb.setEnabled(false);
            rb_points[i] = rb;
            rg_homebanner.addView(rb);
        }
    }

    /**
     * 设置无限轮播
     * @param vp_homebanner
     */
    private void setLoopPlay(final ViewPager vp_homebanner) {
        if(timer != null){
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vp_homebanner.setCurrentItem(vp_homebanner.getCurrentItem() + 1, false);
                    }
                });
            }
        }, 5000, 5000);
    }

    //viewpager页面滑动监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        rb_points[position % bannerInfos.size()].setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
