package com.cqfrozen.jsh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyFragment;

/**
 * Created by Administrator on 2016/9/20.
 */
public class GoodsFragment extends MyFragment {
    private View view;
    private TextView tv_name;
    private String title;

    public static GoodsFragment getInstance(String title){
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getBundleArguments();
        view = inflater.inflate(R.layout.fragment_goods, null);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        initData();
        return view;
    }

    private void initData() {
        tv_name.setText(title);
    }

    private void getBundleArguments() {
        Bundle bundle = getArguments();
        this.title = bundle.getString("title");
    }
}
