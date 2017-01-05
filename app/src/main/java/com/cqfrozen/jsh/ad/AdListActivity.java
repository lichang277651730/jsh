package com.cqfrozen.jsh.ad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.base.BaseValue;
import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.common.widget.GridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AdListResultInfo;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */
public class AdListActivity extends MyActivity implements SupportLayout.LoadMoreListener, SupportLayout.RefreshListener, MyHttp.MyHttpResult {

    private RecyclerView rv_adlist;
    private RefreshLayout refresh_adlist;
    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos = new ArrayList<>();
    private List<AdListResultInfo.AdListBeanInfo> adListBeanInfos = new ArrayList<>();
    private int page = 1;
    private int is_page = 0;//0没有下一页 1有下一页
    private static int urlNum = 2; //当前页面是刷新的url数量
    private AdPageAdapter adPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adlist);
        initView();
        initRV();
        getData();
    }

    private void initView() {
        setMyTitle("赚粮票");
        refresh_adlist = (RefreshLayout) findViewById(R.id.refresh_adlist);
        rv_adlist = (RecyclerView) findViewById(R.id.rv_adlist);
        refresh_adlist.setOnLoadMoreListener(this);
        refresh_adlist.setOnRefreshListener(this);
    }

    private void initRV() {
        rv_adlist.setOverScrollMode(View.OVER_SCROLL_NEVER);
        AdBannerAdapter adBannerAdapter = new AdBannerAdapter(this, bannerAdInfos);
        AdListAdapter adListAdapter = new AdListAdapter(this, adListBeanInfos);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_adlist.setLayoutManager(manager);
        GridDecoration newGridDecoration = new GridDecoration(1, BaseValue.dp2px(1),
                getResources().getColor(R.color.dividerLine), true);
        rv_adlist.addItemDecoration(newGridDecoration);
        adPageAdapter = new AdPageAdapter(adBannerAdapter, adListAdapter);
        rv_adlist.setAdapter(adPageAdapter);
    }

    private void getData() {

        MyHttp.adBannerList(http, AdPageAdapter.TYPE_BANNER, 2, this);

        MyHttp.adList(http, AdPageAdapter.TYPE_LIST, page, this);
    }


    @Override
    public void loadMore() {
        if(is_page == 1){
            MyHttp.adList(http, AdPageAdapter.TYPE_LIST, page, this);
        }else if(is_page == 0){
            refresh_adlist.setLoadNodata();
        }
    }

    @Override
    public void refresh() {
        page = 1;
        page = 1;
        is_page = 0;
        bannerAdInfos.clear();
        adListBeanInfos.clear();
        getData();
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        refresh_adlist.setUrlNum();
        if(code != 0){
            refresh_adlist.setLoadFailed();
            if(refresh_adlist.getUrlNum() == urlNum){
                refresh_adlist.setRefreshFailed();
            }
            return;
        }

        switch (which) {
            case AdPageAdapter.TYPE_BANNER:
                bannerAdInfos.clear();
                bannerAdInfos.addAll(((HomeBannerAdResultInfo)bean).data1);
                if(bannerAdInfos.size() == 0){
                    return;
                }
                break;
            case AdPageAdapter.TYPE_LIST:
                AdListResultInfo adListResultInfo = (AdListResultInfo) bean;
                is_page = adListResultInfo.is_page;
                if (refresh_adlist.isLoading() && adListResultInfo.data1.size()!= 0){
                    refresh_adlist.setLoadSuccess();
                }
                if (refresh_adlist.isLoading() && adListResultInfo.data1.size() == 0){
                    refresh_adlist.setLoadNodata();
                }
                adListBeanInfos.addAll(adListResultInfo.data1);
                page++;
                break;
            default:
                break;
        }
        adPageAdapter.notifyDataSetChanged();
        refresh_adlist.setRefreshSuccess();
        if(refresh_adlist.getUrlNum() == urlNum){
            refresh_adlist.setRefreshSuccess();
        }
    }
}
