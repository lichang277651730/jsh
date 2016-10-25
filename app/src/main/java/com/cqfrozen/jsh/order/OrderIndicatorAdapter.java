package com.cqfrozen.jsh.order;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.util.UIUtils;
import com.shizhefei.view.indicator.IndicatorViewPager;

/**
 * Created by Administrator on 2016/9/20.
 */
public class OrderIndicatorAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

    private String[] names = {"待付款", "待收货", "待评价", "全部"};
    private Context context;
    public OrderIndicatorAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classify_tab_top, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(names[position % names.length]);
        int padding = UIUtils.dip2px(12);
        textView.setPadding(padding, 0, padding, 0);
        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        return OrderFragment.getInstance(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
