package com.example.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.example.malicious.MainActivity;


// 应用程序类
public class LifecycleManager extends Application {
    private int countActivity = 0; // 用于记录处于活动状态的 Activity 数量
    private boolean isBackground = false; // 标记应用是否处于后台
    private final Context context = this; // 上下文对象


    // 注册监听应用进入后台和回到前台的回调方法
    @Override
    public void onCreate() {
        super.onCreate();
        initBackgroundCallBack();
    };


    // 监听应用进入后台和回到前台
    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle bundle) {};
            @Override
            public void onActivityResumed(@NonNull Activity activity) {};
            @Override
            public void onActivityPaused(@NonNull Activity activity) {};
            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {}
            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {}

            // 处理前台
            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                countActivity++;
                if (countActivity == 1 && isBackground) isBackground = false;
            };

            // 处理后台
            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                countActivity--;
                if (countActivity <= 0 && !isBackground) {
                    isBackground = true;
                    if (Settings.canDrawOverlays(context) && MainActivity.OperationalStatus) openApp(); // 检查悬浮窗权限并打开应用
                };
            };
        });
    };


    // 打开应用
    private void openApp() {
        final Intent intent = new Intent(this, MainActivity.class); // 创建一个 Intent 对象，指定要打开的应用
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 添加标志，确保应用在启动时不会保留任何任务栈
        startActivity(intent); // 启动应用
    };
};