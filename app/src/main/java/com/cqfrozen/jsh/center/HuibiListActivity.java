package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.common.widget.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HuibiRVAdapter;
import com.cqfrozen.jsh.entity.HuibiInfo;
import com.cqfrozen.jsh.entity.HuibiResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 * intent.putExtra("hb_count", getUserInfo().hb_count);
 */
public class HuibiListActivity extends MyActivity implements RefreshLayout.TopOrBottom, RefreshLayout.OnRefreshListener, View.OnClickListener {

    private float hb_count;
    private TextView tv_huibi;
    private TextView tv_all;
    private TextView tv_right;
    private TextView tv_use;
//    private ListView lv_huibi;
    private List<HuibiInfo> huibiInfos = new ArrayList<>();
//    private HuibiAdapter adapter;
    private int type = 1;//1收入  2支出
    private int page = 1;
    private int is_page = 1;
    private RefreshLayout refresh_huibi;
    private RecyclerView rv_huibi;
    private HuibiRVAdapter rvAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huibilist);
        getIntentData();
        initView();
//        initLV();
        initRC();
        getData();
    }

    private void getIntentData() {
        hb_count = getIntent().getFloatExtra("hb_count", 0f);
    }

    private void initView() {
        setMyTitle("我的汇币", "汇币规则");
        tv_huibi = (TextView) findViewById(R.id.tv_huibi);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_use = (TextView) findViewById(R.id.tv_use);
        refresh_huibi = (RefreshLayout) findViewById(R.id.refresh_huibi);
        rv_huibi = (RecyclerView) findViewById(R.id.rv_huibi);
//        lv_huibi = (ListView) findViewById(R.id.lv_huibi);
        tv_huibi.setText(hb_count + "");
        tv_all.setOnClickListener(this);
        tv_use.setOnClickListener(this);
        tv_all.setTextColor(getResources().getColor(R.color.main));
    }

    @Override
    public void onClick(View v) {
        tv_all.setTextColor(getResources().getColor(R.color.myblack));
        tv_use.setTextColor(getResources().getColor(R.color.myblack));
        switch (v.getId()) {
            case R.id.tv_all:
                type = 1;
                tv_all.setTextColor(getResources().getColor(R.color.main));
                break;
            case R.id.tv_use:
                type = 2;
                tv_use.setTextColor(getResources().getColor(R.color.main));
                break;
            default:
                break;
        }
        is_page = 1;
        page = 1;
        huibiInfos.clear();
        getData();
    }

    private void initRC() {
        refresh_huibi.setRefreshble(true);
        refresh_huibi.setOnRefreshListener(this);
        rv_huibi.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_huibi.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_huibi.addItemDecoration(decoration);
        rvAdapter = new HuibiRVAdapter(this, huibiInfos);
        rv_huibi.setAdapter(rvAdapter);
        refresh_huibi.setRC(rv_huibi, this);
    }


//    private void initLV() {
//        lv_huibi.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        adapter = new HuibiAdapter(this, huibiInfos);
//        lv_huibi.setAdapter(adapter);
//    }



    private void getData() {
        MyHttp.searchHBinfo(http, null, type, page, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code == 404){
                    showToast(msg);
                    refresh_huibi.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                if(code != 0){
                    showToast(msg);
                    refresh_huibi.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                refresh_huibi.setResultState(RefreshLayout.ResultState.success);
                HuibiResultInfo huibiResultInfo = (HuibiResultInfo) bean;
                huibiInfos.addAll(huibiResultInfo.data1);
//                adapter.notifyDataSetChanged();
                is_page = huibiResultInfo.is_page;
                if(huibiInfos.size() == 0){
                    return;
                }
                rvAdapter.notifyDataSetChanged();
                page++;

            }
        });
    }

    @Override
    public void gotoTop() {

    }

    @Override
    public void gotoBottom() {
        if(is_page == 1){
            getData();
        }else {

        }
    }

    @Override
    public void move() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onRefresh() {
        page = 1;
        is_page = 1;
        huibiInfos.clear();
        getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(refresh_huibi != null && refresh_huibi.isRefreshing){
            refresh_huibi.setResultState(RefreshLayout.ResultState.close);
        }
    }

}
