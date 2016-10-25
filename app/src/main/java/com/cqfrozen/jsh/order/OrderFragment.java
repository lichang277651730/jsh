package com.cqfrozen.jsh.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.common.widget.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderResultInfo;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class OrderFragment extends MyFragment implements RefreshLayout.OnRefreshListener, MyFragment.HttpFail, RefreshLayout.TopOrBottom {

    private int tv = 1;//订单类型 1全部，2待付款，3待收货，4待评价
    private RefreshLayout refresh_order;
    private RecyclerView rv_order;
    private int page = 1;
    private int is_page = 1;

    private List<OrderResultInfo.OrderSearchInfo> orderSearchInfos = new ArrayList<>();
    private OrderListAdapter adapter;

    public static OrderFragment getInstance(int postion){
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", postion);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getBundleData(Bundle bundle) {
        int position = bundle.getInt("position", 1);
        switch (position) {
            case 0://代付款
                tv = 2;
                break;
            case 1://待收货
                tv = 3;
                break;
            case 2://待评价
                tv = 4;
                break;
            case 3://全部
                tv = 1;
                break;
            default:
                tv = 1;
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if(view == null){
            getBundleData(getArguments());
            view = inflater.inflate(R.layout.fragment_order, null);
            initView();
            initRV();
            getData();
        }
        return view;
    }

    private void initView() {
        refresh_order = (RefreshLayout) view.findViewById(R.id.refresh_order);
        rv_order = (RecyclerView) view.findViewById(R.id.rv_order);
        refresh_order.setOnRefreshListener(this);
        refresh_order.setRefreshble(true);
    }

    private void initRV() {
        rv_order.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new OrderListAdapter(mActivity, orderSearchInfos);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 1);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(4), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_order.addItemDecoration(decoration);
        rv_order.setLayoutManager(manager);
        rv_order.setAdapter(adapter);
        refresh_order.setRC(rv_order, this);
    }

    private void getData() {

        MyHttp.searchOrder(http, null, tv, page, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {

                if(code == 404){
                    setHttpFail(OrderFragment.this);
                    refresh_order.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }

                if(code != 0){
                    showToast(msg);
                    refresh_order.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                refresh_order.setResultState(RefreshLayout.ResultState.success);
                OrderResultInfo orderResultInfo = (OrderResultInfo) bean;
                orderSearchInfos.addAll(orderResultInfo.data1);
                if(orderSearchInfos.size() == 0){
                    setHttpNotData(OrderFragment.this);
                    return;
                }
                setHttpSuccess();
                adapter.notifyDataSetChanged();
                is_page = orderResultInfo.is_page;
                page++;
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        is_page = 1;
        orderSearchInfos.clear();
        getData();
    }

    @Override
    public void toHttpAgain() {
        getData();
    }

    @Override
    public void gotoTop() {

    }

    @Override
    public void gotoBottom() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
//            showToast("没有更多数据了!~");
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onStop() {
        super.onStop();
        if(refresh_order != null && refresh_order.isRefreshing){
            refresh_order.setResultState(RefreshLayout.ResultState.close);
        }
    }
}
