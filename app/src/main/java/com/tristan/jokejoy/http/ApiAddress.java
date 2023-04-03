package com.tristan.jokejoy.http;


import com.tristan.jokejoy.model.HomeBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * @author : create by  szh
 * @date : 2022/11/30 13:33
 * @desc :网络请求地址
 */
public interface ApiAddress {

    @POST("home/latest")
    Observable<List<HomeBean>> getHomeData();

}
