package com.cqfrozen.jsh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.base.BaseValue;
import com.common.refresh.SupportLayout;
import com.common.widget.MyGridDecoration;
import com.common.refresh.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.CommentRVAdapter;
import com.cqfrozen.jsh.entity.CommentResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 评论列表界面
 *  intent2.putExtra("g_id", g_id);
 */
public class CommentListActivity extends MyActivity implements MyActivity.HttpFail, SupportLayout.RefreshListener, SupportLayout.LoadMoreListener {

    private Long g_id;
    private List<CommentResultInfo.CommentInfo> commentInfos = new ArrayList<>();
    private RefreshLayout refresh_comment;
    private RecyclerView rv_comment;
    private int is_page = 0;//默认没有下一页
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
        refresh_comment.setOnRefreshListener(this);
        refresh_comment.setOnLoadMoreListener(this);
    }

    private void initRV() {
        rv_comment.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_comment.addItemDecoration(decoration);
        rv_comment.setLayoutManager(manager);
        commentRVAdapter = new CommentRVAdapter(this, commentInfos);
        rv_comment.setAdapter(commentRVAdapter);
    }

    private void getData() {

        MyHttp.pjList(http, null, page, g_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code == 404){
                    refresh_comment.setRefreshFailed();
                    refresh_comment.setLoadFailed();
                    setHttpFail(CommentListActivity.this);
                    return;
                }

                if(code != 0){
                    refresh_comment.setRefreshFailed();
                    refresh_comment.setLoadFailed();
                    setHttpFail(CommentListActivity.this);
                    return;
                }
                refresh_comment.setRefreshSuccess();
                refresh_comment.setLoadSuccess();
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
    public void refresh() {
        is_page = 0;
        page = 1;
        commentInfos.clear();
        getData();
    }

    @Override
    public void loadMore() {
        if(is_page == 1){
            getData();
        }else if(is_page == 0){
            refresh_comment.setLoadNodata();
        }
    }

}
