package com.cqfrozen.jsh.util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/1/3.
 */
public class CountDownAdTimer extends CountDownTimer {

    TextView text;
    Boolean isGoing = false;
    Boolean init = false;

    public CountDownAdTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        isGoing = true;
        if (null != text && !init) {
//            text.setText(millisUntilFinished / 1000 + "s后重新获取");
            text.setText("" + millisUntilFinished / 1000 + "s跳过");
            text.setEnabled(false);
        }
    }

    @Override
    public void onFinish() {
        isGoing = false;
        if (null != text) {
            text.setText("跳过");
            text.setEnabled(true);
        }
    }

    /**
     * 增加TextView
     *
     * @param text
     */
    public void setTextView(TextView text) {
        this.text = text;
    }

    /**
     * 启动倒计时
     */
    public void going() {
        if (!isGoing||init) {
            start();
        }
        init = false;
    }

    /**
     * 初始化 计数器返回0
     */
    public void setInit() {
        init = true;
        if (null != text) {
            text.setEnabled(true);
            text.setText("跳过");
        }
    }
}
