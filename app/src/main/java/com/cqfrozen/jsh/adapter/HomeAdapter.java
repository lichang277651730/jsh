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
    public static final int TYPE_PRICE = 4;
    public static final int TYPE_RECOMMEND = 5;
    public static final int TYPE_POP = 6;
    public static final int TYPE_LIST = 7;

    private HomeBannerAdapter homeBannerAdapter;
    private HomeClassifyAdapter homeClassifyAdapter;
    private HomeHotAdapter homeHotAdapter;
    private HomePriceAdapter homePriceAdapter;
    private HomeRecommendAdapter homeRecommendAdapter;
    private  HomePopAdapter homePopAdapter;

    public HomeAdapter(HomeBannerAdapter homeBannerAdapter,  HomeClassifyAdapter homeClassifyAdapter,
                       HomeHotAdapter homeHotAdapter, HomePriceAdapter homePriceAdapter,
                       HomeRecommendAdapter homeRecommendAdapter,  HomePopAdapter homePopAdapter){
        this.homeBannerAdapter = homeBannerAdapter;
        this.homeClassifyAdapter = homeClassifyAdapter;
        this.homeHotAdapter = homeHotAdapter;
        this.homePriceAdapter = homePriceAdapter;
        this.homeRecommendAdapter = homeRecommendAdapter;
        this.homePopAdapter = homePopAdapter;
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
            return homeHotAdapter.onCreateViewHolder(parent, viewType);
        }
        if(viewType == TYPE_PRICE){
            return homePriceAdapter.onCreateViewHolder(parent, viewType);
        }
        if(viewType == TYPE_RECOMMEND){
            return homeRecommendAdapter.onCreateViewHolder(parent, viewType);
        }
        if(viewType == TYPE_POP){
            return homePopAdapter.onCreateViewHolder(parent, viewType);
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
                homeHotAdapter.onBindViewHolder((HomeHotAdapter.MyViewHolder)holder, position);
                break;
            case TYPE_PRICE:
                homePriceAdapter.onBindViewHolder((HomePriceAdapter.MyViewHolder)holder, position);
                break;
            case TYPE_RECOMMEND:
                homeRecommendAdapter.onBindViewHolder((HomeRecommendAdapter.MyViewHolder)holder, position);
                break;
            case TYPE_POP:
                homePopAdapter.onBindViewHolder((HomePopAdapter.MyViewHolder)holder, position);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 9;
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
        if (position == 6){
            return TYPE_PRICE;
        }
        if (position == 7){
            return TYPE_RECOMMEND;
        }
        if (position == 8){
            return TYPE_POP;
        }
        return TYPE_LIST;
    }
}
