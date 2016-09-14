package com.cqfrozen.jsh.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.base.BaseFragment;

/**
 * Created by Administrator on 2016/9/12.
 * 购物车页面 fragment
 */
public class CartFragment extends BaseFragment {

    private static CartFragment fragment;

    public static CartFragment getInstance(){
        if(fragment == null){
            fragment = new CartFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_cart, null);
        }
        return view;
    }
}
