package com.cqfrozen.jsh.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.common.widget.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
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
    private int is_page = 0;

    private List<OrderResultInfo.OrderSearchInfo> orderSearchInfos = new ArrayList<>();
    private OrderListAdapter adapter;
    private LinearLayout include_ordernoorderlayout;
    private Button include_orderondata_btn;

    public static OrderFragment getInstance(int tv){
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tv", tv);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getBundleData(Bundle bundle) {
        this.tv = bundle.getInt("tv", 1);

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
        include_ordernoorderlayout = (LinearLayout) view.findViewById(R.id.include_ordernoorderlayout);
        include_orderondata_btn = (Button) view.findViewById(R.id.include_orderondata_btn);
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
        adapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, OrderResultInfo.OrderSearchInfo orderSearchInfo) {
                Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                intent.putExtra("o_id", orderSearchInfo.o_id);
                intent.putExtra("from", OrderDetailActivity.FROM.FROM_ORDER_LIST);
                startActivity(intent);
            }
        });
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
//                    showToast(msg);
                    setHttpFail(OrderFragment.this);
                    refresh_order.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                refresh_order.setResultState(RefreshLayout.ResultState.success);
                OrderResultInfo orderResultInfo = (OrderResultInfo) bean;
                orderSearchInfos.addAll(orderResultInfo.data1);
                is_page = orderResultInfo.is_page;
                if(orderSearchInfos.size() == 0){
//                    setHttpNotData(OrderFragment.this);
                    setOrderNotData();
                    return;
                }
                showDataView();
                setHttpSuccess();
                adapter.notifyDataSetChanged();
                page++;
            }
        });
    }

    private void setOrderNotData() {
        include_ordernoorderlayout.setVisibility(View.VISIBLE);
        rv_order.setVisibility(View.GONE);
        include_orderondata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.startActivity(getActivity(), HomeActivity.PAGE_CLASSIFY);
            }
        });
    }


    private void showDataView() {
        include_ordernoorderlayout.setVisibility(View.GONE);
        rv_order.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        page = 1;
        is_page = 0;
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

    @Override
    public void onShow() {
        super.onShow();
        page = 1;
        is_page = 0;
        orderSearchInfos.clear();
        getData();
    }
}
