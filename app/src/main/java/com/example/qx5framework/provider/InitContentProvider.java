package com.example.qx5framework.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qx5framework.util.X5Utils;

/**
 * 用于全局SDK初始化
 */
public class InitContentProvider extends ContentProvider {

    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreate() {
//        AndPermission.with(getContext())
//                .runtime()
//                .permission(Permission.READ_PHONE_STATE)
//                .permission(Permission.Group.STORAGE)
//                .onGranted(permissions -> {
//                      initSDK();
//                })
//                .onDenied(permissions -> {
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                })
//                .start();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private void initSDK() {
        X5Utils.initWebView(getContext());
    }
}
