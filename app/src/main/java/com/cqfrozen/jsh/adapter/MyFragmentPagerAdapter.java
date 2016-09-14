package com.cqfrozen.jsh.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cqfrozen.jsh.base.BaseFragment;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> mFragments;
	public MyFragmentPagerAdapter(FragmentManager fm,
			List<BaseFragment> fragments) {
		super(fm);
		this.mFragments = fragments;
	}
	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}
	@Override
	public int getCount() {
		return mFragments.size();
	}
}
