package com.cqfrozen.jsh.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.common.base.BaseValue;

/**
 * Created by Administrator on 2016/10/26.
 */
public class MeasureUtil {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        setListViewHeightBasedOnChildren(listView, null);
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        int tvHeight = 0;
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + tvHeight +BaseValue.dp2px(20);
//        listView.setLayoutParams(params);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, TextView tv) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int tvHeight = 0;
        if(tv != null && tv.getVisibility() == View.VISIBLE){
            tvHeight = BaseValue.dp2px(60);
        }
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + tvHeight + BaseValue.dp2px(20);
        listView.setLayoutParams(params);
    }

}
