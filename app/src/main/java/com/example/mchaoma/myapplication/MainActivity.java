package com.example.mchaoma.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String LOG_TAG = "WebViewDemo";

    private WebView mWebView;
    private Handler mHandler = new Handler();

/**
 * 获取网页cookie
 * /
    public String getCookie(Context context) {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie("cookie");
        if (cookie != null) {
            return cookie;
        } else {
            cookie = "XXX";
            cookieManager.setCookie("cookie", cookie);
            return cookie;
        }
    }

    @Override
    public void onCreate(Bundle icircle) {
        super.onCreate(icircle);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.web);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        WebView.setWebContentsDebuggingEnabled(true);

        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());

        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");

//        getCookie(this);
        mWebView.loadUrl("file:///android_asset/mc.html");
    }

    final class DemoJavaScriptInterface {
        @JavascriptInterface
        public void loadBaidu() throws ClassNotFoundException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("http://www.baidu.com");
                }
            });
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public int clickOnAndroid() {
            mHandler.post(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, "Js call Android", Toast.LENGTH_SHORT).show();
                    mWebView.loadUrl("javascript:wave()");
                }
            });
            return 100;
        }
    }

    final class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast.makeText(MainActivity.this, "loading", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }
    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                  JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b2 = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Test").setMessage(message)
                    .setPositiveButton("ok",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    result.confirm();
                                    // MyWebView.this.finish();
                                }
                            });

            b2.setCancelable(false);
            b2.create();
            b2.show();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }
    }

    /**
     * 拦截返回按键
     *
     * @param keyCode 按键信息
     * @param event   触摸事件
     * @return 是否消费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
