package com.tristan.jokejoy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;

import timber.log.Timber;

/**
 * @author : create by  szh
 * @date : 2023/4/7 9:24
 * @desc :监听倒计时服务
 */
public class CountdownService extends Service {

    private final IBinder mIBinder = new MyBinder();
    private CountDownTimer mCountDownTimer;
    private int mTimeLeftInMillis;

    public class MyBinder extends Binder {
        public CountdownService getService() {
            return CountdownService.this;
        }

        public int getSeconds() {
            return mTimeLeftInMillis;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int seconds = intent.getIntExtra("second", 60);
        mCountDownTimer = new CountDownTimer(seconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = (int) (millisUntilFinished / 1000);
                MMKV.defaultMMKV().putInt("second", mTimeLeftInMillis);
            }

            @Override
            public void onFinish() {
                // 倒计时器完成时执行某些操作
                MMKV.defaultMMKV().putInt("second", 0);
                stopSelf();
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}
