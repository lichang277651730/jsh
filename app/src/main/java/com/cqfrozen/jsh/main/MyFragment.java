package com.cqfrozen.jsh.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.base.BaseFragment;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.entity.UserInfo;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MyFragment extends BaseFragment {


    private RelativeLayout include_framelayout;
    private LinearLayout include_faillayout;
    private Button include_fail_btn;
    private LinearLayout include_nodatalayout;
    private Button include_nodata_btn;
//    private LinearLayout include_cartnodatalayout;
//    private Button include_cartnodata_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 判断是否登陆
     */
    public boolean isLogined(){
        if(MyApplication.userInfo == null){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 点击某个需要登陆的事件，就调此方法拦截
     * true:不需要登陆
     * false:需要登陆
     */
    public boolean needLogin(){
        if(!isLogined()){
            startActivity(new Intent(mActivity, LoginActivity.class));
            return false;
        }
        return true;
    }

    /**
     * 获取登陆用户
     */
    public UserInfo getUserInfo(){
        if(needLogin()){
            return MyApplication.userInfo;
        }
        return null;
    }




    private void getIncludeView(){
        if(view != null && include_framelayout == null){
            include_framelayout = (RelativeLayout) view.findViewById(R.id.include_framelayout);
            include_faillayout = (LinearLayout) view.findViewById(R.id.include_faillayout);
            include_fail_btn = (Button) view.findViewById(R.id.include_fail_btn);
            include_nodatalayout = (LinearLayout) view.findViewById(R.id.include_nodatalayout);
            include_nodata_btn = (Button) view.findViewById(R.id.include_nodata_btn);
//            include_cartnodatalayout = (LinearLayout) view.findViewById(R.id.include_cartnodatalayout);
//            include_cartnodata_btn = (Button) view.findViewById(R.id.include_cartnodata_btn);
        }
    }

    public void setHttpFail(final HttpFail httpFail) {
        getIncludeView();
        include_framelayout.setVisibility(View.VISIBLE);
        include_faillayout.setVisibility(View.VISIBLE);
        include_nodatalayout.setVisibility(View.GONE);
        include_fail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpFail.toHttpAgain();
            }
        });
    }

    public void setHttpNotData(final HttpFail httpFail) {
        getIncludeView();
        include_framelayout.setVisibility(View.VISIBLE);
        include_faillayout.setVisibility(View.GONE);
        include_nodatalayout.setVisibility(View.VISIBLE);
        include_nodata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpFail.toHttpAgain();
            }
        });
    }

    public void setHttpSuccess() {
        getIncludeView();
        include_framelayout.setVisibility(View.GONE);
    }

    public interface HttpFail {
        void toHttpAgain();
//        void goBuy();
    }
}
