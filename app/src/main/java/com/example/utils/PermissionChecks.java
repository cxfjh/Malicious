package com.example.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;


// 权限检查工具类
public class PermissionChecks {
    // 检查是否有悬浮窗权限
    public static boolean hasOverlayPermission(final Context context) { return !Settings.canDrawOverlays(context); };


    // 请求悬浮窗权限
    public static Intent getOverlayPermissionIntent(Context context) {
        if (hasOverlayPermission(context)) {
            try { return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName())); }
            catch (Exception e) { Toast.makeText(context, "该手机不支持悬浮窗权限", Toast.LENGTH_SHORT).show(); }
        };
        return null;
    };
};