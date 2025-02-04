package com.example.utils;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;


public class AudioUtils {
    // 设置音频输出为扬声器
    public static void setAudioOutputToSpeaker(final Context context) {
        if (!Settings.canDrawOverlays(context)) return; // 检查悬浮窗权限
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            audioManager.setSpeakerphoneOn(true);
        };
    };
};