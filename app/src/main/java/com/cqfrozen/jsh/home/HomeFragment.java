package com.cqfrozen.jsh.home;

import android.content.Context;
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

import com.common.base.BaseFragment;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HomeAdapter;
import com.cqfrozen.jsh.adapter.HomeBannerAdapter;
import com.cqfrozen.jsh.adapter.HomeClassifyAdapter;
import com.cqfrozen.jsh.adapter.HomeHotAdapter;
import com.cqfrozen.jsh.adapter.HomePriceAdapter;
import com.cqfrozen.jsh.adapter.HomeRecommendAdapter;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.entity.HomeClassifyInfo;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomeFragment extends BaseFragment implements MyHttp.MyHttpResult ,View.OnTouchListener {

    private static HomeFragment fragment;
    private List<HomeBannerInfo> bannerInfos = new ArrayList<>();
    private List<HomeClassifyInfo> classifyInfos = new ArrayList<>();
    private List<GoodsInfo> priceGoods = new ArrayList<>();
    private List<GoodsInfo> recommendGoods = new ArrayList<>();
    private RecyclerView rv_home;
    private HomeAdapter homeAdapter;
    private EditText et_search;

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
        if(view == null){
            view = inflater.inflate(R.layout.fragment_home, null);
            initView();
            initRV();
            getData();
        }
        return view;
    }

    private void initView() {
        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
    }


    private void initRV() {
        rv_home.setOverScrollMode(View.OVER_SCROLL_NEVER);
        HomeBannerAdapter homeBannerAdapter = new HomeBannerAdapter(mActivity, bannerInfos);
        HomeClassifyAdapter homeClassifyAdapter = new HomeClassifyAdapter(mActivity, classifyInfos);
        HomeHotAdapter homeHotAdapter = new HomeHotAdapter(mActivity);
        HomePriceAdapter homePriceAdapter = new HomePriceAdapter(mActivity, priceGoods);
        HomeRecommendAdapter homeRecommendAdapter = new HomeRecommendAdapter(mActivity, recommendGoods);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 8);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 8;
                if (position > 0 && position <= 4) return 2;
                if (position == 5) return 8;
                if (position == 6) return 8;
                if (position == 7) return 8;
                return 8;
            }
        });
        homeAdapter = new HomeAdapter(homeBannerAdapter, homeClassifyAdapter, homeHotAdapter, homePriceAdapter
                , homeRecommendAdapter);
        rv_home.setAdapter(homeAdapter);
        rv_home.setLayoutManager(manager);
    }

    private void getData() {
        MyHttp.homeBanner(http, HomeAdapter.TYPE_BANNER, this);
        MyHttp.homePriceGoods(http, HomeAdapter.TYPE_PRICE, "1", this);
        MyHttp.homePriceGoods(http, HomeAdapter.TYPE_RECOMMEND, "2", this);
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if(code != 0){
            showToast(msg);
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
            default:
                break;
        }
        homeAdapter.notifyDataSetChanged();
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
//        Log.d("HomeFragment", "FragmentOnShow");
//        if(bannerInfos == null || bannerInfos.size() == 0){
//            Log.d("HomeFragment", "FragmentOnShowgetData");
//            getData();
//        }
    }

}
