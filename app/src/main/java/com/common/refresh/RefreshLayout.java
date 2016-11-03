package com.common.refresh;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by admin on 2016/7/31.
 */
public class RefreshLayout extends SupportLayout {
    private float down_x;
    private float down_y;
    private float move_x;
    private float move_y;
    private int headerState = RefreshLayout.HeadMoveState.close;
    private int footerState = RefreshLayout.FootMoveState.close;
    private int oldHeaderState = RefreshLayout.FootMoveState.close;
    private int oldFooterState = RefreshLayout.FootMoveState.close;
    private boolean headercanOpen = false; //headerView是否能够展开
    private boolean footercanOpen = false; //footerView是否能够展开
    private boolean isHorizontal = false; //是否是横划

    public RefreshLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (isRefreshing() || isLoading()) {
            setViewEnable(true);
            return super.dispatchTouchEvent(e);
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getViewState();
                headercanOpen = false;
                footercanOpen = false;
                isHorizontal = false;
                down_x = e.getX();
                down_y = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                move_x = e.getX();
                move_y = e.getY();
//                Math.abs() 取绝对值  isHorizontal表示横划,取消view的改变
                if (Math.abs(down_x - move_x) > Math.abs(down_y - move_y) || isHorizontal) {
                    isHorizontal = true;
                    break;
                }
                setHeadMove();
                setFootMove();

                break;
            case MotionEvent.ACTION_UP:
                setUp();
                break;
        }
        return super.dispatchTouchEvent(e);
    }

    private void setUp() {
        getViewState();
        if (headerView != null) {
            if (headercanOpen) {
                setHeadMoveState(HeadMoveState.refreshing);
            } else {
                setHeadMoveState(HeadMoveState.close);
            }
        }
        if (footerView != null) {
            if (footercanOpen) {
                setFootMoveState(FootMoveState.loading);
            } else {
                setFootMoveState(FootMoveState.close);
            }
        }
    }

    private void setHeadMove() {
        if (headerView != null && refreshable) {
            if (headerParams.height == 0 && footerParams.height == 0) {
                setViewEnable(true);
            } else {
                setViewEnable(false);
            }
            if (!isCanRefresh) {
                return;
            }
            if (move_y - down_y > moveLength) {
                headercanOpen = true;
            }
            if (headercanOpen) {
                setHeadMoveState(HeadMoveState.uptorefresh);
            } else {
                setHeadMoveState(HeadMoveState.downtorefresh);
            }
        }
    }

    private void setFootMove() {
        if (footerView != null && loadable) {
            if (headerParams.height == 0 && footerParams.height == 0) {
                setViewEnable(true);
            } else {
                setViewEnable(false);
            }
            if (!isCanLoad && footerState != FootMoveState.uptoload) {
                return;
            }
            if ((down_y - move_y) >= moveLength) {
                footercanOpen = true;
            }

            if (footercanOpen) {
                setFootMoveState(FootMoveState.uptoload);
            } else {
                setFootMoveState(FootMoveState.downtoload);
            }
        }
    }

    private void setHeadMoveState(int state) {
        switch (state) {
            case HeadMoveState.close:
                headerParams.height = 0;
                break;
            case HeadMoveState.refreshing:
                headerParams.height = (int) viewHigth;
                break;
            case HeadMoveState.downtorefresh:
                headerParams.height = (int) (move_y - down_y) >= 0 ? (int) (move_y - down_y) : 0;
                break;
            case HeadMoveState.uptorefresh:
                if ((int) (move_y - down_y) <= viewHigth) {
                    headerParams.height = (int) viewHigth;
                } else if ((int) (move_y - down_y) <= moveLength) {
                    headerParams.height = (int) (move_y - down_y);
                } else {
                    headerParams.height = (int) (moveLength + (((move_y - down_y) - moveLength) *
                            1.2 / 3));
                }
                break;
        }
        headerState = state;
        if (oldHeaderState != state) {
            setHeadAnimation(state);
        }
        oldHeaderState = state;
        headerView.setLayoutParams(headerParams);
    }

    private void setFootMoveState(int state) {
        switch (state) {
            case FootMoveState.close:
                footerParams.height = 0;
                break;
            case FootMoveState.loading:
                footerParams.height = (int) viewHigth;
                break;
            case FootMoveState.downtoload:
                footerParams.height = (int) (down_y - move_y) >= 0 ? (int) (down_y - move_y) : 0;
                break;
            case FootMoveState.uptoload:
                if ((down_y - move_y) <= viewHigth) {
                    footerParams.height = (int) viewHigth;
                }
                if ((down_y - move_y) > (int) viewHigth && (down_y - move_y) < moveLength) {
                    footerParams.height = (int) (down_y - move_y);
                }
                if ((down_y - move_y) > moveLength) {
                    footerParams.height = (int) (moveLength + (((down_y - move_y) - moveLength) *
                            1.2 / 3));
                }
                break;
        }
        footerState = state;
        if (oldFooterState != state) {
            setFootAnimation(state);
        }
        oldFooterState = state;
        footerView.setLayoutParams(footerParams);
    }


    public boolean isRefreshing() {
        return headerState == HeadMoveState.refreshing;
    }

    public boolean isLoading() {
        return footerState == FootMoveState.loading;
    }

    public void setRefreshClose() {
        if (headerView != null) {
            setHeadMoveState(HeadMoveState.close);
        }
    }

    public void setLoadClose() {
        if (footerView != null) {
            setFootMoveState(FootMoveState.close);
        }
    }

    public void setRefreshSuccess() {
        //成功
        if (isRefreshing()) {
            setHeadAnimation(HeadMoveState.success);
            closeView(headerView, headerParams);
        }
    }

    public void setRefreshFailed() {
        //失败
        if (isRefreshing()) {
            setHeadAnimation(HeadMoveState.failed);
            closeView(headerView, headerParams);
        }
    }

    public void setLoadSuccess() {
        //成功
        if (isLoading()) {
            setFootAnimation(FootMoveState.success);
            closeView(footerView, footerParams);
        }
    }

    public void setLoadFailed() {
        //失败
        if (isLoading()) {
            setFootAnimation(FootMoveState.failed);
            closeView(footerView, footerParams);
        }
    }

    public void setLoadNodata() {
        //失败
        if (isLoading()) {
            setFootAnimation(FootMoveState.nodata);
            closeView(footerView, footerParams);
        }
    }


    /**
     * 结束状态时,关闭view
     */
    private void closeView(final View view, final ViewGroup.LayoutParams params) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (i = 0; params.height > 3; i++) {
                    try {
                        if (i == 0) {
                            Thread.sleep(1000);
                        } else {
                            Thread.sleep(20);
                        }
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                params.height = params.height - i * 4;
                                if (params.height < 0) {
                                    params.height = 0;
                                    getViewState();
                                    if (view == headerView) {
                                        setRefreshClose();
                                    }
                                    if (view == footerView) {
                                        setLoadClose();
                                    }
                                }
                                view.setLayoutParams(params);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
