package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyFragment;

/**
 * Created by Administrator on 2016/9/12.
 * 个人中心页面 fragment
 */
public class MineFragment extends MyFragment implements View.OnClickListener {

    private static MineFragment fragment;
    private TextView tv_lookall;
    private ImageView iv_setting;
    private TextView tv_address;

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
            initView();
        }
        return view;
    }

    private void initView() {
        tv_lookall = (TextView) view.findViewById(R.id.tv_lookall);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_lookall.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        tv_address.setOnClickListener(this);
    }

    @Override
    public void onShow() {
        super.onShow();
        Log.d("FragmentShow", "MineFragment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting:
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.tv_lookall:
                startActivity(new Intent(mActivity, OrderListActivity.class));
                break;
            case R.id.tv_address:
                startActivity(new Intent(mActivity, AddressListActivity.class));
                break;
            default:
                break;
        }
    }
}
