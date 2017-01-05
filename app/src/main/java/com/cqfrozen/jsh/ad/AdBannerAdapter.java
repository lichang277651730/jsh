package com.cqfrozen.jsh.ad;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.center.WebUrlActivity;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Administrator on 2016/12/28.
 */
public class AdBannerAdapter extends RecyclerView.Adapter<AdBannerAdapter.MyViewHolder>{

    private Context context;
    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos;
    private final HttpForVolley http;
    public AdBannerAdapter(Context context, List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos){
        this.context = context;
        this.http = new HttpForVolley(context);
        this.bannerAdInfos = bannerAdInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ad_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(bannerAdInfos.size() == 0){
            return;
        }
        List<String> bannerTitles = new ArrayList<>();
        List<String> bannerImages = new ArrayList<>();
        for(int i = 0; i < bannerAdInfos.size(); i++){
            bannerTitles.add(bannerAdInfos.get(i).title);
            bannerImages.add(bannerAdInfos.get(i).pic_url);
        }
        if(bannerAdInfos.size() <= 1){
            holder.banner_ad.setAutoPlayAble(false);
        }else {
            holder.banner_ad.setAutoPlayAble(true);
            holder.banner_ad.setPageChangeDuration(3000);
            holder.banner_ad.startAutoPlay();
        }
        holder.banner_ad.setAdapter(new AdBannerVPAdapter(context));
        holder.banner_ad.setData(bannerImages, bannerTitles);

        holder.banner_ad.setOnItemClickListener(new BGABanner.OnItemClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private BGABanner banner_ad;
        public MyViewHolder(View itemView) {
            super(itemView);
            banner_ad = (BGABanner) itemView.findViewById(R.id.banner_ad);
        }
    }
}
