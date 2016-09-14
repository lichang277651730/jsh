package com.cqfrozen.jsh.classify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.base.BaseFragment;

/**
 * Created by Administrator on 2016/9/12.
 * 分类页面 fragment
 */
public class ClassifyFragment extends BaseFragment {

    private static ClassifyFragment fragment;

    public static ClassifyFragment getInstance(){
        if(fragment == null){
            fragment = new ClassifyFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_classify, null);
        }
        return view;
    }
}
