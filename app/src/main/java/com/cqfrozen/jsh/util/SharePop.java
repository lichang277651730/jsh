package com.cqfrozen.jsh.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cqfrozen.jsh.R;

/**
 * Created by Administrator on 2016/9/29.
 */
public class SharePop implements View.OnClickListener {

    public static SharePop instance;
    private PopupWindow popupWindow;
    private Context context;

    private SharePop(Context context){
        this.context = context;
    }

    public static SharePop getInstance(Context context){
        if(instance == null){
            synchronized (SharePop.class){
                if(instance == null){
                    instance = new SharePop(context);
                }
            }
        }
        return instance;
    }

    public SharePop showPop(View view){
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        LinearLayout ll_wxhy = (LinearLayout) popView.findViewById(R.id.ll_wxhy);
        LinearLayout ll_pyq = (LinearLayout) popView.findViewById(R.id.ll_pyq);
        LinearLayout ll_xlwb = (LinearLayout) popView.findViewById(R.id.ll_xlwb);
        LinearLayout ll_qqhy = (LinearLayout) popView.findViewById(R.id.ll_qqhy);
        LinearLayout ll_qqkj = (LinearLayout) popView.findViewById(R.id.ll_qqkj);
        LinearLayout ll_link = (LinearLayout) popView.findViewById(R.id.ll_link);
        TextView tv_cancel = (TextView) popView.findViewById(R.id.tv_cancel);

        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        ll_wxhy.setOnClickListener(this);
        ll_pyq.setOnClickListener(this);
        ll_xlwb.setOnClickListener(this);
        ll_qqhy.setOnClickListener(this);
        ll_qqkj.setOnClickListener(this);
        ll_link.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        return instance;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wxhy:
                ToastUtil.showToast(context, "微信好友");
                break;
            case R.id.ll_pyq:
                ToastUtil.showToast(context, "朋友圈");
                break;
            case R.id.ll_xlwb:
                ToastUtil.showToast(context, "新浪微博");
                break;
            case R.id.ll_qqhy:
                ToastUtil.showToast(context, "QQ好友");
                break;
            case R.id.ll_qqkj:
                ToastUtil.showToast(context, "QQ空间");
                break;
            case R.id.ll_link:
                ToastUtil.showToast(context, "复制链接");
                break;
            case R.id.tv_cancel:
                break;
            default:
                break;
        }
        popupWindow.dismiss();
    }
}
