package com.tristan.jokejoy.http.httptool;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tristan.jokejoy.http.ApiResponse;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : create by  szh
 * @date : 2022/8/17 9:26
 * @desc :数据转换
 */
public class ResponseConverterFactory extends Converter.Factory {

    private final Gson mGson;

    public ResponseConverterFactory(Gson gson) {
        this.mGson = gson;
    }

    public static ResponseConverterFactory create() {
        return create(new Gson());
    }

    public static ResponseConverterFactory create(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        return new ResponseConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new BaseResponseBodyConverter(type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return GsonConverterFactory.create().requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    private class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        private final Type mType;

        private BaseResponseBodyConverter(Type mType) {
            this.mType = mType;
        }

        @Override
        public T convert(@NonNull ResponseBody response) {
            T object = null;
            try {
                String strResponse = response.string();
                if (TextUtils.isEmpty(strResponse)) {
                    throw new HttpException("返回值是空的—-—");
                }
                ApiResponse apiResponse = mGson.fromJson(strResponse, ApiResponse.class);
                if (apiResponse.getCode() == 0 || apiResponse.getCode() == 200) {
                    object = mGson.fromJson(new Gson().toJson(apiResponse.getData()), mType);
                } else {
                    throw new HttpException(apiResponse.getCode(), apiResponse.getMsg());
                }
            } catch (Exception e) {
                throw new HttpException(e.getMessage());
            } finally {
                response.close();
            }
            return object;
        }
    }
}