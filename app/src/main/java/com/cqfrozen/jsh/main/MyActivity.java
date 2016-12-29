package com.cqfrozen.jsh.main;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.BaseActivity;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.entity.UserInfo;
import com.cqfrozen.jsh.util.UMengUtils;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MyActivity extends BaseActivity {

    //对网络失败，数据为空的统一处理
    private RelativeLayout include_framelayout;
    private LinearLayout include_faillayout;
    private Button include_fail_btn;
    private LinearLayout include_nodatalayout;
    private Button include_nodata_btn;

    private void getIncludeView(){
        if(include_framelayout == null){
            include_framelayout = (RelativeLayout) findViewById(R.id.include_framelayout);
            include_faillayout = (LinearLayout) findViewById(R.id.include_faillayout);
            include_fail_btn = (Button) findViewById(R.id.include_fail_btn);
            include_nodatalayout = (LinearLayout) findViewById(R.id.include_nodatalayout);
            include_nodata_btn = (Button) findViewById(R.id.include_nodata_btn);
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

    public void setHttpSuccess() {
        getIncludeView();
        include_framelayout.setVisibility(View.GONE);
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


    /**
     * 设置标题和返回按钮的点击
     */
    public void setMyTitle(String title) {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);

        if (null != tv_title) {
            tv_title.setText(title);
        }

        //返回按钮
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置标题和返回按钮的点击
     */
    public void setMyTitle(String title, String right) {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_right = (TextView) findViewById(R.id.tv_right);

        if (null != tv_title) {
            tv_title.setText(title);
        }

        if (null != tv_right) {
            tv_right.setText(right);
        }

        //返回按钮
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**判断是否登陆
     * @return  true 已经登陆  false 没有登陆
     */
    public boolean isLogined(){
        if(MyApplication.userInfo == null){
            return false;
        }else {
            return true;
        }
    }

    public UserInfo getUserInfo() {
        if (needLogin()) {
            return MyApplication.userInfo;
        }
        return null;
    }

    /**
     * 点击某个需要登陆的事件，就调此方法拦截
     * true:不需要登陆
     * false:需要登陆
     */
    public boolean needLogin(){
        if(!isLogined()){
            startActivity(new Intent(this, LoginActivity.class));
            return false;
        }
        return true;
    }

    public interface HttpFail {
        void toHttpAgain();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengUtils.setOnPageStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMengUtils.setOnonPageEnd(this);
    }


    //    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Intent i = getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage(getBaseContext().getPackageName());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//    }
}
