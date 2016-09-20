package com.cqfrozen.jsh.classify;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.CategoryInfo;
import com.cqfrozen.jsh.fragment.GoodsFragment;
import com.cqfrozen.jsh.util.UIUtils;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class ClassifyIndicatorAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

    private String[] names = {"鸡鸭类", "猪肉类", "牛羊类", "丸子类", "水产类", "素食类", "其他类"};
    private Context context;
    private List<CategoryInfo> categoryInfos;
    public ClassifyIndicatorAdapter(FragmentManager fragmentManager, Context context, List<CategoryInfo> categoryInfos) {
        super(fragmentManager);
        this.context = context;
        this.categoryInfos = categoryInfos;
    }

    @Override
    public int getCount() {
        return categoryInfos.size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classify_tab_top, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(categoryInfos.get(position % categoryInfos.size()).t_type_name);
        int padding = UIUtils.dip2px(12);
        textView.setPadding(padding, 0, padding, 0);
        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        return GoodsFragment.getInstance(categoryInfos.get(position % categoryInfos.size()).t_type_name);
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
