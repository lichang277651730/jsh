package com.common.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.common.http.HttpForVolley;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BaseFragment extends Fragment {

    public View view;
    public FragmentActivity mActivity;
    public HttpForVolley http;
    private boolean isStopHttp = false;
    private Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(http == null){
            http = new HttpForVolley(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    /**
     * stop页面的时候是否执行取消网络请求
     */
    public void setStopHttp(boolean isStopHttp) {
        this.isStopHttp = isStopHttp;
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

    public HttpForVolley getHttp(){
        if(http == null){
            http = new HttpForVolley(this);
        }
        return http;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isStopHttp){
            BaseValue.mQueue.cancelAll(this);
        }
    }
}
