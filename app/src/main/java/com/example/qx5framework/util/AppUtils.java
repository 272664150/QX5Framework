package com.example.qx5framework.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    public static boolean isSystemApp(Context context) {
        if (context == null) {
            Log.e(TAG, "context is null");
            return false;
        }
        return isSystemApp(context, context.getPackageName());
    }

    public static boolean isSystemApp(Context context, String pkgName) {
        if (context == null) {
            Log.e(TAG, "context is null");
            return false;
        }
        if (!isValidPkg(pkgName)) {
            Log.e(TAG, "pkgName is invalid");
            return false;
        }

        boolean isSystemApp = false;

        PackageInfo packageInfo = null;
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            packageInfo = packageManager.getPackageInfo(pkgName, 0);
        } catch (Throwable t) {
            Log.w(TAG, t.getMessage(), t);
        } finally {
            // 是系统中已安装的应用
            if (packageInfo != null) {
                boolean isSysApp = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
                boolean isSysUpd = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1;
                isSystemApp = isSysApp || isSysUpd;
            }
        }
        return isSystemApp;
    }

    public static boolean isValidPkg(String pkgName) {
        if (!TextUtils.isEmpty(pkgName)) {
            // Java/Android合法包名，可以包含大写字母、小写字母、数字和下划线，用点(英文句号)分隔称为段，且至少包含2个段，隔开的每一段都必须以字母开头
            Pattern pattern = Pattern.compile("^([a-zA-Z_][a-zA-Z0-9_]*)+([.][a-zA-Z_][a-zA-Z0-9_]*)+$");
            Matcher matcher = pattern.matcher(pkgName);
            return matcher.matches();
        }
        return false;
    }
}
