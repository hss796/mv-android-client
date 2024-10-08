/*
 * 版权所有 (c) 2017-2019 Altimit 社区贡献者
 *
 * 根据 Apache 许可证 2.0 版（“许可证”）获得许可；
 * 除非遵守许可证，否则您不得使用此文件。
 * 您可以在以下位置获取许可证副本：
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * 除非适用法律要求或书面同意，否则软件
 * 根据许可证分发是在“按原样”基础上分发的，
 * 不提供任何明示或暗示的保证或条件
 * 请参阅许可证以了解特定语言的管理权限和
 * 许可证下的限制。
 */
package jp.hbox.ImoutoFantasy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 由 felixjones 于 2017 年 4 月 28 日创建。
 *
 * @noinspection ALL
 */
public class WebPlayerView extends WebView {

    private WebPlayer mPlayer;

    public WebPlayerView(Context context) {
        super(context);
        init(context);
    }

    public WebPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WebPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mPlayer = new WebPlayer(this);

        setBackgroundColor(Color.BLACK);

        enableJavascript();

        WebSettings webSettings = getSettings();
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webSettings.setMediaPlaybackRequiresUserGesture(false);

        setWebChromeClient(new ChromeClient());
        setWebViewClient(new ViewClient());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void enableJavascript() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return false;
    }

    @Override
    public void scrollTo(int x, int y) {
    }

    @Override
    public void computeScroll() {
    }

    public Player getPlayer() {
        return mPlayer;
    }

    /**
     *
     */
    private class ChromeClient extends WebChromeClient {

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if ("Scripts may close only the windows that were opened by it.".equals(consoleMessage.message())) {
                if (mPlayer.getContext() instanceof WebPlayerActivity) {
                    ((WebPlayerActivity) mPlayer.getContext()).finish();
                }
            }
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView dumbWV = new WebView(view.getContext());
            dumbWV.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(browserIntent);
                }
            });
            ((WebViewTransport) resultMsg.obj).setWebView(dumbWV);
            resultMsg.sendToTarget();
            return true;
        }

    }

    /**
     *
     */
    private static class ViewClient extends WebViewClient {

        @Override
        @TargetApi(Build.VERSION_CODES.M)
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            view.setBackgroundColor(Color.WHITE);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.setBackgroundColor(Color.WHITE);
        }

    }

    /**
     *
     */
    private static final class WebPlayer implements Player {

        private final WebPlayerView mWebView;

        private WebPlayer(WebPlayerView webView) {
            mWebView = webView;
        }

        @Override
        public void setKeepScreenOn() {
            mWebView.setKeepScreenOn(true);
        }

        @Override
        public View getView() {
            return mWebView;
        }

        @Override
        public void loadUrl(String url) {
            mWebView.loadUrl(url);
        }

        @Override
        @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
        public void addJavascriptInterface(Object object, String name) {
            mWebView.addJavascriptInterface(object, name);
        }

        @Override
        public Context getContext() {
            return mWebView.getContext();
        }

        @Override
        public void loadData(String data) {
            mWebView.loadData(data, "text/html", "base64");
        }

        @Override
        public void evaluateJavascript(String script) {
            mWebView.evaluateJavascript(script, null);
        }

        @Override
        public void post(Runnable runnable) {
            mWebView.post(runnable);
        }

        @Override
        public void removeJavascriptInterface(String name) {
            mWebView.removeJavascriptInterface(name);
        }

        @Override
        public void pauseTimers() {
            mWebView.pauseTimers();
        }

        @Override
        public void onHide() {
            mWebView.onPause();
        }

        @Override
        public void resumeTimers() {
            mWebView.resumeTimers();
        }

        @Override
        public void onShow() {
            mWebView.onResume();
        }

        @Override
        public void onDestroy() {
            mWebView.destroy();
        }

    }

}
