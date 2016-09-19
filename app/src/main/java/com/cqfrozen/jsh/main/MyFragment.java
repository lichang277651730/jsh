package com.cqfrozen.jsh.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.base.BaseFragment;
import com.cqfrozen.jsh.R;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MyFragment extends BaseFragment {


    private RelativeLayout include_framelayout;
    private LinearLayout include_faillayout;
    private Button include_fail_btn;
    private LinearLayout include_nodatalayout;
    private Button include_nodata_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getIncludeView(){
        if(view != null && include_framelayout != null){
            include_framelayout = (RelativeLayout) view.findViewById(R.id.include_framelayout);
            include_faillayout = (LinearLayout) view.findViewById(R.id.include_faillayout);
            include_fail_btn = (Button) view.findViewById(R.id.include_fail_btn);
            include_nodatalayout = (LinearLayout) view.findViewById(R.id.include_nodatalayout);
            include_nodata_btn = (Button) view.findViewById(R.id.include_nodata_btn);
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
    }
}
