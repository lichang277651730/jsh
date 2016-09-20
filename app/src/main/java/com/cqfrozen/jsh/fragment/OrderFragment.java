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
public class OrderFragment extends MyFragment {

    private View view;
    private TextView tv_title;
    private String title;

    public static OrderFragment getInstance(String txt){
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", txt);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        getBundleArgument();
        view = inflater.inflate(R.layout.fragment_order, null);
        initView();
        getData();
        return view;
    }

    private void getBundleArgument() {
        Bundle bundle = getArguments();
        this.title = bundle.getString("title");
    }

    private void initView() {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
    }

    private void getData() {
        tv_title.setText(title);
    }
}
