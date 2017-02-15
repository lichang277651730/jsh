package com.cqfrozen.jsh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HuibiRVAdapter;
import com.cqfrozen.jsh.entity.HuibiInfo;
import com.cqfrozen.jsh.entity.HuibiResultInfo;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */
public class HuibiFragment extends MyFragment implements SupportLayout.LoadMoreListener, SupportLayout.RefreshListener, MyFragment.HttpFail {

    private int type = 1;//1收入  2支出
    private int page = 1;
    private int is_page = 0;//0没有下一页 1有下一页

    private RefreshLayout refresh_huibi;
    private RecyclerView rv_huibi;

    private HuibiRVAdapter rvAdapter;
    private List<HuibiInfo> huibiInfos = new ArrayList<>();
    private LinearLayout include_huibinodatalayout;

    public static HuibiFragment getInstance(int type){
        HuibiFragment fragment = new HuibiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if(view == null){
            getBundleData(getArguments());
            view = inflater.inflate(R.layout.fragment_huibi, container, false);
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

    private void getBundleData(Bundle bundle) {
        this.type = bundle.getInt("type", 1);
    }

    private void initView(View view) {
        refresh_huibi = (RefreshLayout) view.findViewById(R.id.refresh_huibi);
        rv_huibi = (RecyclerView) view.findViewById(R.id.rv_huibi);
        include_huibinodatalayout = (LinearLayout) view.findViewById(R.id.include_huibinodatalayout);
    }

    private void initRV() {
        refresh_huibi.setOnLoadMoreListener(this);
        refresh_huibi.setOnRefreshListener(this);
        rv_huibi.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        rv_huibi.setLayoutManager(manager);
//        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
//                .dp2px(0), getResources().getColor(R.color.mybg), false);
//        rv_huibi.addItemDecoration(decoration);
        rvAdapter = new HuibiRVAdapter(getActivity(), huibiInfos);
        rv_huibi.setAdapter(rvAdapter);
    }

    private void getData() {
        MyHttp.searchHBinfo(http, null, type, page, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code == 404){
                    refresh_huibi.setRefreshFailed();
                    refresh_huibi.setLoadFailed();
                    setHttpFail(HuibiFragment.this);
                    return;
                }
                if(code != 0){
                    refresh_huibi.setRefreshFailed();
                    refresh_huibi.setLoadFailed();
                    setHttpFail(HuibiFragment.this);
                    return;
                }
                refresh_huibi.setLoadSuccess();
                refresh_huibi.setRefreshSuccess();
                HuibiResultInfo huibiResultInfo = (HuibiResultInfo) bean;
                huibiInfos.addAll(huibiResultInfo.data1);
                is_page = huibiResultInfo.is_page;
                if(huibiInfos.size() == 0){
                    setHuibiNotData();
                    return;
                }
                showDataView();
                setHttpSuccess();
                rvAdapter.notifyDataSetChanged();
                page++;

            }
        });
    }

    private void showDataView() {
        include_huibinodatalayout.setVisibility(View.GONE);
        rv_huibi.setVisibility(View.VISIBLE);
    }

    private void setHuibiNotData() {
        include_huibinodatalayout.setVisibility(View.VISIBLE);
        rv_huibi.setVisibility(View.GONE);
    }

    @Override
    public void loadMore() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
            refresh_huibi.setLoadNodata();
        }
    }

    @Override
    public void refresh() {
        page = 1;
        is_page = 0;
        huibiInfos.clear();
        getData();
    }

    public void setResultData(int type) {
        this.type =  type;
        page = 1;
        is_page = 0;
        huibiInfos.clear();
        getData();
    }

    public void setSelection(int location) {
        if(rv_huibi != null && rv_huibi.getChildCount() != 0){
            rv_huibi.scrollToPosition(location);
        }
    }

    @Override
    public void toHttpAgain() {
        page = 1;
        is_page = 0;
        huibiInfos.clear();
        getData();
    }
}
