package com.cqfrozen.jsh.ad;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/12/29.
 */
public class AdPageAdapter extends RecyclerView.Adapter{

    public static final int TYPE_BANNER = 1;
    public static final int TYPE_LIST = 2;

    private AdBannerAdapter adBannerAdapter;
    private AdListAdapter adListAdapter;
    public AdPageAdapter(AdBannerAdapter adBannerAdapter, AdListAdapter adListAdapter){
        this.adBannerAdapter = adBannerAdapter;
        this.adListAdapter = adListAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_BANNER){
            return adBannerAdapter.onCreateViewHolder(parent, viewType);
        }
        if(viewType == TYPE_LIST){
            return adListAdapter.onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_BANNER:
                adBannerAdapter.onBindViewHolder((AdBannerAdapter.MyViewHolder)holder, position);
                break;
            case TYPE_LIST:
                adListAdapter.onBindViewHolder((AdListAdapter.MyViewHolder)holder, position - 1);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return adListAdapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_BANNER;
        }
        return TYPE_LIST;
    }
}
