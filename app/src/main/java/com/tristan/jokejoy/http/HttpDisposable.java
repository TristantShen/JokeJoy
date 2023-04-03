package com.tristan.jokejoy.http;

import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * @author : create by  szh
 * @date : 2022/11/30 15:34
 * @desc :返回数据
 */
public abstract class HttpDisposable<T> extends DisposableObserver<T> {

    public HttpDisposable() {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(T value) {
        success(value);
    }

    @Override
    public void onError(Throwable e) {
        ToastUtils.showShort(e.getMessage());
        Timber.e(e);
        onFailure(e);
    }

    @Override
    public void onComplete() {

    }

    protected abstract void success(T value);

    protected abstract void onFailure(Throwable e);
}
