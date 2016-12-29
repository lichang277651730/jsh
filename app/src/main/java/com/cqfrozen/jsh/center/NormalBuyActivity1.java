package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.NormalBuyAdapter1;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.entity.GoodsResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.cqfrozen.jsh.widget.swiperv.baseview.PtrSwipeMenuRecyclerView;
import com.cqfrozen.jsh.widget.swiperv.interfaces.OnMenuClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */
public class NormalBuyActivity1 extends MyActivity implements MyActivity.HttpFail {

    private PtrSwipeMenuRecyclerView rv_normalbuy1;
    private List<GoodsInfo> goodsInfos = new ArrayList<>();
    private NormalBuyAdapter1 normalBuyAdapter1;
    private int page = 1;
    private int is_page = 0;
    private int is_oos;//是否缺货0否，1是

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normalbuy1);
        initView();
        initRV();
        getData();
    }

    private void getData() {
        MyHttp.commonGoodsList(http, null, page, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {

                if(code == 404){
//                    setHttpFail(NormalBuyActivity1.this);
                    return;
                }

                if(code != 0){
//                    showToast(msg);
                    return;
                }
                GoodsResultInfo goodsResultInfo = (GoodsResultInfo)bean;
                is_page = goodsResultInfo.is_page;
                if(goodsResultInfo == null || goodsResultInfo.data1.size() == 0){
//                    setHttpNotData(NormalBuyActivity1.this);
                    return;
                }
//                setHttpSuccess();
                goodsInfos.addAll(goodsResultInfo.data1);
                normalBuyAdapter1.notifyDataSetChanged();
                page++;
            }
        });
    }

    private void initView() {
        setMyTitle("常用采购");
        rv_normalbuy1 = (PtrSwipeMenuRecyclerView) findViewById(R.id.rv_normalbuy1);
    }

    private void initRV() {
        //参数：context,横向或纵向滑动，是否颠倒显示数据
        rv_normalbuy1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        normalBuyAdapter1 = new NormalBuyAdapter1(this, goodsInfos);
        rv_normalbuy1.setAdapter(normalBuyAdapter1);
//        //设置不允许上拉加载更多
//        recyclerView.setPullLoadMoreEnable(false);
//        //设置不允许下拉刷新
//        recyclerView.setPullToRefreshEnable(false);
        //添加菜单点击监听事件
        rv_normalbuy1.setOnMenuClickListener(onMenuClickListener);
        rv_normalbuy1.setOnPullListener(onPullListener);
    }

    /**
     * 菜单点击事件监听
     */
    private OnMenuClickListener onMenuClickListener = new OnMenuClickListener() {
        @Override
        public void onMenuClick(View view, int position) {
//            if (view.getId() == R.id.menu1){
//                ToastUtil.showToast("position:" + position + ",menu1", 0);
//            }
            GoodsInfo goodsInfo = goodsInfos.get(position);
            showToast(goodsInfo.g_name);
        }
    };

    /**
     * 上拉加载、下拉刷新监听
     */
    private PtrSwipeMenuRecyclerView.OnPullListener onPullListener = new PtrSwipeMenuRecyclerView.OnPullListener() {
        @Override
        public void onRefresh() {
            page = 1;
            is_page = 0;
            goodsInfos.clear();
            getData();
        }

        @Override
        public void onLoadMore() {
            if(is_page == 1){
                getData();
            }else if(is_page == 0){
            }
        }
    };

    @Override
    public void toHttpAgain() {
        getData();
    }
}
