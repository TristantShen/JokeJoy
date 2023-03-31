package com.tristan.jokejoy.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.ViewDataBinding;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.KeyboardUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseActivity;
import com.hjq.base.BaseDialog;
import com.hjq.http.listener.OnHttpListener;
import com.tristan.jokejoy.R;
import com.tristan.jokejoy.action.Init;
import com.tristan.jokejoy.action.TitleBarAction;
import com.tristan.jokejoy.action.ToastAction;
import com.tristan.jokejoy.ui.dialog.WaitDialog;


/**
 * @author : Android 轮子哥 & A Lonely Cat & Tristan
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 业务 Activity 基类
 */
public abstract class AppActivity extends BaseActivity
        implements Init, ToastAction, TitleBarAction {

    /**
     * 标题栏对象
     */
    private TitleBar mTitleBar;
    /**
     * 状态栏沉浸
     */
    private ImmersionBar mImmersionBar;

    /**
     * 加载对话框
     */
    private BaseDialog mDialog;
    /**
     * 对话框数量
     */
    private int mDialogTotal;

    /**
     * 初始化事件监听和观察者
     */
    @Override
    protected void initActivity() {
        super.initActivity();
        initEvent();
        initObserver();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initObserver() {

    }

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        mDialogTotal++;
        postDelayed(() -> {
            if (mDialogTotal <= 0 || isFinishing() || isDestroyed()) {
                return;
            }

            if (mDialog == null) {
                mDialog = new WaitDialog.Builder(this)
                        .setCancelable(false)
                        .create();
            }
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }, 300);
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        if (mDialogTotal > 0) {
            mDialogTotal--;
        }

        if (mDialogTotal == 0 && mDialog != null && mDialog.isShowing() && !isFinishing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        ViewDataBinding viewBinding = onBindingView();
        // 优先使用 DataBinding
        if (viewBinding != null) {
            setContentView(viewBinding.getRoot());
            KeyboardUtils.fixAndroidBug5497(this);
            initSoftKeyboard();
        }
        if (getTitleBar() != null) {
            getTitleBar().setOnTitleBarListener(this);
        }

        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init();

            // 设置标题栏沉浸
            if (getTitleBar() != null) {
                ImmersionBar.setTitleBar(this, getTitleBar());
            }
        }
    }


    /**
     * 如果使用 DataBinding 则复写此方法
     */
    protected ViewDataBinding onBindingView() {
        return null;
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return false;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    public ImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式状态栏
     */
    @NonNull
    protected ImmersionBar createStatusBarConfig() {
        return ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(isStatusBarDarkFont())
                // 指定导航栏背景颜色
                .navigationBarColor(android.R.color.white)
                // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
                .autoDarkModeEnable(true, 0.2f);
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(@StringRes int id) {
        setTitle(getString(id));
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (getTitleBar() != null) {
            getTitleBar().setTitle(title);
        }
    }

    @Override
    @Nullable
    public TitleBar getTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = obtainTitleBar(getContentView());
        }
        return mTitleBar;
    }

    @Override
    public void onLeftClick(TitleBar titleBar) {
        onBackPressed();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.right_in_activity, R.anim.right_out_activity);
    }

    @Override
    public void finish() {
        super.finish();
        hideKeyboard();
        overridePendingTransition(R.anim.left_in_activity, R.anim.left_out_activity);
    }

    @Override
    public void hideKeyboard(View view) {
        KeyboardUtils.hideSoftInput(this);
    }

    public void hideKeyboard() {
        KeyboardUtils.hideSoftInput(this);
    }

    /**
     * {@link OnHttpListener}
     */


    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        // if (!AppConfig.isDebug() && DeviceUtils.isDeviceRooted()) {
        //     toast("请勿在root设备使用本App");
        //     ActivityManager.getInstance().finishAllActivities();
        //     return;
        // }
        // if (!AppConfig.isDebug() && DeviceUtils.isEmulator()) {
        //     toast("请勿在模拟器上使用本App");
        //     ActivityManager.getInstance().finishAllActivities();
        //     return;
        // }
        //TODO:
        // FragmentActivityKt.checkToken(this, (result, continuation) -> null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isShowDialog()) {
            hideDialog();
        }
        mDialog = null;
        KeyboardUtils.fixSoftInputLeaks(this);
    }
}