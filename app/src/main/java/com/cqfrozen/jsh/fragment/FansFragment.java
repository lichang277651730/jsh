package com.cqfrozen.jsh.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.common.refresh.RefreshLayout;
import com.common.refresh.SupportLayout;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.FansRVAdapter;
import com.cqfrozen.jsh.entity.FansResultInfo;
import com.cqfrozen.jsh.main.MyFragment;
import com.cqfrozen.jsh.share.SharePop;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */
public class FansFragment extends MyFragment implements SupportLayout.LoadMoreListener, SupportLayout.RefreshListener, MyFragment.HttpFail, View.OnClickListener {

    private int level = 1;//1一级兄弟伙  2兄弟伙的兄弟伙
    private int is_page = 0;//1有下一页 0没有下一页
    private int page = 1;
    private RefreshLayout refresh_fans;
    private RecyclerView rv_fans;
    private List<FansResultInfo.FansInfo> fansInfos = new ArrayList<>();
    private FansRVAdapter fansRVAdapter;
    private Button include_fansnodata_btn;
    private LinearLayout include_fansnodatalayout;

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
        include_fansnodatalayout = (LinearLayout) view.findViewById(R.id.include_fansnodatalayout);
        include_fansnodata_btn = (Button) view.findViewById(R.id.include_fansnodata_btn);
        include_fansnodata_btn.setOnClickListener(this);
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
                    setFansNotData();
                    return;
                }
                showDataView();
                setHttpSuccess();
                fansRVAdapter.notifyDataSetChanged();
                page++;
            }
        });
    }

    private void setFansNotData() {
        include_fansnodatalayout.setVisibility(View.VISIBLE);
        rv_fans.setVisibility(View.GONE);
        include_fansnodata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream open = null;
                try {
                    open = getActivity().getAssets().open("ic_launcher.png");
                    BitmapDrawable fromStream = (BitmapDrawable) Drawable.createFromStream(open,null);
                    Bitmap bitmap = fromStream.getBitmap();
                    SharePop.getInstance().showPop(getActivity(), include_fansnodata_btn,
                            "我在这里买冻品一起来捡耙活！注册就有送，有买就有送，不买也有",
                            SPUtils.getInviteUrl(),
                            "我在这里买冻品一起来捡耙活！注册就有送，有买就有送，不买也有",
                            bitmap,
                            "http://p18.qhimg.com/t01024fff41e15d3348.png", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(open != null){
                        try {
                            open.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void showDataView() {
        include_fansnodatalayout.setVisibility(View.GONE);
        rv_fans.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {

    }
}
