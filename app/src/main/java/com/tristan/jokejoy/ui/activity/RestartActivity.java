package com.tristan.jokejoy.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tristan.jokejoy.MainActivity;
import com.tristan.jokejoy.R;
import com.tristan.jokejoy.app.AppActivity;
import com.tristan.jokejoy.app.AppApplication;


/**
 * @author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2020/11/29
 * desc   : 重启应用
 */
public final class RestartActivity extends AppActivity {

    public static void start() {
        Context context = AppApplication.getInstance();
        Intent intent = new Intent(context, RestartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        restart(this);
        finish();
        toast(R.string.common_crash_hint);
    }

    public static void restart(Context context) {
        Intent intent;
        if (true) {
            // 如果是未登录的情况下跳转到闪屏页
            intent = new Intent(context, MainActivity.class);
        } else {
            // 如果是已登录的情况下跳转到首页
            intent = new Intent(context, MainActivity.class);
        }

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}