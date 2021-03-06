package com.cqfrozen.jsh.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqfrozen.jsh.R;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CustomToast {

    private static CustomToast instance;

//    private Toast netToast;
    private Toast cartToast;
    private Context context;
    private View layout;
    private TextView text;
    private ImageView mImageView;

    private CustomToast(Context context) {
        this.context = context;
    }

    public static CustomToast getInstance(Context context) {
        if (instance == null) {
            synchronized (CustomToast.class) {
                if (instance == null) {
                    instance = new CustomToast(context);
                }
            }
        }
        return instance;
    }

//    /**
//     * 显示Toast
//     *
//     * @param tvString
//     */
//    public void showToast(String tvString) {
//
//        if (layout == null) {
//            layout = LayoutInflater.from(context).inflate(R.layout.custom_view_toast, null);
//            text = (TextView) layout.findViewById(R.id.text);
//            mImageView = (ImageView) layout.findViewById(R.id.iv);
////            mImageView.setBackgroundResource(R.mipmap.icon_add_cart_success);
//            text.setText(tvString);
//        }
//        if (netToast == null) {
//            netToast = new Toast(context);
//        }
//        netToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//        netToast.setView(layout);
//        netToast.show();
//    }

    /**
     * 显示Toast
     *
     * @param tvString
     */
    public void showToast(String tvString, int imgRes) {

        if (layout == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.custom_view_toast, null);
            text = (TextView) layout.findViewById(R.id.text);
            mImageView = (ImageView) layout.findViewById(R.id.iv);
        }
        mImageView.setBackgroundResource(imgRes);
        text.setText(tvString);
        if (cartToast == null) {
            cartToast = new Toast(context);
        }
        cartToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        cartToast.setView(layout);
        cartToast.show();
    }

}
