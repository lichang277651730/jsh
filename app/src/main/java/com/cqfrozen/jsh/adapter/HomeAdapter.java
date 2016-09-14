package com.cqfrozen.jsh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HomeAdapter extends RecyclerView.Adapter{
    public static final int TYPE_BANNER = 1;
    public static final int TYPE_CLASSIFY = 2;
    public static final int TYPE_TABLE = 3;
    public static final int TYPE_LIST = 4;

    private HomeBannerAdapter homeBannerAdapter;
    private  HomeClassifyAdapter homeClassifyAdapter;

    public HomeAdapter(HomeBannerAdapter homeBannerAdapter,  HomeClassifyAdapter homeClassifyAdapter ){
        this.homeBannerAdapter = homeBannerAdapter;
        this.homeClassifyAdapter = homeClassifyAdapter;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_BANNER){
            return homeBannerAdapter.onCreateViewHolder(parent, viewType);
        }
        if(viewType == TYPE_CLASSIFY){
            return homeClassifyAdapter.onCreateViewHolder(parent, viewType);
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
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_BANNER;
        }
        if(position > 0 && position <= 4){
            return TYPE_CLASSIFY;
        }
        return TYPE_LIST;
    }
}
