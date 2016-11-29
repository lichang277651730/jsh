package com.cqfrozen.jsh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.base.BaseValue;
import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.FansRVAdapter;
import com.cqfrozen.jsh.entity.FansResultInfo;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */
public class FansFragment extends MyFragment implements SupportLayout.LoadMoreListener, SupportLayout.RefreshListener, MyFragment.HttpFail {

    private int level = 1;//1一级兄弟伙  2兄弟伙的兄弟伙
    private int is_page = 0;//1有下一页 0没有下一页
    private int page = 1;
    private RefreshLayout refresh_fans;
    private RecyclerView rv_fans;
    private List<FansResultInfo.FansInfo> fansInfos = new ArrayList<>();
    private FansRVAdapter fansRVAdapter;

    public static FansFragment getInstance(int level){
        FansFragment fragment = new FansFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if(view == null){
            getBundleData(getArguments());
            view = inflater.inflate(R.layout.fragment_fans, null);
            initView();
            initRV();
            getData();
        }
        return view;
    }

    private void initView() {
        refresh_fans = (RefreshLayout) view.findViewById(R.id.refresh_fans);
        rv_fans = (RecyclerView) view.findViewById(R.id.rv_fans);
    }

    private void initRV() {
        refresh_fans.setOnLoadMoreListener(this);
        refresh_fans.setOnRefreshListener(this);
        rv_fans.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        rv_fans.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.dividerLine), false);
        rv_fans.addItemDecoration(decoration);
        fansRVAdapter = new FansRVAdapter(getActivity(), fansInfos);
        rv_fans.setAdapter(fansRVAdapter);
    }

    private void getBundleData(Bundle bundle) {
        this.level = bundle.getInt("level", 1);
    }

    private void getData() {
        MyHttp.searchFans(http, null, page, level, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code == 404){
                    refresh_fans.setRefreshFailed();
                    refresh_fans.setLoadFailed();
                    setHttpFail(FansFragment.this);
                    return;
                }
                if(code != 0){
                    refresh_fans.setRefreshFailed();
                    refresh_fans.setLoadFailed();
                    setHttpFail(FansFragment.this);
                    return;
                }
                refresh_fans.setRefreshSuccess();
                refresh_fans.setLoadSuccess();
                FansResultInfo fansResultInfo = (FansResultInfo) bean;
                is_page = fansResultInfo.is_page;
                fansInfos.addAll(fansResultInfo.data1);
                if(fansInfos.size() == 0){
//                    setHttpNotData(FansFragment.this);
                    return;
                }
                setHttpSuccess();
                fansRVAdapter.notifyDataSetChanged();
                page++;
            }
        });
    }

    @Override
    public void loadMore() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
            refresh_fans.setLoadNodata();
        }
    }

    @Override
    public void refresh() {
        page = 1;
        is_page = 0;
        fansInfos.clear();
        getData();
    }

    @Override
    public void toHttpAgain() {
        getData();
    }

    public void setSelection(int location) {
        if(rv_fans != null && rv_fans.getChildCount() != 0){
            rv_fans.scrollToPosition(location);
        }
    }

    public void setResultData(int level) {
        this.level =  level;
        page = 1;
        is_page = 0;
        fansInfos.clear();
        getData();
    }
}
