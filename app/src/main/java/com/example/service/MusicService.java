package com.example.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.example.malicious.R;


// 音乐前台服务
public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "music_service_channel"; // 通知渠道 ID


    // 初始化
    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.audio); // 加载音频资源
        mediaPlayer.setLooping(true); // 设置循环播放

        // Android 8.0 及以上版本的系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final int importance = NotificationManager.IMPORTANCE_DEFAULT;
            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "音乐服务通知渠道", importance);
            channel.setDescription("系统通知");
            final NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        };
    };


    // 开始播放
    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start(); // 播放音乐

        // 将服务提升为前台服务
        final Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("音乐服务")
                .setContentText("音乐正在播放")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .build();

        startForeground(2, notification); // 通知 ID 为 2
        return START_STICKY; // 确保服务在系统资源不足时被重启
    };


    // 释放资源
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        };
    };


    // 必须实现的方法
    @Override
    public IBinder onBind(Intent intent) { return null; };
};