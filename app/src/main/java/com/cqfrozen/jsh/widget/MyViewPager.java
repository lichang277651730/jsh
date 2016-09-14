package com.cqfrozen.jsh.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cqfrozen.jsh.adapter.MyFragmentPagerAdapter;
import com.cqfrozen.jsh.base.BaseFragment;

import java.util.HashMap;
import java.util.List;

public class MyViewPager extends ViewPager {

	private boolean isCanScroll = true;
	private boolean isHaveFragment = false;
	private List<BaseFragment> mFragments;
	private HashMap<String, Fragment> stopMap = new HashMap<String, Fragment>();
	
	public void setFragment(FragmentManager fm, List<BaseFragment> fragments){
		this.mFragments = fragments;
		isHaveFragment = true;
		setOffscreenPageLimit(0);
		stopMap.put("Fragment", mFragments.get(0));
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fm, mFragments);
		setAdapter(adapter);
		setOnMyPageChangeListener(null);
	}
	
	public void setOnMyPageChangeListener(final OnMyPageChangeListener listener){
		setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(listener != null){
					listener.onMyPageSelected(position);
				}
				
				if(isHaveFragment){
					stopMap.get("Fragment").onStop();
					stopMap.put("Fragment", mFragments.get(position));
					mFragments.get(position).onShow();
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				if(listener != null){
					listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				if(listener != null){
					listener.onMyPageScrollStateChanged(state);
				}
			}
		});
	}
	
	public MyViewPager(Context context) {
		super(context);
		setOverScrollMode(View.OVER_SCROLL_NEVER);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setScanScroll(boolean isCanScroll){
		this.isCanScroll = isCanScroll;
	}
	
	@Override
	public void scrollTo(int x, int y) {
		if(isCanScroll){
			super.scrollTo(x, y);
		}
	}

	/**
	 * 禁止滑动
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	public interface OnMyPageChangeListener{
		void onMyPageScrollStateChanged(int state);

		void onMyPageSelected(int position);

		void onPageScrolled(int position, float positionOffset,
							int positionOffsetPixels);
		
	}
	


}
