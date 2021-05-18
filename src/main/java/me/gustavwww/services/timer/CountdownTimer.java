package me.gustavwww.services.timer;

import java.util.TimerTask;

public class CountdownTimer extends TimerTask {

    private int counter;
    private TimerCallback callback;
    private TimerCallbackCounter periodCallback = null;

    public CountdownTimer(int seconds, TimerCallback callback, TimerCallbackCounter periodCallback) {
        counter = seconds;
        this.callback = callback;
        this.periodCallback = periodCallback;
    }

    public CountdownTimer(int seconds, TimerCallback callback) {
        counter = seconds;
        this.callback = callback;
    }

    @Override
    public void run() {

        counter--;
        if (counter <= 0) {
            callback.callback();
            return;
        }

        if (periodCallback != null) {
            periodCallback.callback(counter);
        }
    }

}
