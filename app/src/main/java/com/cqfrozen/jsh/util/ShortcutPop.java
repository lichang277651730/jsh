package com.cqfrozen.jsh.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.home.SearchActivity;

/**
 * Created by Administrator on 2016/10/13.
 */
public class ShortcutPop implements View.OnClickListener {

    private static ShortcutPop instance;
    private Context context;
    private PopupWindow popupWindow;

    private ShortcutPop(Context context){
        this.context = context;
    }

    public static ShortcutPop getInstance(Context context){
        if(instance == null){
            synchronized (ShortcutPop.class){
                if(instance == null){
                    instance = new ShortcutPop(context);
                }
            }
        }
        return instance;
    }

    public void showPop(View view){
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_shortcut, null);
        View pop_shortcut_search = popView.findViewById(R.id.pop_shortcut_search);
        View pop_shortcut_home = popView.findViewById(R.id.pop_shortcut_home);
        pop_shortcut_search.setOnClickListener(this);
        pop_shortcut_home.setOnClickListener(this);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view, BaseValue.dp2px(-6), BaseValue.dp2px(8));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_shortcut_search:
                context.startActivity(new Intent(context, SearchActivity.class));
                break;
            case R.id.pop_shortcut_home:
//                context.startActivity(new Intent(context, SearchActivity.class));
                ((HomeActivity)context).setHomeFragment();
                break;
            default:
                break;
        }
        popupWindow.dismiss();
    }
}
