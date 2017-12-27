package com.gaelanbolger.woltile.view;

import android.os.Handler;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public abstract class OnDoubleClickListener implements View.OnClickListener {

    private static final int DELAY = 300;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;

    private long lastClickTime = 0;
    private Timer timer = null;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            processDoubleClickEvent(v);
        } else {
            processSingleClickEvent(v);
        }
        lastClickTime = clickTime;
    }


    private void processSingleClickEvent(final View v) {
        final Handler handler = new Handler();
        final Runnable mRunnable = () -> onSingleClick(v);
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(mRunnable);
            }
        };
        timer = new Timer();
        timer.schedule(timertask, DELAY);
    }


    private void processDoubleClickEvent(View v) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        onDoubleClick(v);
    }

    public abstract void onSingleClick(View v);

    public abstract void onDoubleClick(View v);
}