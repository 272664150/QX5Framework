package com.example.qx5framework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qx5framework.config.Constants;
import com.example.qx5framework.util.X5Utils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_show).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WebViewActivity.class);
            intent.putExtra(Constants.KEY_URL_PATH, "https://x5.tencent.com");
            startActivity(intent);
        });

        AndPermission.with(this).runtime()
                .permission(Permission.READ_PHONE_STATE)
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    X5Utils.initWebView(MainActivity.this);
                })
                .onDenied(permissions -> {
                    android.os.Process.killProcess(android.os.Process.myPid());
                })
                .start();
    }
}
