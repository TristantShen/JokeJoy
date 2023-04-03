package com.tristan.jokejoy.http;

import java.io.Serializable;

/**
 * @author : create by  szh
 * @date : 2022/11/30 10:52
 * @desc :网络请求返回的基础数据类型
 */
public class ApiResponse<T> implements Serializable {

    private int code;
    private T data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
