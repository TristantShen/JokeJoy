package com.tristan.jokejoy;


import android.widget.TextView;

import com.google.gson.Gson;
import com.tristan.jokejoy.app.AppActivity;
import com.tristan.jokejoy.databinding.ActivityMainBinding;
import com.tristan.jokejoy.http.HttpDisposable;
import com.tristan.jokejoy.http.request.HttpFactory;
import com.tristan.jokejoy.http.request.HttpRequest;
import com.tristan.jokejoy.model.HomeBean;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

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
        HttpRequest.getInstance()
                .getHomeData()
                .compose(HttpFactory.schedulers())
                .subscribe(new HttpDisposable<List<HomeBean>>() {
                    @Override
                    protected void success(List<HomeBean> value) {
                        Timber.d(new Gson().toJson(value));
                    }

                    @Override
                    protected void onFailure(Throwable e) {

                    }
                });
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