package com.tristan.jokejoy.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.Configuration;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.RippleBarStyle;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.http.EasyConfig;
import com.hjq.toast.ToastLogInterceptor;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengClient;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;
import com.tristan.jokejoy.R;
import com.tristan.jokejoy.http.RequestHandler;
import com.tristan.jokejoy.http.RequestServer;
import com.tristan.jokejoy.manager.ActivityManager;
import com.tristan.jokejoy.manager.LocalCookieManager;
import com.tristan.jokejoy.other.AppConfig;
import com.tristan.jokejoy.other.CrashHandler;
import com.tristan.jokejoy.other.DebugLoggerTree;
import com.tristan.jokejoy.other.SmartBallPulseFooter;

import java.io.InputStream;

import com.tristan.jokejoy.db.CookieRoomDatabase;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * @author : Android 轮子哥 & A Lonely Cat
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 应用入口
 */
public class AppApplication extends Application implements Configuration.Provider {

    private static AppApplication INSTANCE;
    private static CookieRoomDatabase sDatabase;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        initSdk(this);
    }

    private void initSdk(AppApplication application) {
        // 本地异常捕捉
        CrashHandler.register(application);
        // MMKV初始化
        MMKV.initialize(application);

        // 初始化吐司
        ToastUtils.init(application);
        // 设置调试模式

        // 设置 Toast 拦截器
        ToastUtils.setInterceptor(new ToastLogInterceptor());

        // 设置标题栏初始化器
        TitleBar.setDefaultStyle(new RippleBarStyle() {
            @Override
            public Drawable getTitleBarBackground(Context context) {
                return ContextCompat.getDrawable(context, R.drawable.shape_gradient);
            }
        });

        // 友盟统计、登录、分享 SDK
        UmengClient.init(application, AppConfig.isLogEnable());

        // Bugly 异常捕捉
        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug());

        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) ->
                new MaterialHeader(context).setColorSchemeColors(ContextCompat.getColor(context, R.color.common_accent_color)));
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new SmartBallPulseFooter(context));
        // 设置全局初始化器
        SmartRefreshLayout.setDefaultRefreshInitializer((context, layout) -> {
            // 刷新头部是否跟随内容偏移
            layout.setEnableHeaderTranslationContent(true)
                    // 刷新尾部是否跟随内容偏移
                    .setEnableFooterTranslationContent(true)
                    // 加载更多是否跟随内容偏移
                    .setEnableFooterFollowWhenNoMoreData(true)
                    // 内容不满一页时是否可以上拉加载更多
                    .setEnableLoadMoreWhenContentNotFull(false)
                    // 仿苹果越界效果开关
                    .setEnableOverScrollDrag(false);
        });

        // Activity 栈管理初始化
        ActivityManager.getInstance().init(application);

        // 网络请求框架初始化
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(LocalCookieManager.get())
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                .setLogEnabled(AppConfig.isLogEnable())
                // 设置服务器配置
                .setServer(new RequestServer())
                // 设置请求处理策略
                .setHandler(new RequestHandler(application))
                // 设置请求重试次数
                .setRetryCount(1)
                // 添加全局请求参数
                //.addParam("token", "6666666")
                // 添加全局请求头
                //.addHeader("time", "20191030")
                // 启用配置
                .into();

        // 设置 Json 解析容错监听
        GsonFactory.setJsonCallback((typeToken, fieldName, jsonToken) -> {
            // 上报到 Bugly 错误列表
            CrashReport.postCatchedException(new IllegalArgumentException(
                    "类型解析异常：" + typeToken + "#" + fieldName + "，后台返回的类型为：" + jsonToken));
        });

        // 初始化日志打印
        if (AppConfig.isLogEnable()) {
            Timber.plant(new DebugLoggerTree());
        }

        // 注册网络状态变化监听
        ConnectivityManager connectivityManager = ContextCompat.getSystemService(application, ConnectivityManager.class);
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(@NonNull Network network) {
                    Activity topActivity = ActivityManager.getInstance().getTopActivity();
                    if (topActivity instanceof LifecycleOwner) {
                        LifecycleOwner lifecycleOwner = ((LifecycleOwner) topActivity);
                        if (lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                            ToastUtils.show(R.string.common_network_error);
                        }
                    }
                }
            });
            // 初始化 Room 数据库
            sDatabase = CookieRoomDatabase.getDatabase(application);
            // 初始化 Glide 的 Cookie 管理
            Glide.get(application)
                    .getRegistry()
                    .replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(castOrNull(okHttpClient)));
        }

    }

    private static Call.Factory castOrNull(OkHttpClient okHttpClient) {
        if (okHttpClient != null) {
            return (Call.Factory) okHttpClient;
        }
        return null;
    }

    public static AppApplication getInstance() {
        return INSTANCE;
    }

    public static CookieRoomDatabase getDatabase() {
        return sDatabase;
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return null;
    }
}
