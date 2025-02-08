package com.example.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.example.malicious.R;


// 前台服务
public class RestrictionService extends Service {
    private static final String CHANNEL_ID = "RestrictionService";


    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final int importance = NotificationManager.IMPORTANCE_DEFAULT;
            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "服务通知渠道", importance);
            channel.setDescription("系统通知");
            final NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        };
    };


    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        // 显示通知
        final Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("系统通知")
                .setContentText("正在运行中")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .build();

        startForeground(1, notification);
        return START_STICKY;
    };


    @Override
    public IBinder onBind(final Intent intent) { return null; };
};
