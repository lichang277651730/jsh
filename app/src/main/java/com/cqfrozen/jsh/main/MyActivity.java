package com.cqfrozen.jsh.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.BaseActivity;
import com.cqfrozen.jsh.R;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MyActivity extends BaseActivity {


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
}
