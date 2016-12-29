package com.cqfrozen.jsh.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cqfrozen.jsh.entity.GoodDetailResultInfo;
import com.cqfrozen.jsh.fragment.ShowImgsFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */
public class ImgsVPAdapter extends FragmentPagerAdapter {

    private List<GoodDetailResultInfo.PicsInfo> picsInfos;
    public ImgsVPAdapter(FragmentManager fm, List<GoodDetailResultInfo.PicsInfo> picsInfos) {
        super(fm);
        this.picsInfos = picsInfos;
    }

    @Override
    public Fragment getItem(int position) {
        return ShowImgsFragment.getInstance(picsInfos.get(position));
    }

    @Override
    public int getCount() {
        return picsInfos.size();
    }
}
