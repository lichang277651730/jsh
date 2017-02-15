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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.base.BaseFragment;
import com.common.base.BaseValue;
import com.common.widget.GridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HomeAdapter2;
import com.cqfrozen.jsh.adapter.HomeBannerAdapter;
import com.cqfrozen.jsh.adapter.HomeClassifyAdapter;
import com.cqfrozen.jsh.adapter.HomeNotifyAdapter;
import com.cqfrozen.jsh.adapter.HomeRecommendAdapter;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.cqfrozen.jsh.entity.HomeClassifyInfo;
import com.cqfrozen.jsh.entity.HomeNotifyInfo;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */
public class HomeFragment3 extends BaseFragment  implements MyHttp.MyHttpResult ,View.OnTouchListener, View.OnClickListener{

    private static HomeFragment3 fragment;
//    private List<HomeBannerInfo> bannerInfos = new ArrayList<>();
    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos = new ArrayList<>();
    private List<HomeClassifyInfo> classifyInfos = new ArrayList<>();
    private List<HomeNotifyInfo> notifyInfos = new ArrayList<>();
    private List<GoodsInfo> recommendGoods = new ArrayList<>();
    private RecyclerView rv_home;
    private HomeAdapter2 homeAdapter2;
    private EditText et_search;
    private LinearLayout ll_search;
    private SuperSwipeRefreshLayout refresh_home;

    private static int urlNum = 3; //当前页面是刷新的url数量
    private ImageView iv_search;
    private TextView tv_location;
    private Pull mPull = new Pull();
    private Push mPush = new Push();

    public static HomeFragment3 getInstance(){
        if(fragment == null){
            fragment = new HomeFragment3();
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
            view = inflater.inflate(R.layout.fragment_home3, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initRefresh();
        initRV();
        getData();
    }

    private void initView(View view) {
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        refresh_home = (SuperSwipeRefreshLayout) view.findViewById(R.id.refresh_home);
        et_search = (EditText) view.findViewById(R.id.et_search);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
        et_search.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        tv_location.setText(MyApplication.userInfo == null ? "" : MyApplication.userInfo.area_name);
    }

    private void initRefresh() {
        refresh_home.setHeaderView(mPull.createView());
        refresh_home.setFooterView(mPush.createView());
        refresh_home.setOnPullRefreshListener(mPull);
        refresh_home.setOnPushLoadMoreListener(mPush);
        refresh_home.setTargetScrollWithLayout(true);
    }

    private void initRV() {
        rv_home.setOverScrollMode(View.OVER_SCROLL_NEVER);
        HomeBannerAdapter homeBannerAdapter =  new HomeBannerAdapter(mActivity, bannerAdInfos);
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
                getResources().getColor(R.color.dividerLine), true);
        rv_home.addItemDecoration(newGridDecoration);
        homeAdapter2 = new HomeAdapter2(homeBannerAdapter, homeClassifyAdapter, homeNotifyAdapter, homeRecommendAdapter);
        rv_home.setAdapter(homeAdapter2);
    }

    private void getData() {
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_home.setRefreshing(true);
                mPull.onRefresh();
            }
        }, 200);
//        MyHttp.homeBanner(http, HomeAdapter2.TYPE_BANNER, this);
//        MyHttp.adBannerList(http, HomeAdapter2.TYPE_BANNER, 1, this);
//        MyHttp.noticeList(http, HomeAdapter2.TYPE_TABLE, this);
//        MyHttp.homePriceGoods(http, HomeAdapter2.TYPE_LIST, "1", this);//1推荐，2特价，3热门
    }

    private void loadData(){
        MyHttp.adBannerList(http, HomeAdapter2.TYPE_BANNER, 1, this);
        MyHttp.noticeList(http, HomeAdapter2.TYPE_TABLE, this);
        MyHttp.homePriceGoods(http, HomeAdapter2.TYPE_LIST, "1", this);//1推荐，2特价，3热门
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if(code != 0){
            return;
        }

        switch (which) {
            case HomeAdapter2.TYPE_BANNER:
                bannerAdInfos.clear();
                bannerAdInfos.addAll(((HomeBannerAdResultInfo)bean).data1);
                if(bannerAdInfos.size() == 0){
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
        if(bannerAdInfos.size() != 0 && notifyInfos.size() != 0 && recommendGoods.size() != 0){
            mPull.done();
        }
        homeAdapter2.notifyDataSetChanged();
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

    private class Pull implements SuperSwipeRefreshLayout.OnPullRefreshListener{

        private TextView tv_header;
        private ProgressBar pb_header;
        private ImageView iv_header;

        public View createView(){
            View headerView = LayoutInflater.from(refresh_home.getContext()).inflate(R.layout.layout_head, null);
            tv_header = (TextView) headerView.findViewById(R.id.tv_header);
            pb_header = (ProgressBar) headerView.findViewById(R.id.pb_header);
            iv_header = (ImageView) headerView.findViewById(R.id.iv_header);

            tv_header.setText(R.string.ptr_pull_refresh);
            iv_header.setVisibility(View.VISIBLE);
            iv_header.setImageResource(R.mipmap.pull_icon_big);
            pb_header.setVisibility(View.GONE);
            return headerView;
        }

        @Override
        public void onRefresh() {
            tv_header.setText(R.string.ptr_pull_refreshing);
            iv_header.setVisibility(View.GONE);
            pb_header.setVisibility(View.VISIBLE);
            loadData();
        }

        @Override
        public void onPullDistance(int distance) {

        }

        public void done(){
            refresh_home.setRefreshing(false);
            pb_header.setVisibility(View.GONE);
        }

        @Override
        public void onPullEnable(boolean enable) {
            tv_header.setText(enable ? R.string.ptr_pull_enable : R.string.ptr_pull_disable);
            iv_header.setVisibility(View.VISIBLE);
            iv_header.setRotation(enable ? 180 : 0);
        }
    }

    private class Push implements SuperSwipeRefreshLayout.OnPushLoadMoreListener{

        private TextView tv_footer;
        private ProgressBar pb_footer;
        private ImageView iv_footer;

        public View createView(){
            View footerView = LayoutInflater.from(refresh_home.getContext()).inflate(R.layout.layout_footer, null);

            tv_footer = (TextView) footerView.findViewById(R.id.tv_footer);
            pb_footer = (ProgressBar) footerView.findViewById(R.id.pb_footer);
            iv_footer = (ImageView) footerView.findViewById(R.id.iv_footer);

            tv_footer.setText(R.string.ptr_push_refresh);
            pb_footer.setVisibility(View.GONE);
            iv_footer.setVisibility(View.VISIBLE);
            iv_footer.setImageResource(R.mipmap.pull_icon_big);

            return footerView;
        }

        @Override
        public void onLoadMore() {
            tv_footer.setText(R.string.ptr_push_refreshing);
            iv_footer.setVisibility(View.GONE);
            pb_footer.setVisibility(View.VISIBLE);
            pushLoad();
        }

        public void done(){
            iv_footer.setVisibility(View.VISIBLE);
            pb_footer.setVisibility(View.GONE);
            refresh_home.setLoadMore(false);
        }

        @Override
        public void onPushDistance(int distance) {

        }

        @Override
        public void onPushEnable(boolean enable) {
            tv_footer.setText(enable ? R.string.ptr_push_enable : R.string.ptr_pull_disable);
            iv_footer.setVisibility(View.VISIBLE);
            iv_footer.setRotation(enable ? 0 : 180);
        }
    }

    private void pushLoad() {
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPush.done();
            }
        }, 200);
    }

}
