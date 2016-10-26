package com.common.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cqfrozen.jsh.adapter.MyFragmentPagerAdapter;
import com.common.base.BaseFragment;

import java.util.HashMap;
import java.util.List;

public class MyViewPager extends ViewPager {
	private boolean isCanScroll = true;
	private boolean isHaveFragment = false;
	HashMap<String, Fragment> stopMap = new HashMap<String, Fragment>();
	private List<BaseFragment> mFragments;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOverScrollMode(View.OVER_SCROLL_NEVER); // 设置滑动到顶部的阴影
	}

	public MyViewPager(Context context) {
		super(context);
		setOverScrollMode(View.OVER_SCROLL_NEVER);// 设置滑动到顶部的阴影
	}

	/**
	 * 设置是否可以滑动
	 */
	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	/**
	 * 设置fragment
	 */
	public void setFragemnt(FragmentManager fragmentManager,
			final List<BaseFragment> mFragments) {
		this.mFragments = mFragments;
		isHaveFragment = true;
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
				fragmentManager, mFragments);
//		setOffscreenPageLimit(1);
		stopMap.put("Fragment", mFragments.get(0));
		setAdapter(adapter);
		setOnMyPageChangeListener(null);
	}

	public void setOnMyPageChangeListener(final OnMyPageChangeListener listener) {
		setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (null != listener) {
					listener.OnMyPageSelected(position);
				}
				if(isHaveFragment){
					stopMap.get("Fragment").onStop();
					stopMap.put("Fragment", mFragments.get(position));
					mFragments.get(position).onShow();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (null != listener) {
					listener.OnMyPonPageScrolled(arg0,arg1,arg2);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (null != listener) {
					listener.OnMyPageScrollStateChanged(arg0);
				}
			}
		});
	}

	public interface OnMyPageChangeListener {
		public void OnMyPageSelected(int arg0);
		public void OnMyPonPageScrolled(int arg0, float arg1, int arg2);
		public void OnMyPageScrollStateChanged(int arg0);
	}

	@Override
	public void scrollTo(int x, int y) {
		if (isCanScroll) {
			super.scrollTo(x, y);
		}
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
