package net.adsplay.scalablevideo.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.adsplay.common.AdsPlayCallback;
import net.adsplay.holder.AdsPlayImageBannerHolder;
import net.adsplay.holder.AdsPlayVideoInfeedHolder;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--------for mobile banner-----------------
        final int bannerPlacementId = 1008;
        AdsPlayCallback bannerCallback = new AdsPlayCallback() {
            @Override
            public void onFinished() {
                //TODO something when the ad is loaded fully
                Log.i("AdsPlay","------->  loaded PlacementId: "+bannerPlacementId);
            }
        };
        AdsPlayImageBannerHolder holderImage = (AdsPlayImageBannerHolder) findViewById(R.id.banner_view);
        holderImage.loadAdData(this, bannerPlacementId, bannerCallback);


        //for mobile infeed video
        final int infeedPlacementId = 1009;
        AdsPlayCallback infeedCallback = new AdsPlayCallback() {
            @Override
            public void onFinished() {
                //TODO do something when the ad is loaded fully
                Log.i("AdsPlay", "------->  loaded PlacementId: " + infeedPlacementId);
            }
        };
        //AdsPlayVideoInfeedHolder infeedHolder = (AdsPlayVideoInfeedHolder) findViewById(R.id.masterhead_view);
        //infeedHolder.loadAdData(this, infeedPlacementId,  infeedCallback);

        final WebView myWebView = (WebView) findViewById(R.id.masthead_webview);
        myWebView.setBackgroundColor(Color.TRANSPARENT);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);


//        myWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onConsoleMessage(ConsoleMessage cs) {
//                Log.i("AdsPlaySDK", cs.message() + " -- From line "
//                        + cs.lineNumber() + " of "
//                        + cs.sourceId());
//                return true;
//            }
//        });
        WebViewClient webViewClient= new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView  view, String  url){
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                Log.d("adsplay",url);
                myWebView.setVisibility(View.GONE);
                return true;
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                //myWebView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                myWebView.loadUrl("javascript:window.HtmlViewer.check" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        };

        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("Referer", "https://fptplay.net");
        myWebView.addJavascriptInterface(new MyJavaScriptInterface(myWebView), "HtmlViewer");
        myWebView.setWebViewClient(webViewClient);
        myWebView.loadUrl("http://st.adsplay.net/js/masthead/masthead.html",extraHeaders);
    }

    static final class MyJavaScriptInterface {

        private WebView myWebView;

        MyJavaScriptInterface(WebView myWebView) {
            this.myWebView = myWebView;
        }

        public void check(String html) {
            Log.d("adsplay",html);
            if(!html.contains("html")){
               // myWebView.setVisibility(View.GONE);
            }
        }

    }
}