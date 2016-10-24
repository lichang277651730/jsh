package com.cqfrozen.jsh.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqfrozen.jsh.R;


/**
 * Created by <a href="http://www.cniao5.com">菜鸟窝</a>
 * 一个专业的Android开发在线教育平台
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    private TextView tv_num;
    private Button btn_add;
    private Button btn_sub;

    private OnSubAddClickListener listener;
    private LayoutInflater mInflater;

    public static final int DEFUALT_MAX=1000;
    private int curValue;
    private int minValue;
    private int maxValue = DEFUALT_MAX;



    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        initView();
        if(attrs != null){
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.NumberAddSubView, defStyleAttr, 0);
            int curVal =  a.getInt(R.styleable.NumberAddSubView_curValue,0);
            setCurValue(curVal);
            int minVal = a.getInt(R.styleable.NumberAddSubView_minValue,0);
            setMinValue(minVal);
            int maxVal = a.getInt(R.styleable.NumberAddSubView_maxValue,0);
            if(maxVal!=0){
                setMaxValue(maxVal);
            }

            Drawable numBg = a.getDrawable(R.styleable.NumberAddSubView_numBackground);
            if(numBg!=null){
                setNumBg(numBg);
            }

             Drawable addBg = a.getDrawable(R.styleable.NumberAddSubView_addBackgroud);
             if(addBg!=null){
                 setAddBg(addBg);
             }

            Drawable subBg = a.getDrawable(R.styleable.NumberAddSubView_subBackgroud);
            if(subBg!=null){
                setSubBg(subBg);
            }
            a.recycle();
        }
    }


    private void initView(){
        View view = mInflater.inflate(R.layout.widet_num_add_sub,this,true);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_num.setInputType(InputType.TYPE_NULL);
        tv_num.setKeyListener(null);

        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_sub = (Button) view.findViewById(R.id.btn_sub);

        btn_add.setOnClickListener(this);
        btn_sub.setOnClickListener(this);
    }

    /**
     * 设置当前数量
     */
    public void setCurValue(int curValue) {
        this.curValue = curValue;
        tv_num.setText(curValue + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add://点击+按钮
                addNum();
                break;
            case R.id.btn_sub://点击-按钮
                subNum();
                break;
            default:
                break;
        }
        if(listener != null){
            listener.onSubAddClick(v, curValue);
        }
    }
    /**
     * 增加1个数
     */
    private void addNum() {
        if(!btn_sub.isEnabled()){
            btn_sub.setEnabled(true);
        }
        getCurValue();
        if(curValue < maxValue){
            curValue = curValue + 1;
        }
        tv_num.setText(curValue + "");
    }

    /**
     * 减少1个数
     */
    private void subNum() {
        getCurValue();
        if(curValue == 1){
            btn_sub.setEnabled(false);
        }
        if(curValue > minValue){
            curValue = curValue - 1;
        }else {
//            ToastUtil.showToast(getContext(), "数量不能再少了哦,亲！~");
            btn_sub.setEnabled(false);
        }
        tv_num.setText(curValue + "");
    }

    /**
     * 获取当前数量
     */
    public int getCurValue(){
        String curValStr = tv_num.getText().toString();
        if(!TextUtils.isEmpty(curValStr)){
            this.curValue = Integer.parseInt(curValStr);
        }
        return this.curValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
//        tv_num.setText(minValue);
        this.minValue = minValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setNumBg(Drawable numBg){
        tv_num.setBackgroundDrawable(numBg);
    }

    public void setNumBg(int drawableId){
        setNumBg(getResources().getDrawable(drawableId));
    }

    public void setAddBg(Drawable addBg){
        this.btn_add.setBackgroundDrawable(addBg);
    }

    public void setSubBg(Drawable subBg){
        this.btn_sub.setBackgroundDrawable(subBg);
    }

    public void setOnSubAddClickListener(OnSubAddClickListener listener){
        this.listener = listener;
    }

    public interface OnSubAddClickListener{
        void onSubAddClick(View view, int curVal);
    }

}
