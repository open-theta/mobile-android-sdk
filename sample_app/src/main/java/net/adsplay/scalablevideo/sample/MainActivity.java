package net.adsplay.scalablevideo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.adsplay.common.AdsPlayCallback;
import net.adsplay.holder.AdsPlayImageBannerHolder;
import net.adsplay.holder.AdsPlayVideoInfeedHolder;


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
        AdsPlayVideoInfeedHolder infeedHolder = (AdsPlayVideoInfeedHolder) findViewById(R.id.masterhead_view);
        infeedHolder.loadAdData(this, infeedPlacementId,  infeedCallback);

        WebView myWebView = (WebView) findViewById(R.id.masthead_webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("http://richmedia.adsplay.net/masthead-mobile");
    }
}