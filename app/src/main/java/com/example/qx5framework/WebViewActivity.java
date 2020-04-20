package com.example.qx5framework;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qx5framework.config.Constants;
import com.example.qx5framework.view.X5WebView;

/**
 * 腾讯X5内核加载H5的壳
 */
public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = WebViewActivity.class.getSimpleName();

    private ViewGroup mViewParent;
    private X5WebView mWebView;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewParent = findViewById(R.id.fl_web);
        mWebView = new X5WebView(this);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        if (getIntent() == null) {
            Log.e(TAG, "Intent is null");
            return;
        }

        String urlPath = getIntent().getStringExtra(Constants.KEY_URL_PATH);
        if (!TextUtils.isEmpty(urlPath)) {
            if (urlPath.startsWith("/storage")) {
                urlPath = "file://" + urlPath;
            }
        }
        mWebView.loadUrl(urlPath);
    }
}