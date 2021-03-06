package com.cqfrozen.jsh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.common.widget.GridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.GoodsAdapter;
import com.cqfrozen.jsh.adapter.NormalBuyAdapter;
import com.cqfrozen.jsh.constants.Where;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.GoodsResultInfo;
import com.cqfrozen.jsh.home.SearchResultActivity;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class GoodsFragment extends MyFragment implements MyHttp.MyHttpResult, MyFragment.HttpFail, SupportLayout.RefreshListener, SupportLayout.LoadMoreListener {

    private TextView tv_name;
    private String title;
    private int where;
    private RefreshLayout refresh_goods;
    private RecyclerView rv_goods;
    private List<GoodsInfo> goodsInfos = new ArrayList<>();
    private GoodsAdapter goodsAdapter;

    private String g_type_id;
    private int page = 1;
    private int is_page = 0;

    private String keyword;
    private int sort;
    private int order;
    private GridLayoutManager gvmanager;
    private GridLayoutManager lvManager;
    private NormalBuyAdapter normalBuyAdapter;

    public static GoodsFragment getInstance(String title){
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static GoodsFragment getInstanceForClassify(String g_type_id){
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("where", Where.CLASSIFY);
        bundle.putString("g_type_id", g_type_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static GoodsFragment getInstanceForSearch(String keyword, int sort, int order) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        bundle.putInt("where", Where.SEARCH);
        bundle.putInt("sort", sort);
        bundle.putInt("order", order);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setSearchValue(int sort, int order) {
        this.sort = sort;
        this.order = order;
        page = 1;
        is_page = 0;
        goodsInfos.clear();
        getData();
    }

    public void setSelection(int index){
        if(rv_goods.getChildCount() != 0){
            rv_goods.scrollToPosition(index);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            getBundleData(getArguments());
            view = inflater.inflate(R.layout.fragment_goods, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initRV();
        getData();
    }

    private void initView(View view) {
        refresh_goods = (RefreshLayout) view.findViewById(R.id.refresh_goods);
        rv_goods = (RecyclerView) view.findViewById(R.id.rv_goods);
        refresh_goods.setOnRefreshListener(this);
        refresh_goods.setOnLoadMoreListener(this);
    }

    private void initRV() {
        rv_goods.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gvmanager = new GridLayoutManager(mActivity, 2);
        lvManager = new GridLayoutManager(mActivity, 1);
        GridDecoration newGridDecoration = new GridDecoration(0, BaseValue.dp2px(4),
                getResources().getColor(R.color.mybg), true);
        rv_goods.addItemDecoration(newGridDecoration);
        goodsAdapter = new GoodsAdapter(mActivity, goodsInfos);
        normalBuyAdapter = new NormalBuyAdapter(mActivity, goodsInfos);
        rv_goods.setLayoutManager(gvmanager);
        rv_goods.setAdapter(goodsAdapter);

    }

    private void getData() {
        switch (this.where) {
            case Where.CLASSIFY:
                refresh_goods.setRefreshable(true);
                MyHttp.goodstypeList(http, null, page, g_type_id, this);
                break;
            case Where.SEARCH:
                refresh_goods.setRefreshable(true);
                MyHttp.goodsSearch(http, null, page, keyword, sort, order, this);
                break;
            default:
                break;
        }
    }

    private void getBundleData(Bundle bundle) {
        this.where = bundle.getInt("where");
        switch (this.where) {
            case Where.CLASSIFY:
                this.g_type_id = bundle.getString("g_type_id");
                break;
            case Where.SEARCH:
                this.keyword = bundle.getString("keyword");
                this.sort = bundle.getInt("sort", SearchResultActivity.SortType.DEFAULT);
                this.order = bundle.getInt("order", SearchResultActivity.OrderType.ASC);
                break;
            default:
                break;
        }
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if(code == 404){
            refresh_goods.setRefreshFailed();
            refresh_goods.setLoadFailed();
            setHttpFail(this);
            return;
        }

        if(code != 0){
            refresh_goods.setLoadFailed();
            refresh_goods.setRefreshFailed();
            setHttpFail(this);
            return;
        }
        refresh_goods.setRefreshSuccess();
        refresh_goods.setLoadSuccess();
        GoodsResultInfo goodsResultInfo = (GoodsResultInfo) bean;
        is_page = goodsResultInfo.is_page;
        goodsInfos.addAll(goodsResultInfo.data1);
        if(goodsInfos.size() == 0){
            setHttpNotData(this);
            return;
        }
        setHttpSuccess();
        goodsAdapter.notifyDataSetChanged();
        normalBuyAdapter.notifyDataSetChanged();
        page++;
    }

    //没有数据，请求失败 点击重试
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
            refresh_goods.setLoadNodata();
        }
    }


    public void changeView(int viewType){
        if(viewType == SearchResultActivity.ViewType.TYPE_GV){
            rv_goods.setLayoutManager(gvmanager);
            rv_goods.setAdapter(goodsAdapter);
        }else if(viewType == SearchResultActivity.ViewType.TYPE_LV){
            rv_goods.setLayoutManager(lvManager);
            rv_goods.setAdapter(normalBuyAdapter);
        }
    }

}
