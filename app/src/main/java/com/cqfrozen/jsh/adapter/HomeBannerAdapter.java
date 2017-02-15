package com.cqfrozen.jsh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.ad.AdBannerVPAdapter;
import com.cqfrozen.jsh.ad.BannerDetailActivity;
import com.cqfrozen.jsh.center.WebUrlActivity;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Administrator on 2016/9/13.
 *首页 轮播条 数据适配器
 */
public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.MyViewHolder>{

    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos;
    private RadioButton[] rb_points;
    private Timer timer;
    private Context context;
    private final HttpForVolley http;

    public HomeBannerAdapter(Context context, List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos){
        this.context = context;
        this.bannerAdInfos = bannerAdInfos;
        this.http = new HttpForVolley(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_homebanner, null));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(bannerAdInfos.size() == 0){
            return;
        }
        List<String> bannerTitles = new ArrayList<>();
        List<String> bannerImages = new ArrayList<>();
        for(int i = 0; i < bannerAdInfos.size(); i++){
            bannerTitles.add("");
            bannerImages.add(bannerAdInfos.get(i).pic_url);
        }
        if(bannerAdInfos.size() <= 1){
            holder.vp_homebanner.setAutoPlayAble(false);
        }else {
            holder.vp_homebanner.setAutoPlayAble(true);
            holder.vp_homebanner.setPageChangeDuration(5000);
            holder.vp_homebanner.startAutoPlay();
        }
        holder.vp_homebanner.setAdapter(new AdBannerVPAdapter(context));
        holder.vp_homebanner.setData(bannerImages, bannerTitles);

        holder.vp_homebanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
                HomeBannerAdResultInfo.HomeBannerAdInfo bannerAdInfo = bannerAdInfos.get(position);
//                1图文，2外链接，3不看详情，4粮票（图文），5粮票（外链），6购买[返回商品id]
                switch (bannerAdInfo.content_type) {
                    case 1:
                    case 2:
                        MyHttp.adRead(http, null, bannerAdInfo.ad_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                            }
                        });
                        Intent intent2 = new Intent(context, WebUrlActivity.class);
                        intent2.putExtra("title", bannerAdInfo.title);
                        intent2.putExtra("url", bannerAdInfo.content);
                        context.startActivity(intent2);
                        break;
                    case 3:
                        break;
                    case 4:
                    case 5:
                        MyHttp.adRead(http, null, bannerAdInfo.ad_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                            }
                        });
                        Intent intent = new Intent(context, BannerDetailActivity.class);
                        intent.putExtra("ad_id", bannerAdInfo.ad_id);
                        context.startActivity(intent);
                        break;
                    case 6:
                        MyHttp.adRead(http, null, bannerAdInfo.ad_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                            }
                        });
                        Intent intent1 = new Intent(context, GoodsDetailActivity.class);
                        intent1.putExtra("g_id", Long.parseLong(bannerAdInfo.content));
                        context.startActivity(intent1);
                        break;
                }
            }
        });
//        //给vp_homebanner设置adapter
//        holder.vp_homebanner.setAdapter(new HomeBannerVPAdapter(context, bannerAdInfos));
//        //设置圆点数
//        setPoints(holder.rg_homebanner);
//        //给vp_homebanner设置页面滑动监听事件
////        holder.vp_homebanner.addOnPageChangeListener(this);
//        holder.vp_homebanner.setOnMyPageChangeListener(this);
//        //默认第一页 第一个点选中
//        holder.vp_homebanner.setCurrentItem(bannerAdInfos.size() * 1000, false);
//        rb_points[0].setChecked(true);
//        //设置无限轮播
//        setLoopPlay(holder.vp_homebanner);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private BGABanner vp_homebanner;
//        private MyViewPager vp_homebanner;
//        private RadioGroup rg_homebanner;
        public MyViewHolder(View itemView) {
            super(itemView);
            vp_homebanner = (BGABanner) itemView.findViewById(R.id.vp_homebanner);
//            vp_homebanner = (MyViewPager) itemView.findViewById(R.id.vp_homebanner);
//            rg_homebanner = (RadioGroup) itemView.findViewById(R.id.rg_homebanner);
        }
    }

    /**
     * 设置圆点
     */
    private void setPoints(RadioGroup rg_homebanner) {
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(BaseValue.dp2px(4),
                BaseValue.dp2px(4));
        rb_points = new RadioButton[bannerAdInfos.size()];
        params.leftMargin = BaseValue.dp2px(2);
        rg_homebanner.removeAllViews();
        for(int i = 0; i < bannerAdInfos.size(); i++){
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

//    @Override
//    public void OnMyPageSelected(int position) {
//        rb_points[position % bannerAdInfos.size()].setChecked(true);
//    }
//
//    @Override
//    public void OnMyPonPageScrolled(int arg0, float arg1, int arg2) {
//
//    }
//
//    @Override
//    public void OnMyPageScrollStateChanged(int arg0) {
//
//    }
}
