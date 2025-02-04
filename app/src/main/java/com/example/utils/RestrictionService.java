package com.example.utils;

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


// 后台服务
public class RestrictionService extends Service {
    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        // 创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Restriction Service", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        };

        // 显示通知
        final Notification notification = new NotificationCompat.Builder(this, "channel_id")
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
