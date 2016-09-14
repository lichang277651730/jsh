package com.cqfrozen.jsh.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cqfrozen.jsh.volleyhttp.HttpForVolley;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BaseFragment extends Fragment {

    public View view;
    public Activity mActivity;
    public HttpForVolley http;
    public Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        if(http == null){
            http = new HttpForVolley(this);
        }
    }

    /**
     * 提示框
     */
    public void showToast(String str) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            Log.e("error", "error:" + e.getMessage());
        }
    }

    public void onShow(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(view != null){
            ((ViewGroup)this.view.getParent()).removeView(this.view);
        }
    }


}
