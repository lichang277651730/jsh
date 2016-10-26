package com.cqfrozen.jsh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.common.widget.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.CommentRVAdapter;
import com.cqfrozen.jsh.entity.CommentResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 *  intent2.putExtra("g_id", g_id);
 */
public class CommentListActivity extends MyActivity implements MyActivity.HttpFail, RefreshLayout.OnRefreshListener, RefreshLayout.TopOrBottom {

    private Long g_id;
    private List<CommentResultInfo.CommentInfo> commentInfos = new ArrayList<>();
    private RefreshLayout refresh_comment;
    private RecyclerView rv_comment;
    private int is_page = 1;
    private int page = 1;
    private CommentRVAdapter commentRVAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentlist);
        getIntentData();
        initView();
        initRV();
        getData();
    }

    private void getIntentData() {
        g_id = getIntent().getLongExtra("g_id", 0L);

    }

    private void initView() {
        setMyTitle("商品评论");
        refresh_comment = (RefreshLayout) findViewById(R.id.refresh_comment);
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
        refresh_comment.setRefreshble(true);
        refresh_comment.setOnRefreshListener(this);
    }

    private void initRV() {
        rv_comment.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.myline), false);
        rv_comment.addItemDecoration(decoration);
        rv_comment.setLayoutManager(manager);
        commentRVAdapter = new CommentRVAdapter(this, commentInfos);
        rv_comment.setAdapter(commentRVAdapter);
        refresh_comment.setRC(rv_comment, this);
    }

    private void getData() {

        MyHttp.pjList(http, null, page, g_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code == 404){
                    setHttpFail(CommentListActivity.this);
                    refresh_comment.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }

                if(code != 0){
                    showToast(msg);
                    refresh_comment.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                refresh_comment.setResultState(RefreshLayout.ResultState.success);
                CommentResultInfo commentResultInfo = (CommentResultInfo) bean;
                commentInfos.addAll(commentResultInfo.data1);
                is_page = commentResultInfo.is_page;
                if(commentInfos.size() == 0){
                    setHttpNotData(CommentListActivity.this);
                    return;
                }
                setHttpSuccess();
                commentRVAdapter.notifyDataSetChanged();
                page++;
            }
        });
    }


    @Override
    public void toHttpAgain() {
        getData();
    }

    @Override
    public void onRefresh() {
        is_page = 1;
        page = 1;
        commentInfos.clear();
        getData();
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
    protected void onStop() {
        super.onStop();
        if(refresh_comment != null && refresh_comment.isRefreshing){
            refresh_comment.setResultState(RefreshLayout.ResultState.close);
        }
    }
}