package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.base.BaseFragment;

/**
 * Created by Administrator on 2016/9/12.
 * 个人中心页面 fragment
 */
public class MineFragment extends BaseFragment {

    private static MineFragment fragment;

    public static MineFragment getInstance(){
        if(fragment == null){
            fragment = new MineFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_mine, null);
        }
        return view;
    }
}
