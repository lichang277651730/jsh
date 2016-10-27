package com.cqfrozen.jsh.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.common.base.BaseFragment;

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

	//解决Fragement no longer exists for key f0: index 0 异常
	@Override
	public Parcelable saveState() {
		return null;
	}
}
