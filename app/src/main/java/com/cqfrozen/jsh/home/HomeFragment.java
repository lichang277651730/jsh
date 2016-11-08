package com.cqfrozen.jsh.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.common.base.BaseFragment;
import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HomeAdapter;
import com.cqfrozen.jsh.adapter.HomeBannerAdapter;
import com.cqfrozen.jsh.adapter.HomeClassifyAdapter;
import com.cqfrozen.jsh.adapter.HomeNotifyAdapter;
import com.cqfrozen.jsh.adapter.HomePopAdapter;
import com.cqfrozen.jsh.adapter.HomePriceAdapter;
import com.cqfrozen.jsh.adapter.HomeRecommendAdapter;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.entity.HomeClassifyInfo;
import com.cqfrozen.jsh.entity.HomeNotifyInfo;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomeFragment extends BaseFragment implements MyHttp.MyHttpResult ,View.OnTouchListener, View.OnClickListener, SupportLayout.RefreshListener, SupportLayout.LoadMoreListener {

    private static HomeFragment fragment;
    private List<HomeBannerInfo> bannerInfos = new ArrayList<>();
    private List<HomeClassifyInfo> classifyInfos = new ArrayList<>();
    private List<HomeNotifyInfo> notifyInfos = new ArrayList<>();
    private List<GoodsInfo> priceGoods = new ArrayList<>();
    private List<GoodsInfo> recommendGoods = new ArrayList<>();
    private List<GoodsInfo> popGoods = new ArrayList<>();
    private RecyclerView rv_home;
    private HomeAdapter homeAdapter;
    private EditText et_search;
    private LinearLayout ll_search;
    private RefreshLayout refresh_home;

    private static final int urlNum = 5; //当前页面是刷新的url数量
    private ImageView iv_search;
    private ImageView iv_location;

    public static HomeFragment getInstance(){
        if(fragment == null){
            fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = this;
        if(view == null){
            view = inflater.inflate(R.layout.fragment_home, null);
            initView();
            initRV();
            getData();
        }
        return view;
    }

    private void initView() {
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        refresh_home = (RefreshLayout) view.findViewById(R.id.refresh_home);
        et_search = (EditText) view.findViewById(R.id.et_search);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_location = (ImageView) view.findViewById(R.id.iv_location);
        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
        et_search.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_location.setOnClickListener(this);
        refresh_home.setOnLoadMoreListener(this);
//        refresh_home.setLoadClose();
        refresh_home.setOnRefreshListener(this);
    }


    private void initRV() {
        rv_home.setOverScrollMode(View.OVER_SCROLL_NEVER);
        HomeBannerAdapter homeBannerAdapter = new HomeBannerAdapter(mActivity, bannerInfos);
        HomeClassifyAdapter homeClassifyAdapter = new HomeClassifyAdapter(mActivity, classifyInfos);
        HomeNotifyAdapter homeNotifyAdapter = new HomeNotifyAdapter(mActivity, notifyInfos);
        HomePriceAdapter homePriceAdapter = new HomePriceAdapter(mActivity, priceGoods);
        HomeRecommendAdapter homeRecommendAdapter = new HomeRecommendAdapter(mActivity, recommendGoods);
        HomePopAdapter homePopAdapter = new HomePopAdapter(mActivity, popGoods);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 8);
        rv_home.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 8;
                if (position > 0 && position <= 4) return 2;
                if (position > 4 && position <= 8) return 8;
                return 8;
            }
        });
        homeAdapter = new HomeAdapter(homeBannerAdapter, homeClassifyAdapter, homeNotifyAdapter, homePriceAdapter
                , homeRecommendAdapter, homePopAdapter);
        rv_home.setAdapter(homeAdapter);
    }

    private void getData() {
        MyHttp.homeBanner(http, HomeAdapter.TYPE_BANNER, this);
        MyHttp.noticeList(http, HomeAdapter.TYPE_TABLE, this);
        MyHttp.homePriceGoods(http, HomeAdapter.TYPE_PRICE, "1", this);
        MyHttp.homePriceGoods(http, HomeAdapter.TYPE_RECOMMEND, "2", this);
        MyHttp.homePriceGoods(http, HomeAdapter.TYPE_POP, "3", this);
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        refresh_home.setUrlNum();
        if(code != 0){
//            showToast(msg);
            refresh_home.setLoadFailed();
            if(refresh_home.getUrlNum() == urlNum){
                refresh_home.setRefreshFailed();
            }
            return;
        }

        switch (which) {
            case HomeAdapter.TYPE_BANNER:
                bannerInfos.clear();
                bannerInfos.addAll((List<HomeBannerInfo>) bean);
                if (bannerInfos.size() == 0) {
                    return;
                }
                break;
            case HomeAdapter.TYPE_TABLE:
                notifyInfos.clear();
                notifyInfos.addAll((List<HomeNotifyInfo>) bean);
                if (notifyInfos.size() == 0) {
                    return;
                }
                break;
            case HomeAdapter.TYPE_PRICE:
                priceGoods.clear();
                priceGoods.addAll((List<GoodsInfo>) bean);
                if (priceGoods.size() == 0) {
                    return;
                }
                break;
            case HomeAdapter.TYPE_RECOMMEND:
                recommendGoods.clear();
                recommendGoods.addAll((List<GoodsInfo>) bean);
                if (recommendGoods.size() == 0) {
                    return;
                }
                break;
            case HomeAdapter.TYPE_POP:
                if (refresh_home.isLoading()&&((ArrayList<GoodsInfo>) bean).size()!=0){
                    refresh_home.setLoadSuccess();
                }
                if (refresh_home.isLoading()&&((ArrayList<GoodsInfo>) bean).size()==0){
                    refresh_home.setLoadNodata();
                }
                popGoods.clear();
                popGoods.addAll((List<GoodsInfo>) bean);
                if (popGoods.size() == 0) {
                    return;
                }
                break;
            default:
                break;
        }
        homeAdapter.notifyDataSetChanged();
        if(refresh_home.getUrlNum() == urlNum){
            refresh_home.setRefreshSuccess();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        et_search.clearFocus();
        return true;
    }


    @Override
    public void onShow() {
        super.onShow();
//        if(bannerInfos.size() == 0 || classifyInfos.size() == 0
//                || notifyInfos.size() == 0 || priceGoods.size() == 0
//                || recommendGoods.size() == 0 || popGoods.size() == 0){
//            getData();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
            case R.id.iv_search:
                startActivity(new Intent(mActivity, SearchActivity.class));
                break;
            case R.id.iv_location:
                startActivity(new Intent(mActivity, LocationActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void refresh() {
        getData();
    }

    @Override
    public void loadMore() {
        refresh_home.setLoadNodata();
    }
}
