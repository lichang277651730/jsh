package com.cqfrozen.jsh.ad;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.center.WebUrlActivity;
import com.cqfrozen.jsh.entity.AdListResultInfo;
import com.cqfrozen.jsh.util.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 */
public class AdListAdapter extends RecyclerView.Adapter<AdListAdapter.MyViewHolder> {

    private Context context;
    private List<AdListResultInfo.AdListBeanInfo> adListBeanInfos;
    public AdListAdapter(Context context, List<AdListResultInfo.AdListBeanInfo> adListBeanInfos){
        this.context = context;
        this.adListBeanInfos = adListBeanInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ad_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AdListResultInfo.AdListBeanInfo adListBeanInfo = adListBeanInfos.get(position);
        ImageLoader.load(context, adListBeanInfo.pic_url, holder.iv_ad);
        if(adListBeanInfo.content_type == 4 || adListBeanInfo.content_type == 5){
            holder.tv_get_huibi.setVisibility(View.VISIBLE);
        }else {
            holder.tv_get_huibi.setVisibility(View.GONE);
        }
        holder.tv_title.setText(adListBeanInfo.title);
        holder.tv_time.setText("发布时间：" + adListBeanInfo.start_time);
        holder.tv_introduction.setText("广告简介：" + adListBeanInfo.introduction);
        if(adListBeanInfo.ad_status == 2){
            holder.iv_over.setVisibility(View.VISIBLE);
        }else {
            holder.iv_over.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                1图文，2外链接，3不看详情，4粮票（图文），5粮票（外链），6购买[返回商品id]
                switch (adListBeanInfo.content_type) {
                    case 1:
                    case 2:
                        Intent intent2 = new Intent(context, WebUrlActivity.class);
                        intent2.putExtra("title", adListBeanInfo.title);
                        intent2.putExtra("url", adListBeanInfo.content);
                        context.startActivity(intent2);
                        break;
                    case 3:
                        break;
                    case 4:
                    case 5:
                        Intent intent = new Intent(context, BannerDetailActivity.class);
                        intent.putExtra("ad_id", adListBeanInfo.ad_id);
                        context.startActivity(intent);
                        break;
                    case 6:
                        Intent intent1 = new Intent(context, GoodsDetailActivity.class);
                        intent1.putExtra("g_id", Long.parseLong(adListBeanInfo.content));
                        context.startActivity(intent1);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adListBeanInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_ad;
        private TextView tv_get_huibi;
        private TextView tv_title;
        private TextView tv_time;
        private TextView tv_introduction;
        private ImageView iv_over;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_ad = (ImageView) itemView.findViewById(R.id.iv_ad);
            tv_get_huibi = (TextView) itemView.findViewById(R.id.tv_get_huibi);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_introduction = (TextView) itemView.findViewById(R.id.tv_introduction);
            iv_over = (ImageView) itemView.findViewById(R.id.iv_over);
        }
    }
}
