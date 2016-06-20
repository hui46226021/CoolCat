package com.zshgif.laugh.utils;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * button计时器
 * Created by dxkj on 2015/10/6.
 */
public class TimeCount extends CountDownTimer {

    private Button btn;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCount(long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;

    }

    /**
     * 计时过程显示
     * @param millisUntilFinished
     */
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);
//        btn.setBackgroundResource(R.drawable.registbuts);
        btn.setText("重新获取("+millisUntilFinished /1000 + "秒)");
    }

    /**
     * 计时完毕触发
     */
    @Override
    public void onFinish() {
//        btn.setBackgroundResource(R.drawable.registbut);
        btn.setText("重新获取");
        btn.setClickable(true);
    }
}
