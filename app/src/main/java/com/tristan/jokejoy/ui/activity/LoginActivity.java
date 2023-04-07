package com.tristan.jokejoy.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.hjq.bar.TitleBar;
import com.hjq.widget.view.SubmitButton;
import com.tristan.jokejoy.R;
import com.tristan.jokejoy.aop.DebugLog;
import com.tristan.jokejoy.app.AppActivity;
import com.tristan.jokejoy.manager.InputTextManager;
import com.tristan.jokejoy.other.IntentKey;
import com.tristan.jokejoy.other.KeyboardWatcher;

/**
 * @author : create by  szh
 * @date : 2023/3/31 15:46
 * @desc :
 */
public class LoginActivity extends AppActivity implements KeyboardWatcher.SoftKeyboardStateListener, TextView.OnEditorActionListener {

    private int mTranslationScale;

    @DebugLog
    public static void start(Context context, String phone, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(IntentKey.PHONE, phone);
        intent.putExtra(IntentKey.PASSWORD, password);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private ImageView mLogoView;

    private ViewGroup mBodyLayout;
    private EditText mPhoneView;
    private EditText mPasswordView;
    private EditText mVerifyCodeViewEt;
    private ShapeableImageView mVerifyCodeView;

    private View mForgetView;
    private SubmitButton mCommitView;

    /**
     * logo 缩放比例
     */
    private final float mLogoScale = 0.8f;
    /**
     * 动画时间
     */
    private final int mAnimTime = 300;

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initView() {
        mLogoView = findViewById(R.id.iv_login_logo);
        mBodyLayout = findViewById(R.id.ll_login_body);
        mPhoneView = findViewById(R.id.et_login_phone);
        mPasswordView = findViewById(R.id.et_login_password);
        mVerifyCodeViewEt = findViewById(R.id.et_login_verify_code);
        mVerifyCodeView = findViewById(R.id.siv_login_verify_code);
        mForgetView = findViewById(R.id.tv_login_forget);
        mCommitView = findViewById(R.id.btn_login_commit);


        setOnClickListener(mVerifyCodeView, mForgetView, mCommitView);

        mVerifyCodeViewEt.setOnEditorActionListener(this);

        InputTextManager.with(this)
                .addView(mPhoneView)
                .addView(mPasswordView)
                .addView(mVerifyCodeViewEt)
                .setMain(mCommitView)
                .build();
    }

    @Override
    protected void initData() {
        postDelayed(() -> KeyboardWatcher.with(LoginActivity.this)
                .setListener(LoginActivity.this), 500);
    }

    @Override
    public void onRightClick(TitleBar titleBar) {
        startActivity(RegisterActivity.class);
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        mTranslationScale = -mCommitView.getHeight();

        int[] location = new int[2];
        mCommitView.getLocationOnScreen(location);
        int viewBottom = location[1] + mCommitView.getHeight();
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int distanceToBottom = screenHeight - viewBottom;

        int moveScale = keyboardHeight - distanceToBottom;
        if (moveScale > mCommitView.getHeight()) {
            mTranslationScale = -moveScale;
        }

        //执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", 0, mTranslationScale);
        objectAnimator.setDuration(mAnimTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        //执行缩小动画
        mLogoView.setPivotX(mLogoView.getWidth() / 2f);
        mLogoView.setPivotY(mLogoView.getHeight());
        //动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", 1f, mLogoScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", 1f, mLogoScale);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", 0f, mTranslationScale);
        animatorSet.play(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(mAnimTime);
        animatorSet.start();
    }

    @Override
    public void onSoftKeyboardClosed() {

        mTranslationScale = -mCommitView.getHeight();
        // 执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", mBodyLayout.getTranslationY(), 0f);
        objectAnimator.setDuration(mAnimTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        if (mLogoView.getTranslationY() == 0) {
            return;
        }

        // 执行放大动画
        mLogoView.setPivotX(mLogoView.getWidth() / 2f);
        mLogoView.setPivotY(mLogoView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", mLogoScale, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", mLogoScale, 1f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", mLogoView.getTranslationY(), 0f);
        animatorSet.play(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(mAnimTime);
        animatorSet.start();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
