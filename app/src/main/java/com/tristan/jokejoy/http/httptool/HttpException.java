package com.tristan.jokejoy.http.httptool;


import android.text.TextUtils;

import androidx.annotation.Nullable;

/**
 * 自定义异常抛出
 *
 */
public class HttpException extends RuntimeException{

    private int code;
    private String message;

    public HttpException(String message) {
        this.message = message;
    }

    public HttpException(int code, String message) {
        this.message = message;
        this.code = code;
    }

    @Nullable
    @Override
    public String getMessage() {
        return TextUtils.isEmpty(message) ? "" : message;
    }
}
