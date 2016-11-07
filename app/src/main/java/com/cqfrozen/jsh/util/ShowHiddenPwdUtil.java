package com.cqfrozen.jsh.util;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;

/**
 * Created by Administrator on 2016/10/13.
 * 显示和隐藏输入的密码工具类
 * 注册时 同意和不用意视图的切换
 */
public class ShowHiddenPwdUtil {
    private static final int TAG_PWD_SHOW = 1;
    private static final int TAG_PWD_HIDDEN = 2;

    private static final int TAG_ALLOW_YES = 1;
    private static final int TAG_ALLOW_NO = 2;

    /**
     * 同意和不用视图的切换
     */
    public static void initAllow(final ImageView iv_allow, final TextView tv_allow, final Button btn_go) {
        iv_allow.setTag(TAG_ALLOW_YES);
        iv_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curAllowTag = (int) v.getTag();
                if(curAllowTag == TAG_ALLOW_YES){//当前是同意，点击变不同意
                    iv_allow.setImageResource(R.mipmap.icon_register_allow_not);
                    tv_allow.setTextColor(UIUtils.getColor(R.color.mygray));
                    tv_allow.setEnabled(false);
                    btn_go.setEnabled(false);
                    iv_allow.setTag(TAG_ALLOW_NO);
                    btn_go.setBackgroundResource(R.drawable.shape_no_press_btn_bg);
                }else if(curAllowTag == TAG_ALLOW_NO){//当前是不同意，点击变同意
                    iv_allow.setImageResource(R.mipmap.icon_register_allow_yes);
                    tv_allow.setTextColor(UIUtils.getColor(R.color.myblack));
                    tv_allow.setEnabled(true);
                    btn_go.setEnabled(true);
                    iv_allow.setTag(TAG_ALLOW_YES);
                    btn_go.setBackgroundResource(R.drawable.sl_blue2gray_btn_bg);
                }
            }
        });
    }

    /**
     * 显示和隐藏密码
     */
    public static void initShowHiddenPwdView(final ImageView iv_see, final MyEditText et_pwd) {

        iv_see.setTag(TAG_PWD_HIDDEN);
        iv_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPwdTad = (int) v.getTag();
                if(curPwdTad == TAG_PWD_HIDDEN){//当前是隐藏密码，点击显示密码
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_see.setTag(TAG_PWD_SHOW);
                }else if(curPwdTad == TAG_PWD_SHOW){//当前是显示密码，点击隐藏密码
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_see.setTag(TAG_PWD_HIDDEN);
                }
                setSlection(et_pwd);
            }
        });
    }

    private static void setSlection(MyEditText etView) {
        if(etView.getText() != null && !etView.getText().equals("")){
            etView.setSelection(etView.getText().length());
        }
    }
}
