package com.example.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class HeadsetReceiver extends BroadcastReceiver {
    // 耳机插入/拔出广播接收器
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
            final int state = intent.getIntExtra("state", -1);
            if (state == 1) AudioUtils.setAudioOutputToSpeaker(context); // 耳机插入
            else if (state == 0) AudioUtils.setAudioOutputToSpeaker(context); // 耳机拔出
        };
    };


    // 注册耳机插入/拔出广播接收器
    public void register(final Context context) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        context.registerReceiver(this, filter);
    };
};