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
import com.common.widget.GridDecoration;
import com.common.widget.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.GoodsAdapter;
import com.cqfrozen.jsh.constants.Where;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.GoodsResultInfo;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class GoodsFragment extends MyFragment implements MyHttp.MyHttpResult, MyFragment.HttpFail, RefreshLayout.OnRefreshListener, RefreshLayout.TopOrBottom {

    private TextView tv_name;
    private String title;
    private String area_id = "5";
    private int page = 1;
    private String g_type_id;
    private int where;
    private RecyclerView rv_goods;
    private List<GoodsInfo> goodsInfos = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    private int is_page = 1;
    private RefreshLayout refresh_goods;

    public static GoodsFragment getInstance(String title){
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static GoodsFragment getInstanceForClassify(String area_id, String g_type_id){
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("area_id", area_id);
        bundle.putInt("where", Where.CLASSIFY);
        bundle.putString("g_type_id", g_type_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            getBundleData(getArguments());
            view = inflater.inflate(R.layout.fragment_goods, null);
            initView();
            initRV();
            getData();
        }
        return view;
    }

    private void initView() {
        refresh_goods = (RefreshLayout) view.findViewById(R.id.refresh_goods);
        rv_goods = (RecyclerView) view.findViewById(R.id.rv_goods);
        refresh_goods.setOnRefreshListener(this);
    }

    private void initRV() {
        rv_goods.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 2);
        GridDecoration newGridDecoration = new GridDecoration(0, BaseValue.dp2px(1),
                getResources().getColor(R.color.myline), true);
        rv_goods.addItemDecoration(newGridDecoration);
        goodsAdapter = new GoodsAdapter(mActivity, goodsInfos);
        rv_goods.setLayoutManager(manager);
        rv_goods.setAdapter(goodsAdapter);
        refresh_goods.setRC(rv_goods, this);
    }

    private void getData() {
        switch (this.where) {
            case Where.CLASSIFY:
                MyHttp.goodstypeList(http, null, area_id, page, g_type_id, this);
                break;
            default:
                break;
        }
    }


    private void getBundleData(Bundle bundle) {
        this.where = bundle.getInt("where");
        switch (this.where) {
            case Where.CLASSIFY:
                this.area_id = bundle.getString("area_id");
                this.g_type_id = bundle.getString("g_type_id");
                break;
            default:
                break;
        }
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {

        if(code == 404){
            setHttpFail(this);
            refresh_goods.setResultState(RefreshLayout.ResultState.failed);
            return;
        }

        if(code != 0){
            showToast(msg);
            refresh_goods.setResultState(RefreshLayout.ResultState.failed);
            return;
        }
        refresh_goods.setResultState(RefreshLayout.ResultState.success);
        GoodsResultInfo goodsResultInfo = (GoodsResultInfo) bean;
        if(goodsResultInfo == null || goodsResultInfo.data1.size() == 0){
            setHttpNotData(this);
            return;
        }
        goodsInfos.addAll(goodsResultInfo.data1);
        setHttpSuccess();
        is_page = goodsResultInfo.is_page;
        goodsAdapter.notifyDataSetChanged();
        page++;
    }

    //没有数据，请求失败 点击重试
    @Override
    public void toHttpAgain() {
        getData();
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        page = 1;
        goodsInfos.clear();
        getData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(refresh_goods != null && refresh_goods.isRefreshing){
            refresh_goods.setResultState(RefreshLayout.ResultState.close);
        }
    }

    @Override
    public void gotoTop() {

    }

    @Override
    public void gotoBottom() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
            showToast("没有更多数据了!~");
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void stop() {

    }
}
