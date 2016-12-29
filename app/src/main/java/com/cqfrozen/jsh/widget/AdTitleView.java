package com.cqfrozen.jsh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;

/**
 * Created by Administrator on 2016/12/28.
 */
public class AdTitleView extends LinearLayout {

    private Context context;
    private View adView;
    private TextView tv_py;
    private TextView tv_hz;

    public AdTitleView(Context context) {
        this(context, null);
    }

    public AdTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = BaseValue.dp2px(10);
        params.topMargin = BaseValue.dp2px(10);
        params.leftMargin = BaseValue.dp2px(30);
        params.rightMargin = BaseValue.dp2px(30);
        setLayoutParams(params);
        setOrientation(HORIZONTAL);
    }

    public void setData(String[] hanzi, String[] pingyin){
        for(int i = 0; i < hanzi.length; i++){
            adView = LayoutInflater.from(context).inflate(R.layout.view_ad_title, null, false);
            tv_py = (TextView) adView.findViewById(R.id.tv_py);
            tv_hz = (TextView) adView.findViewById(R.id.tv_hz);
            tv_py.setText(pingyin[i]);
            tv_hz.setText(hanzi[i]);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup
                            .LayoutParams.WRAP_CONTENT);
            params.width = 0;
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            addView(adView, params);
        }
    }
}
