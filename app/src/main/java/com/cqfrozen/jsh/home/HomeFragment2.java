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
import android.widget.TextView;

import com.common.base.BaseFragment;
import com.common.base.BaseValue;
import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.common.widget.GridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HomeAdapter2;
import com.cqfrozen.jsh.adapter.HomeBannerAdapter;
import com.cqfrozen.jsh.adapter.HomeClassifyAdapter;
import com.cqfrozen.jsh.adapter.HomeNotifyAdapter;
import com.cqfrozen.jsh.adapter.HomeRecommendAdapter;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.entity.HomeClassifyInfo;
import com.cqfrozen.jsh.entity.HomeNotifyInfo;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */
public class HomeFragment2 extends BaseFragment  implements MyHttp.MyHttpResult ,View.OnTouchListener, View.OnClickListener, SupportLayout.RefreshListener, SupportLayout.LoadMoreListener {

    private static HomeFragment2 fragment;
    private List<HomeBannerInfo> bannerInfos = new ArrayList<>();
    private List<HomeClassifyInfo> classifyInfos = new ArrayList<>();
    private List<HomeNotifyInfo> notifyInfos = new ArrayList<>();
    private List<GoodsInfo> recommendGoods = new ArrayList<>();
    private RecyclerView rv_home;
    private HomeAdapter2 homeAdapter2;
    private EditText et_search;
    private LinearLayout ll_search;
    private RefreshLayout refresh_home;

    private static final int urlNum = 3; //当前页面是刷新的url数量
    private ImageView iv_search;
    private TextView tv_location;

    public static HomeFragment2 getInstance(){
        if(fragment == null){
            fragment = new HomeFragment2();
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
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
        et_search.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        refresh_home.setOnLoadMoreListener(this);
        refresh_home.setOnRefreshListener(this);
        tv_location.setText(MyApplication.userInfo.area_name);
    }


    private void initRV() {
        rv_home.setOverScrollMode(View.OVER_SCROLL_NEVER);
        HomeBannerAdapter homeBannerAdapter = new HomeBannerAdapter(mActivity, bannerInfos);
        HomeClassifyAdapter homeClassifyAdapter = new HomeClassifyAdapter(mActivity, classifyInfos);
        HomeNotifyAdapter homeNotifyAdapter = new HomeNotifyAdapter(mActivity, notifyInfos);
        HomeRecommendAdapter homeRecommendAdapter = new HomeRecommendAdapter(mActivity, recommendGoods);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 8);
        rv_home.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 8;
                if (position > 0 && position <= 4) return 2;
                if (position == 5) return 8;
                return 8;
            }
        });
        GridDecoration newGridDecoration = new GridDecoration(6, BaseValue.dp2px(1),
                getResources().getColor(R.color.mybg), true);
        rv_home.addItemDecoration(newGridDecoration);
        homeAdapter2 = new HomeAdapter2(homeBannerAdapter, homeClassifyAdapter, homeNotifyAdapter, homeRecommendAdapter);
        rv_home.setAdapter(homeAdapter2);
    }

    private void getData() {
        MyHttp.homeBanner(http, HomeAdapter2.TYPE_BANNER, this);
        MyHttp.noticeList(http, HomeAdapter2.TYPE_TABLE, this);
        MyHttp.homePriceGoods(http, HomeAdapter2.TYPE_LIST, "1", this);
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
            case HomeAdapter2.TYPE_BANNER:
                bannerInfos.clear();
                bannerInfos.addAll((List<HomeBannerInfo>) bean);
                if (bannerInfos.size() == 0) {
                    return;
                }
                break;
            case HomeAdapter2.TYPE_TABLE:
                notifyInfos.clear();
                notifyInfos.addAll((List<HomeNotifyInfo>) bean);
                if (notifyInfos.size() == 0) {
                    return;
                }
                break;
            case HomeAdapter2.TYPE_LIST:
                recommendGoods.clear();
                recommendGoods.addAll((List<GoodsInfo>) bean);
                if (recommendGoods.size() == 0) {
                    return;
                }
                break;
            default:
                break;
        }
        homeAdapter2.notifyDataSetChanged();
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
