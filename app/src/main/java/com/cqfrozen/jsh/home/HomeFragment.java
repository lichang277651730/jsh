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

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HomeAdapter;
import com.cqfrozen.jsh.adapter.HomeBannerAdapter;
import com.cqfrozen.jsh.adapter.HomeClassifyAdapter;
import com.cqfrozen.jsh.base.BaseFragment;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.entity.HomeClassifyInfo;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.cqfrozen.jsh.widget.MyToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomeFragment extends BaseFragment implements MyHttp.MyHttpResult ,View.OnTouchListener {

    private static HomeFragment fragment;
    private List<HomeBannerInfo> bannerInfos = new ArrayList<>();
    private List<HomeClassifyInfo> classifyInfos = new ArrayList<>();
    private MyToolbar toolbar;
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
            initToolbar();
            initRV();
            getData();
        }
        return view;
    }

    private void initView() {
        toolbar = (MyToolbar) view.findViewById(R.id.toolbar);
        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
    }

    private void initToolbar() {
        et_search = toolbar.getEditText();
    }

    private void initRV() {
        rv_home.setOverScrollMode(View.OVER_SCROLL_NEVER);
        HomeBannerAdapter homeBannerAdapter = new HomeBannerAdapter(mActivity, bannerInfos);
        HomeClassifyAdapter homeClassifyAdapter = new HomeClassifyAdapter(mActivity, classifyInfos);
//        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 8);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 8;
                if (position > 0 && position <= 4) return 2;
//                if (position == 11) return 10;
                return 8;
            }
        });
        homeAdapter = new HomeAdapter(homeBannerAdapter, homeClassifyAdapter);
        rv_home.setAdapter(homeAdapter);
        rv_home.setLayoutManager(manager);
    }

    private void getData() {
        MyHttp.homeBanner(http, HomeAdapter.TYPE_BANNER, this);
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
}
