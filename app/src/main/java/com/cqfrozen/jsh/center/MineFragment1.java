package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.common.base.BaseValue;
import com.common.zoomview.PullToZoomScrollViewEx;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyFragment;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MineFragment1 extends MyFragment {

    private static MineFragment1 fragment;
    private PullToZoomScrollViewEx zsv_mine;

    public static MineFragment1 getInstance(){
        if(fragment == null){
            fragment = new MineFragment1();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_mine1, null);
            initView();
        }
        return view;

    }

    private void initView() {
        zsv_mine = (PullToZoomScrollViewEx) view.findViewById(R.id.zsv_mine);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_header, null, false);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_zoom, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_content, null, false);
        zsv_mine.setHeaderView(headView);
        zsv_mine.setZoomView(zoomView);
        zsv_mine.setScrollContentView(contentView);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(BaseValue.screenWidth, (int) (9.0F * (BaseValue.screenWidth / 16.0F)));
        zsv_mine.setHeaderLayoutParams(localObject);

    }
}
