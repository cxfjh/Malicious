package com.example.malicious;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

import com.example.utils.AudioUtils;
import com.example.utils.RestrictionManager;
import com.example.utils.HeadsetReceiver;
import com.example.utils.BluetoothReceiver;


public class MainActivity extends AppCompatActivity  {
    private static final int REQUEST_CODE_OVERLAY = 1; // 悬浮窗权限请求码
    private final RestrictionManager restrictionManager = new RestrictionManager(this, Executors.newScheduledThreadPool(1));;
    private final HeadsetReceiver headsetReceiver = new HeadsetReceiver();
    private final BluetoothReceiver bluetoothReceiver = new BluetoothReceiver();
    public static boolean OperationalStatus = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 调用父类方法
        setContentView(R.layout.activity_main); // 设置布局文件
        setVolumeControlStream(AudioManager.STREAM_MUSIC); // 设置音量控制流为媒体流
        onGlobalLayout(); // 监听布局大小
    };


    // 注册广播接收器 onResume 是 Android 生命周期中的一个方法，当 Activity 进入前台时会被调用
    @Override
    protected void onResume() {
        super.onResume();
        if (!OperationalStatus) return;
        headsetReceiver.register(this);
        bluetoothReceiver.register(this);
    };


    // 监听布局大小
    private void onGlobalLayout() {
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            // 获取当前页面的宽度和高度
            final int width = rootView.getWidth();
            final int height = rootView.getHeight();

            // 获取屏幕的宽度和高度
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            final int screenWidth = displayMetrics.widthPixels;
            final int screenHeight = displayMetrics.heightPixels;

            // 判断是否处于小窗模式或悬浮窗模式
            if (width < screenWidth || height < screenHeight) finish(); // 强制退出应用
        });
    };


    // 禁止返回键
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {};


    // 处理 Switch 点击事件的方法
    public void onSwitchClicked(final View view) {
        // 检查悬浮窗权限
        if (!Settings.canDrawOverlays(this)) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY);
        } else {
            if (view.getId() == R.id.switch4) {
                Toast.makeText(this, "该功能暂未开放, 请选择其他功能", Toast.LENGTH_SHORT).show();
                return;
            };
            restrictionManager.startRestriction(); // 启用限制
            headsetReceiver.register(this); // 注册耳机广播接收器
            bluetoothReceiver.register(this); // 注册蓝牙广播接收器
            AudioUtils.setAudioOutputToSpeaker(this); // 设置音频输出到扬声器
            OperationalStatus = true; // 设置操作状态为 true
        };
    };


    // 处理悬浮窗权限请求结果
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY) {
            if (Settings.canDrawOverlays(this)) Toast.makeText(this, "悬浮窗权限已开启, 请重新点击", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "需要悬浮窗权限", Toast.LENGTH_SHORT).show();
            handleButton(); // 处理按钮
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