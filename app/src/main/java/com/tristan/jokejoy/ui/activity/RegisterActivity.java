package com.tristan.jokejoy.ui.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.tencent.mmkv.MMKV;
import com.tristan.jokejoy.R;
import com.tristan.jokejoy.aop.SingleClick;
import com.tristan.jokejoy.app.AppActivity;
import com.tristan.jokejoy.databinding.RegisterActivityBinding;
import com.tristan.jokejoy.service.CountdownService;


/**
 * @author : create by  szh
 * @date : 2023/4/6 15:36
 * @desc :注册界面
 */
public class RegisterActivity extends AppActivity implements TextView.OnEditorActionListener {

    RegisterActivityBinding binding;
    private int seconds;

    @Override
    protected int getLayoutId() {
        return R.layout.register_activity;
    }

    @Override
    protected ViewDataBinding onBindingView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        return binding;
    }

    @Override
    protected void initView() {
        int second = MMKV.defaultMMKV().getInt("second", 60);
        if (second > 0) {
            binding.cvRegisterCountdown.start(second);
        }
        setOnClickListener(binding.cvRegisterCountdown);
    }

    @Override
    protected void initData() {

    }

    @Override
    @SingleClick
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_register_countdown:
                if (TextUtils.isEmpty(binding.etRegisterPhone.getText()) || binding.etRegisterPhone.getText().length() != 11) {
                    binding.etRegisterPhone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    toast(R.string.common_phone_input_hint);
                    return;
                }
                binding.cvRegisterCountdown.start();
                break;
            case R.id.btn_register_commit:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    protected boolean isStatusBarDarkFont() {
        return true;
    }

    private CountdownService mService;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountdownService.MyBinder binder = (CountdownService.MyBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int currentSecond = binding.cvRegisterCountdown.getCurrentSecond();
        //页面销毁时，如果倒计时大于0 则开启倒计时服务
        if (currentSecond > 0) {
            Intent intent = new Intent(RegisterActivity.this, CountdownService.class);
            intent.putExtra("second", currentSecond);
            startService(intent);
        }
    }
}
