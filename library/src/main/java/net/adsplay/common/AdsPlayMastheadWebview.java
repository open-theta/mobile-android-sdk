package net.adsplay.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by trieunt on 4/8/17.
 */

public final class AdsPlayMastheadWebview {
    public static final int ADTYPE_MASTHEAD = 11;//masthead for mobile app
    public static final int PLACEMENT_MASTHEAD_ANDROID = 313;//masthead for mobile app
    public static final String TEXT_HTML = "text/html";
    public static final String UTF_8 = "UTF-8";

    final static String USER_AGENT = System.getProperty("http.agent");
    final static String BASE_URL = "http://d4.adsplay.net/get?";
    final static String REFERER_URL = "https://fptplay.net/";
    final static String SOURCE = "AdsPlaySDK.v2";
    final static OkHttpClient httpClient = new OkHttpClient();

    final WebView myWebView;
    final Activity activity;
    final String uuid;

    public AdsPlayMastheadWebview(Activity activity,WebView webView){
        this.activity = activity;
        this.myWebView = webView;
        this.uuid = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        init();
    }

    public void init(){
        myWebView.setWebViewClient(webViewClient);


        WebSettings wSettings = myWebView.getSettings();
        Context applicationContext = activity.getApplicationContext();


        wSettings.setAppCachePath( applicationContext.getCacheDir().getAbsolutePath() );
        wSettings.setAllowFileAccess( true );
        wSettings.setAppCacheEnabled( true );
        wSettings.setJavaScriptEnabled( true );
        wSettings.setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default
        if ( !isNetworkAvailable() ) { // loading offline
            wSettings.setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        }
        wSettings.setAllowContentAccess( true );
        wSettings.setDatabaseEnabled( true );
        wSettings.setDomStorageEnabled( true );
        wSettings.setGeolocationEnabled( true );
        wSettings.setUserAgentString(USER_AGENT);

        myWebView.setBackgroundColor(Color.TRANSPARENT);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        MastheadWebviewNative webviewNative = new MastheadWebviewNative(applicationContext);
        myWebView.addJavascriptInterface(webviewNative,"MastheadWebviewNative");

        Request request = new Request.Builder().url(buildAdRequestUrl()).addHeader("User-Agent",USER_AGENT).build();

        httpClient.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(activity.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        closeAd();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String html = response.body().string();
                                if (html.contains("html")) {
                                    myWebView.loadDataWithBaseURL(BASE_URL,html,TEXT_HTML, UTF_8,REFERER_URL);
                                } else {
                                    myWebView.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                Log.e("AdsPlay",e.toString());
                                closeAd();
                            }
                        }
                    });
                }
            }
        });
    }

    String buildAdRequestUrl(){
        long t = System.currentTimeMillis()/1000L;
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("time=").append(t);
        url.append("&uuid=").append(uuid);
        url.append("&source=").append(SOURCE);
        url.append("&adtype=").append(ADTYPE_MASTHEAD);
        url.append("&placement=").append(PLACEMENT_MASTHEAD_ANDROID);
        Log.d("AdsPlay.WebViewClient","url "+url);
        return url.toString();
    }

    WebViewClient webViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String  url){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(i);
            Log.d("AdsPlay.WebViewClient",url);
            closeAd();
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            closeAd();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d("AdsPlay.WebViewClient","onLoadResource.url "+url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class MastheadWebviewNative {
        Context mContext;

        /** Instantiate the interface and set the context */
        public MastheadWebviewNative(Context c) {
            mContext = c;
        }

        /** close ad */
        @JavascriptInterface
        public void closeAdView() {
            closeAd();
        }
    }

    private void closeAd() {
        myWebView.setVisibility(View.GONE);
        myWebView.invalidate();
        myWebView.destroy();
    }
}