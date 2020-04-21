package com.example.qx5framework.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯X5公共类
 */
public class X5Utils {
    private static final String TAG = X5Utils.class.getSimpleName();

    public static void initWebView(Context context) {
        if (context == null) {
            Log.e(TAG, "context is null");
            return;
        }

        hookSysWebView(context);

        //TBS内核首次加载 ”dex2oat优化方案“
        Map<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);

        //搜集本地tbs内核信息并上报服务器, 服务器返回结果决定使用哪个内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调, 为true表示x5内核加载成功, 否则表示x5内核加载失败, 会自动切换到系统内核
                Log.d(TAG, " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                Log.d(TAG, " onCoreInitFinished ");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context.getApplicationContext(), cb);

        //非wifi情况下, 主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //监控内核下载状态
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d(TAG, " onDownloadFinish ");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d(TAG, " onInstallFinish ");
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d(TAG, " onDownloadProgress is " + i);
            }
        });
    }

    /**
     * 解决系统进程中无法使用WebView的问题
     */
    private static void hookSysWebView(Context context) {
        if (!AppUtils.isSystemApp(context)) {
            Log.d(TAG, "It isn't sysApp");
            return;
        }

        try {
            Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
            Field field = factoryClass.getDeclaredField("sProviderInstance");
            field.setAccessible(true);
            Object sProviderInstance = field.get(null);
            if (sProviderInstance != null) {
                Log.d(TAG, "sProviderInstance isn't null");
                return;
            }

            Method getProviderClassMethod;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
            } else {
                Log.d(TAG, "Don't need to Hook WebView");
                return;
            }
            getProviderClassMethod.setAccessible(true);
            Class<?> factoryProviderClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
            Constructor<?> delegateConstructor = delegateClass.getDeclaredConstructor();
            delegateConstructor.setAccessible(true);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Constructor<?> providerConstructor =
                        factoryProviderClass.getConstructor(delegateClass);
                if (providerConstructor != null) {
                    providerConstructor.setAccessible(true);
                    sProviderInstance =
                            providerConstructor.newInstance(delegateConstructor.newInstance());
                }
            } else {
                Field chromiumMethodName = factoryClass.getDeclaredField(
                        "CHROMIUM_WEBVIEW_FACTORY_METHOD");
                chromiumMethodName.setAccessible(true);
                String chromiumMethodNameStr = (String) chromiumMethodName.get(null);
                if (chromiumMethodNameStr == null) {
                    chromiumMethodNameStr = "create";
                }
                Method staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr,
                        delegateClass);
                if (staticFactory != null) {
                    sProviderInstance = staticFactory.invoke(null,
                            delegateConstructor.newInstance());
                }
            }

            if (sProviderInstance != null) {
                field.set("sProviderInstance", sProviderInstance);
                Log.d(TAG, "Hook success!");
            } else {
                Log.d(TAG, "Hook failed!");
            }
        } catch (Throwable e) {
            Log.w(TAG, e);
        }
    }
}
