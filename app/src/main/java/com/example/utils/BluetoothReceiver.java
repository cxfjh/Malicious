package com.example.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class BluetoothReceiver extends BroadcastReceiver {
    // 蓝牙连接状态改变
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);
            if (state == BluetoothAdapter.STATE_CONNECTED) AudioUtils.setAudioOutputToSpeaker(context);
        } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
            if (state == BluetoothAdapter.STATE_ON) AudioUtils.setAudioOutputToSpeaker(context);
        };
    };


    // // 注册广播接收器
    public void register(final Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(this, filter);
    };
};