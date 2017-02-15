package com.common.refresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;

/**
 * Created by admin on 2016/7/31.
 */
public class SupportLayout extends LinearLayout {
    protected RefreshListener refreshListener;
    protected LoadMoreListener loadMoreListener;
    protected View headerView;
    protected View footerView;
    protected RecyclerView recyclerView;
    protected boolean isCanRefresh = true;
    protected boolean isCanLoad = false;
    protected boolean refreshable = true;
    protected boolean loadable = true;

    private int urlNum = 0;

    protected float viewHigth = BaseValue.dp2px(50);
    protected float moveLength = BaseValue.dp2px(100);

    protected ViewGroup.LayoutParams headerParams;
    protected ViewGroup.LayoutParams footerParams;
    protected TextView headerTv;
    protected ImageView headerIv;
    protected TextView footerTv;
    protected ImageView footerIv;
    protected RotateAnimation rotateAnimation;
    protected RotateAnimation rotatingAnimation;
    protected LinearLayoutManager manager;
    protected int i;
    private LinearLayout ll_head;
    private FrameLayout fl_head_view;

    public SupportLayout(Context context) {
        super(context);
    }

    public SupportLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        headerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        footerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        initAnimation();
        initHeaderView();
        initFooterView();
        initRecyclerView();
        super.onSizeChanged(w, h, oldw, oldh);
    }


    protected void setHeadAnimation(int state) {
        headerIv.clearAnimation();
        switch (state) {
            case HeadMoveState.close:
                break;
            case HeadMoveState.refreshing:
                headerTv.setText("正在刷新...");
                headerIv.setImageResource(R.mipmap.refreshing);
                headerIv.startAnimation(rotatingAnimation);
                refreshListener.refresh();
                urlNum = 0;
                break;
            case HeadMoveState.downtorefresh:
                headerTv.setText("下拉刷新");
                headerIv.setImageResource(R.mipmap.to_down);
                break;
            case HeadMoveState.uptorefresh:
                headerTv.setText("释放立即刷新");
                headerIv.setImageResource(R.mipmap.to_down);
                headerIv.startAnimation(rotateAnimation);
                break;
            case HeadMoveState.success:
                headerTv.setText("刷新成功");
                headerIv.setImageResource(R.mipmap.refresh_succeed);
                break;
            case HeadMoveState.failed:
                headerTv.setText("刷新失败");
                headerIv.setImageResource(R.mipmap.refresh_failed);
                break;
        }
    }

    protected void setFootAnimation(int state) {
        footerIv.clearAnimation();
        switch (state) {
            case FootMoveState.close:
                break;
            case FootMoveState.loading:
                footerTv.setText("正在加载更多...");
                footerIv.setImageResource(R.mipmap.refreshing);
                footerIv.startAnimation(rotatingAnimation);
                loadMoreListener.loadMore();
                break;
            case FootMoveState.downtoload:
                footerTv.setText("上划加载更多");
                footerIv.setImageResource(R.mipmap.to_up);
                break;
            case FootMoveState.uptoload:
                footerTv.setText("释放立即加载");
                footerIv.setImageResource(R.mipmap.to_up);
                footerIv.startAnimation(rotateAnimation);
                break;
            case FootMoveState.success:
                footerTv.setText("加载更多成功");
                footerIv.setImageResource(R.mipmap.refresh_succeed);
                break;
            case FootMoveState.failed:
                footerTv.setText("加载更多失败");
                footerIv.setImageResource(R.mipmap.refresh_failed);
                break;
            case FootMoveState.nodata:
                footerTv.setText("没有数据了~");
                footerIv.setImageResource(R.mipmap.refresh_failed);
                break;
        }
    }

    protected void setViewEnable(final boolean viewEnable) {
        if (recyclerView == null) {
            return;
        }
//        recyclerView.setLayoutFrozen(!viewEnable);
        recyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return !viewEnable;
            }
        });
    }

    protected void getViewState() {
        if (recyclerView == null|| manager == null) {
            return;
        }
        if (manager.findLastCompletelyVisibleItemPosition() == manager
                .getItemCount() -
                1) {
            //底部
            isCanLoad = true;
        } else {
            isCanLoad = false;
        }
    }

    private void initRecyclerView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof RecyclerView) {
                recyclerView = (RecyclerView) getChildAt(i);
                 manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (SupportLayout.this.manager.findFirstCompletelyVisibleItemPosition() == 0) {
                            //顶部
                            isCanRefresh = true;
                        } else {
                            isCanRefresh = false;
                        }
                        if (SupportLayout.this.manager.findLastCompletelyVisibleItemPosition() == SupportLayout.this.manager
                                .getItemCount() -
                                1) {
                            //底部
                            if (loadMoreListener != null && loadable) {
                                loadMoreListener.loadMore();
                            }
                            isCanLoad = true;
                        } else {
                            isCanLoad = false;
                        }
                    }
                });
            }
        }
    }

    private void initAnimation() {
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                getContext(), R.anim.reverse_anim);
        rotatingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                getContext(), R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        rotatingAnimation.setInterpolator(lir);
        rotateAnimation.setFillAfter(true);
    }

    private void initHeaderView() {
        if (refreshListener != null) { //有下拉刷新interface,添加headerview
            headerView = LayoutInflater.from(getContext()).inflate(R.layout.header, null);
            headerTv = (TextView) headerView.findViewById(R.id.textView);
            ll_head = (LinearLayout) headerView.findViewById(R.id.ll_head);
            fl_head_view = (FrameLayout) headerView.findViewById(R.id.head_view);
            headerIv = (ImageView) headerView.findViewById(R.id.imageView);
            headerParams.height = 0;
            headerView.setLayoutParams(headerParams);
            addView(headerView, 0);
        }
    }

    public void setHeaderBgColor(int colorRes){
        ll_head.setBackgroundColor(colorRes);
        fl_head_view.setBackgroundColor(colorRes);
    }

    private void initFooterView() {
        if (loadMoreListener != null) {//有加载更多interface,添加footerview
            footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer, null);
            footerTv = (TextView) footerView.findViewById(R.id.textView);
            footerIv = (ImageView) footerView.findViewById(R.id.imageView);
            footerParams.height = 0;
            footerView.setLayoutParams(footerParams);
            addView(footerView, getChildCount());
        }
    }

    public void setOnRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setOnLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * 下拉刷新监听
     */
    public interface RefreshListener {
        void refresh();
    }

    /**
     * 加载更多监听
     */
    public interface LoadMoreListener {
        void loadMore();
    }

    /**
     * FooterView的各种状态
     */
    protected static class FootMoveState {
        final static int close = 0;
        final static int loading = 1;
        final static int downtoload = 2;
        final static int uptoload = 3;
        final static int success = 4;
        final static int failed = 5;
        final static int nodata = 6;

    }

    /**
     * HeaderView的各种状态
     */
    protected static class HeadMoveState {
        final static int close = 0;
        final static int refreshing = 1;
        final static int downtorefresh = 2;
        final static int uptorefresh = 3;
        final static int success = 4;
        final static int failed = 5;
    }

    /**
     * 设置能否下拉刷新
     */
    public void setRefreshable(boolean refreshable) {
        this.refreshable = refreshable;
    }

    /**
     * 设置能否加载更多
     */
    public void setLoadable(boolean loadable) {
        this.loadable = loadable;
    }

    public int getUrlNum() {
        return urlNum;
    }

    public void setUrlNum() {
        urlNum++;
    }
}
