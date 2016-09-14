package com.cqfrozen.jsh.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/9/14.
 */
public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * 拦截ViewPager的触摸事件, 不做任何处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * 表示不对事件进行拦截, 从而可以使嵌套在ViewPager内部的ViewPager可以响应滑动动作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
