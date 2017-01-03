package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.adapter.NormalBuyAdapter;
import com.cqfrozen.jsh.cart.CartActivity;
import com.cqfrozen.jsh.cart.CartManager;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.GoodsResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.CustomMiddleToast;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */
public class NormalBuyActivity extends MyActivity implements MyActivity.HttpFail, SupportLayout.RefreshListener, SupportLayout.LoadMoreListener, View.OnClickListener {

    public static final int FROM_NORMAL_LIST = 100;

    private RefreshLayout refresh_normalbuy;
    private RecyclerView rv_normalbuy;
    private List<GoodsInfo> goodsInfos = new ArrayList<>();
    private NormalBuyAdapter adapter;
    private int page = 1;
    private int is_page = 0;
    private TextView tv_total;
    private Button btn_buy_all;
    private CartManager cartManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normalbuy);
        cartManager = CartManager.getInstance(this);
        initView();
        initRV();
        getData();
    }

    private void initView() {
        setMyTitle("常用采购");
        refresh_normalbuy = (RefreshLayout) findViewById(R.id.refresh_normalbuy);
        rv_normalbuy = (RecyclerView) findViewById(R.id.rv_normalbuy);
        tv_total = (TextView) findViewById(R.id.tv_total);
        btn_buy_all = (Button) findViewById(R.id.btn_buy_all);
        refresh_normalbuy.setOnRefreshListener(this);
        refresh_normalbuy.setOnLoadMoreListener(this);
        btn_buy_all.setOnClickListener(this);
    }

    private void initRV() {
        rv_normalbuy.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_normalbuy.setLayoutManager(manager);
        adapter = new NormalBuyAdapter(this, goodsInfos);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_normalbuy.addItemDecoration(decoration);
        rv_normalbuy.setAdapter(adapter);

        adapter.setOnItemClickListener(new NormalBuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NormalBuyAdapter.MyViewHolder holder, int position) {
                Intent intent = new Intent(NormalBuyActivity.this, GoodsDetailActivity.class);
                intent.putExtra("g_id", goodsInfos.get(position).g_id);
                startActivityForResult(intent, FROM_NORMAL_LIST);
            }
        });
    }

    private void getData() {
        MyHttp.commonGoodsList(http, null, page, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {

                if(code == 404){
                    setHttpFail(NormalBuyActivity.this);
                    refresh_normalbuy.setRefreshFailed();
                    return;
                }

                if(code != 0){
//                    showToast(msg);
                    refresh_normalbuy.setRefreshFailed();
                    return;
                }
                refresh_normalbuy.setRefreshSuccess();
                GoodsResultInfo goodsResultInfo = (GoodsResultInfo)bean;
                is_page = goodsResultInfo.is_page;
                if(goodsResultInfo == null || goodsResultInfo.data1.size() == 0){
                    tv_total.setText("0件商品");
                    setHttpNotData(NormalBuyActivity.this);
                    return;
                }
                setHttpSuccess();
                goodsInfos.addAll(goodsResultInfo.data1);
                tv_total.setText(goodsInfos.size() + "件商品");
                adapter.notifyDataSetChanged();
                page++;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(refresh_normalbuy != null){
            if(refresh_normalbuy.isLoading()){
                refresh_normalbuy.setLoadClose();
            }
            if(refresh_normalbuy.isRefreshing()){
                refresh_normalbuy.setRefreshClose();
            }
        }
    }

    @Override
    public void toHttpAgain() {
        getData();
    }


    @Override
    public void refresh() {
        page = 1;
        is_page = 0;
        goodsInfos.clear();
        getData();
    }

    @Override
    public void loadMore() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
            refresh_normalbuy.setLoadNodata();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_buy_all:
                buyAll();
                break;
            default:
                break;
        }
    }

    private void buyAll() {
        MyHttp.addCartMore(http, null, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                int code = response.optInt("code");
                if(code != 0){
                    showToast(response.optString("msg"));
                    return;
                }
                cartManager.clear();
                CustomMiddleToast.getInstance(NormalBuyActivity.this).showToast("添加成功");
                for (GoodsInfo goodsInfo : goodsInfos){
                    cartManager.add(goodsInfo);
                }
//                HomeActivity.startActivity(NormalBuyActivity.this, 2);
                startActivity(new Intent(NormalBuyActivity.this, CartActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FROM_NORMAL_LIST && resultCode == RESULT_OK){
            boolean isCancelNormal = data.getBooleanExtra("isCancelNormal", false);
            if(isCancelNormal){
                page = 1;
                is_page = 0;
                goodsInfos.clear();
                getData();
            }
        }
    }
}
