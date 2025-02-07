package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import com.example.malicious.R;


// 音乐服务
public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    // 初始化
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        mediaPlayer.setLooping(true); // 设置循环播放
    };

    // 开始播放
    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d("MusicService", "音乐开始播放");
        };
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
            Log.d("MusicService", "音乐停止播放");
        };
    };


    // 必须实现的方法
    @Override
    public IBinder onBind(Intent intent) { return null; };
};