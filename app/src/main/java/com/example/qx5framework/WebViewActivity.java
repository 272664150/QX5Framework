package com.example.qx5framework;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qx5framework.config.Constants;
import com.example.qx5framework.view.X5WebView;

/**
 * 腾讯X5加载H5的壳
 */
public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = WebViewActivity.class.getSimpleName();

    private ViewGroup mContainerFl;
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

        mContainerFl = findViewById(R.id.fl_container);
        mWebView = new X5WebView(this);
        mContainerFl.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, "intent is null");
            return;
        }

        String urlPath = intent.getStringExtra(Constants.KEY_URL_PATH);
        if (!TextUtils.isEmpty(urlPath)) {
            if (urlPath.startsWith("/storage")) {
                urlPath = "file://" + urlPath;
            }
        }
        mWebView.loadUrl(urlPath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            if (mContainerFl != null) {
                mContainerFl.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }
}