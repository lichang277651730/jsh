package com.cqfrozen.jsh.util;

import android.os.CountDownTimer;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/17.
 */
public class ClickableTimer extends CountDownTimer {

    ImageView iv_click;
    Boolean isGoing = false;
    Boolean init = false;

    public ClickableTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        isGoing = true;
        if (null != iv_click && !init) {
            iv_click.setEnabled(false);
        }
    }

    @Override
    public void onFinish() {
        isGoing = false;
        if (null != iv_click) {
            iv_click.setEnabled(true);
        }
    }

    /**
     * 增加ImageView
     */
    public void setImageView(ImageView iv_click) {
        this.iv_click = iv_click;
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
        if (null != iv_click) {
            iv_click.setEnabled(true);
        }
    }
}
