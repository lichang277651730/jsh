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
import com.cqfrozen.jsh.adapter.FansRVAdapter;
import com.cqfrozen.jsh.entity.FansResultInfo;
import com.cqfrozen.jsh.entity.MyFansPageInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class FansListActity extends MyActivity implements View.OnClickListener, RefreshLayout.OnRefreshListener, RefreshLayout.TopOrBottom {

    private TextView tv_desc;
    private TextView tv_huibi_total;
    private TextView tv_fans_count;
    private TextView tv_send_invite;
    private TextView tv_face_to_face;
    private TextView tv_invite_code;
    private TextView tv_one_fans;
    private TextView tv_two_fans;

    private int level = 1;//1一级兄弟伙  2兄弟伙的兄弟伙
    private int is_page = 1;//1有下一页 0没有下一页
    private int page = 1;
    private RefreshLayout refresh_fans;
    private RecyclerView rv_fans;
    private List<FansResultInfo.FansInfo> fansInfos = new ArrayList<>();
    private FansRVAdapter fansRVAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanslist);
        initView();
        getViewData();
        initRV();
        getRVData();
    }

    private void getViewData() {
        MyHttp.myFans(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                MyFansPageInfo myFansPageInfo = (MyFansPageInfo) bean;
                initViewData(myFansPageInfo);
            }
        });
    }

    private void initViewData(MyFansPageInfo myFansPageInfo) {
        tv_desc.setText(myFansPageInfo.content);
        tv_huibi_total.setText("￥" + myFansPageInfo.hb_count);
        tv_fans_count.setText("邀请好友总计：" + myFansPageInfo.intotal_fans_count +"人");
        tv_invite_code.setText("我的邀请码:" + myFansPageInfo.code);
        tv_one_fans.setText("我的兄弟伙(" + myFansPageInfo.one_fans_count + ")");
        tv_two_fans.setText("兄弟伙的兄弟伙(" + myFansPageInfo.two_fans_count + ")");
        tv_one_fans.setTextColor(getResources().getColor(R.color.main));
    }

    private void initView() {
        setMyTitle("我的兄弟伙");
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_huibi_total = (TextView) findViewById(R.id.tv_huibi_total);
        tv_fans_count = (TextView) findViewById(R.id.tv_fans_count);
        tv_send_invite = (TextView) findViewById(R.id.tv_send_invite);
        tv_face_to_face = (TextView) findViewById(R.id.tv_face_to_face);
        tv_invite_code = (TextView) findViewById(R.id.tv_invite_code);
        tv_one_fans = (TextView) findViewById(R.id.tv_one_fans);
        tv_two_fans = (TextView) findViewById(R.id.tv_two_fans);
        refresh_fans = (RefreshLayout) findViewById(R.id.refresh_fans);
        rv_fans = (RecyclerView) findViewById(R.id.rv_fans);
        tv_one_fans.setOnClickListener(this);
        tv_two_fans.setOnClickListener(this);
    }

    private void initRV() {
        refresh_fans.setRefreshble(true);
        refresh_fans.setOnRefreshListener(this);
        rv_fans.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_fans.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_fans.addItemDecoration(decoration);
        fansRVAdapter = new FansRVAdapter(this, fansInfos);
        rv_fans.setAdapter(fansRVAdapter);
        refresh_fans.setRC(rv_fans, this);
    }

    private void getRVData() {
        MyHttp.searchFans(http, null, page, level, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code == 404){
                    showToast(msg);
                    refresh_fans.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                if(code != 0){
                    showToast(msg);
                    refresh_fans.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                refresh_fans.setResultState(RefreshLayout.ResultState.success);
                FansResultInfo fansResultInfo = (FansResultInfo) bean;
                is_page = fansResultInfo.is_page;
                fansInfos.addAll(fansResultInfo.data1);
                if(fansInfos.size() == 0){
                    return;
                }
                fansRVAdapter.notifyDataSetChanged();
                page++;
            }
        });
    }

    @Override
    public void onClick(View v) {
        tv_one_fans.setTextColor(getResources().getColor(R.color.myblack));
        tv_two_fans.setTextColor(getResources().getColor(R.color.myblack));
        switch (v.getId()) {
            case R.id.tv_one_fans:
                tv_one_fans.setTextColor(getResources().getColor(R.color.main));
                level = 1;
                break;
            case R.id.tv_two_fans:
                tv_two_fans.setTextColor(getResources().getColor(R.color.main));
                level = 2;
                break;
            default:
                break;
        }
        page = 1;
        is_page = 1;
        fansInfos.clear();
        getRVData();

    }

    @Override
    public void onRefresh() {
        page = 1;
        is_page = 1;
        fansInfos.clear();
        getRVData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(refresh_fans != null && refresh_fans.isRefreshing){
            refresh_fans.setResultState(RefreshLayout.ResultState.close);
        }
    }

    @Override
    public void gotoTop() {

    }

    @Override
    public void gotoBottom() {
        if(is_page == 1){
            getRVData();
        }else if(is_page == 0){

        }
    }

    @Override
    public void move() {

    }

    @Override
    public void stop() {

    }
}
