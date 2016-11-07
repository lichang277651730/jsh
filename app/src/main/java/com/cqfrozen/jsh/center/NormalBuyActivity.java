package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.base.BaseValue;
import com.common.refresh.SupportLayout;
import com.common.widget.MyGridDecoration;
import com.common.refresh.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.NormalBuyAdapter;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.GoodsResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */
public class NormalBuyActivity extends MyActivity implements MyActivity.HttpFail, SupportLayout.RefreshListener, SupportLayout.LoadMoreListener {

    private RefreshLayout refresh_normalbuy;
    private RecyclerView rv_normalbuy;
    private List<GoodsInfo> goodsInfos = new ArrayList<>();
    private NormalBuyAdapter adapter;
    private int page = 1;
    private int is_page = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normalbuy);
        initView();
        initRV();
        getData();
    }

    private void initView() {
        setMyTitle("常用采购");
        refresh_normalbuy = (RefreshLayout) findViewById(R.id.refresh_normalbuy);
        rv_normalbuy = (RecyclerView) findViewById(R.id.rv_normalbuy);
        refresh_normalbuy.setOnRefreshListener(this);
        refresh_normalbuy.setOnLoadMoreListener(this);

    }

    private void initRV() {
        rv_normalbuy.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_normalbuy.setLayoutManager(manager);
        adapter = new NormalBuyAdapter(this, goodsInfos);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_normalbuy.addItemDecoration(decoration);
        rv_normalbuy.setAdapter(adapter);
    }

    private void getData() {
        MyHttp.commonGoodsList(http, null, page, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {

                if(code == 404){
                    setHttpFail(NormalBuyActivity.this);
                    refresh_normalbuy.setRefreshFailed();
                    return;
                }

                if(code != 0){
//                    showToast(msg);
                    refresh_normalbuy.setRefreshFailed();
                    return;
                }
                refresh_normalbuy.setRefreshSuccess();
                GoodsResultInfo goodsResultInfo = (GoodsResultInfo)bean;
                is_page = goodsResultInfo.is_page;
                if(goodsResultInfo == null || goodsResultInfo.data1.size() == 0){
                    setHttpNotData(NormalBuyActivity.this);
                    return;
                }
                setHttpSuccess();
                goodsInfos.addAll(goodsResultInfo.data1);
                adapter.notifyDataSetChanged();
                page++;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(refresh_normalbuy != null){
            if(refresh_normalbuy.isLoading()){
                refresh_normalbuy.setLoadClose();
            }
            if(refresh_normalbuy.isRefreshing()){
                refresh_normalbuy.setRefreshClose();
            }
        }
    }

    @Override
    public void toHttpAgain() {
        getData();
    }


    @Override
    public void refresh() {
        page = 1;
        is_page = 0;
        goodsInfos.clear();
        getData();
    }

    @Override
    public void loadMore() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
            refresh_normalbuy.setLoadNodata();
        }
    }
}
