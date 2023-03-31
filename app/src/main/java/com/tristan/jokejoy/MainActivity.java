package com.tristan.jokejoy;


import android.widget.TextView;

import com.tristan.jokejoy.app.AppActivity;
import com.tristan.jokejoy.databinding.ActivityMainBinding;

import java.util.ArrayList;

/**
 * @author : create by  szh
 * @date : 2023/3/31 9:27
 * @desc :
 */
public class MainActivity extends AppActivity {

    ActivityMainBinding binding;
    private TextView textView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initObserver() {

    }

    // @Override
    // protected ViewDataBinding onBindingView() {
    //     if (getLayoutId() > 0) {
    //         binding = DataBindingUtil.setContentView(this, getLayoutId());
    //     }
    //     return binding;
    // }

    @Override
    protected boolean isStatusBarEnabled() {
        return super.isStatusBarEnabled();
    }

    @Override
    protected boolean isStatusBarDarkFont() {
        return !super.isStatusBarDarkFont();
    }
}