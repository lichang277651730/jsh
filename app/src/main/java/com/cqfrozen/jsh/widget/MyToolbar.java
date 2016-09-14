package com.cqfrozen.jsh.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cqfrozen.jsh.R;


/**
 * Created by lichang on 2016/8/30.
 */
public class MyToolbar extends Toolbar {

    private LayoutInflater mInflater;
    private View mView;
    private TextView tv_title;
    private EditText et_search;
    private Button btn_right;
    private TextView tv_right;

    public MyToolbar(Context context) {
        this(context, null);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setContentInsetsRelative(10, 10);
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.MyToolbar, defStyleAttr, 0);
        final Drawable navIcon = a.getDrawable(R.styleable.MyToolbar_rightButtonIcon);
        if (navIcon != null) {
            setRightButton(navIcon);
        }

        boolean isSearchShow = a.getBoolean(R.styleable.MyToolbar_isSearchShow, false);
        showSearchBar(isSearchShow);

        CharSequence rightBtnTxt = a.getText(R.styleable.MyToolbar_rightButtonText);
        if(rightBtnTxt != null){
            setRightBtnText(rightBtnTxt);
        }
        CharSequence rightTxt = a.getText(R.styleable.MyToolbar_rightText);
        if(rightTxt != null){
            setRightText(rightTxt);
        }

        a.recycle();
    }

    private void setRightText(CharSequence rightTxt) {
        if(tv_right != null){
            tv_right.setText(rightTxt);
            tv_right.setVisibility(VISIBLE);
        }
    }

    private void initView() {
        if(mView == null){
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.widget_toolbar, null);
            tv_title = (TextView)mView.findViewById(R.id.tv_title);
            et_search = (EditText)mView.findViewById(R.id.et_search);
            btn_right = (Button)mView.findViewById(R.id.btn_right);
            tv_right = (TextView)mView.findViewById(R.id.tv_right);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, params);
        }
    }

    public void setRightButton(Drawable icon){
        if(btn_right != null){
            btn_right.setBackgroundDrawable(icon);
            btn_right.setVisibility(VISIBLE);
        }
    }

    public void setRightButton(int iconId){
        setRightButton(getResources().getDrawable(iconId));
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if(tv_title != null){
            tv_title.setText(title);
        }
        showTitle();
    }

    public void showSearchBar(boolean isSearchShow){
        et_search.setVisibility(isSearchShow ? VISIBLE : GONE);
    }

    public void hideSearchBar(){
        if (et_search != null){
            et_search.setVisibility(GONE);
        }
    }

    public void showTitle(){
        if (tv_title != null){
            tv_title.setVisibility(VISIBLE);
        }
    }

    public void hideTitle(){
        if (tv_title != null){
            tv_title.setVisibility(GONE);
        }
    }

    public void showRightButton(){
        if (btn_right != null){
            btn_right.setVisibility(VISIBLE);
        }
    }

    public void hideRightButton(){
        if (btn_right != null){
            btn_right.setVisibility(GONE);
        }
    }

    public void setRightBtnText(CharSequence text){
        if(btn_right != null){
            btn_right.setText(text);
            btn_right.setVisibility(VISIBLE);
            btn_right.setBackgroundDrawable(null);
        }
    }


    public Button getRightButton() {
        return btn_right;
    }

    public EditText getEditText(){
        return this.et_search;
    }
}
