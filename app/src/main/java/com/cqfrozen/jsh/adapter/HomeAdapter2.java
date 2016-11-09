package com.cqfrozen.jsh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HomeAdapter2 extends RecyclerView.Adapter{
    public static final int TYPE_BANNER = 1;
    public static final int TYPE_CLASSIFY = 2;
    public static final int TYPE_TABLE = 3;
    public static final int TYPE_LIST = 4;

    private HomeBannerAdapter homeBannerAdapter;
    private HomeClassifyAdapter homeClassifyAdapter;
    private HomeNotifyAdapter homeNotifyAdapter;
    private HomeRecommendAdapter homeRecommendAdapter;

    public HomeAdapter2(HomeBannerAdapter homeBannerAdapter, HomeClassifyAdapter homeClassifyAdapter,
                        HomeNotifyAdapter homeNotifyAdapter, HomeRecommendAdapter homeRecommendAdapter){
        this.homeBannerAdapter = homeBannerAdapter;
        this.homeClassifyAdapter = homeClassifyAdapter;
        this.homeNotifyAdapter = homeNotifyAdapter;
        this.homeRecommendAdapter = homeRecommendAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_BANNER){
            return homeBannerAdapter.onCreateViewHolder(parent, viewType);
        }
        if(viewType == TYPE_CLASSIFY){
            return homeClassifyAdapter.onCreateViewHolder(parent, viewType);
        }
        if(viewType == TYPE_TABLE){
            return homeNotifyAdapter.onCreateViewHolder(parent, viewType);
        }
        if(viewType == TYPE_LIST){
            return homeRecommendAdapter.onCreateViewHolder(parent, viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_BANNER:
                homeBannerAdapter.onBindViewHolder((HomeBannerAdapter.MyViewHolder)holder, position);
                break;
            case TYPE_CLASSIFY:
                homeClassifyAdapter.onBindViewHolder((HomeClassifyAdapter.MyViewHolder)holder, position);
                break;
            case TYPE_TABLE:
                homeNotifyAdapter.onBindViewHolder((HomeNotifyAdapter.MyViewHolder)holder, position);
                break;
            case TYPE_LIST:
                homeRecommendAdapter.onBindViewHolder((HomeRecommendAdapter.MyViewHolder)holder, position - 6);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return homeRecommendAdapter.getItemCount() + 6;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_BANNER;
        }
        if(position > 0 && position <= 4){
            return TYPE_CLASSIFY;
        }
        if (position == 5){
            return TYPE_TABLE;
        }
        return TYPE_LIST;
    }
}
