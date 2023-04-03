package com.tristan.jokejoy.http.request;



import com.tristan.jokejoy.http.httptool.HttpInterceptor;
import com.tristan.jokejoy.http.httptool.ResponseConverterFactory;
import com.tristan.jokejoy.other.AppConfig;
import com.tristan.jokejoy.util.StringUtil;

import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import timber.log.Timber;

/**
 * @author : create by  szh
 * @date : 2022/11/30 11:21
 * @desc :网络请求
 */
public class HttpFactory {

    public static String HTTP_HOST_URL = AppConfig.getHostUrl();

    private static Retrofit retrofit = null;

    public static final HttpInterceptor INTERCEPTOR = new HttpInterceptor();

    private static final HttpLoggingInterceptor LOGGING_INTERCEPTOR = new HttpLoggingInterceptor(s ->
            Timber.d("===> result：%s", StringUtil.unicodeToString(s))).setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    //添加自定义拦截器
                    .addInterceptor(LOGGING_INTERCEPTOR)
                    .addInterceptor(INTERCEPTOR)
                    //设置超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();


    public static <T> T getInstance(Class<T> service) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(HTTP_HOST_URL)
                    .addConverterFactory(ResponseConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(HTTP_CLIENT)
                    .build();
        }
        return retrofit.create(service);
    }

    public static <T> T getChangeUrlInstance(String url, Class<T> service) {
        return new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(ResponseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(HTTP_CLIENT)
                .build()
                .create(service);
    }

    public static <T> ObservableTransformer<T, T> schedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
