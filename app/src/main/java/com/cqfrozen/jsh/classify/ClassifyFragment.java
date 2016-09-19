package com.cqfrozen.jsh.classify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.base.BaseFragment;
import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;

/**
 * Created by Administrator on 2016/9/12.
 * 分类页面 fragment
 */
public class ClassifyFragment extends BaseFragment {

    private static ClassifyFragment fragment;
    private ViewPager vp_classify;

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
            initView();
            initVP();
        }
        return view;
    }

    private void initView() {
    }

    private void initVP() {
    }

    @Override
    public void onShow() {
        super.onShow();
        Log.d("FragmentShow", "ClassifyFragment" + BaseValue.density);
        Log.d("miduvalue", BaseValue.density + "");
    }
}
