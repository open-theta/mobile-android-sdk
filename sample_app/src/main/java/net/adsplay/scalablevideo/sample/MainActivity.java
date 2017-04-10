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
import net.adsplay.common.AdsPlayMastheadWebview;
import net.adsplay.holder.AdsPlayImageBannerHolder;
import net.adsplay.holder.AdsPlayVideoInfeedHolder;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    WebView mastheadAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mastheadAdview = (WebView) findViewById(R.id.masthead_adview);
        new AdsPlayMastheadWebview(this,mastheadAdview);

        //--------for mobile banner-----------------
        /*final int bannerPlacementId = 1008;
        AdsPlayCallback bannerCallback = new AdsPlayCallback() {
            @Override
            public void onFinished() {
                //TODO something when the ad is loaded fully
                Log.i("AdsPlay","------->  loaded PlacementId: "+bannerPlacementId);
            }
        };*/
        //AdsPlayImageBannerHolder holderImage = (AdsPlayImageBannerHolder) findViewById(R.id.banner_view);
        //holderImage.loadAdData(this, bannerPlacementId, bannerCallback);


        //for mobile infeed video
       /* final int infeedPlacementId = 1009;
        AdsPlayCallback infeedCallback = new AdsPlayCallback() {
            @Override
            public void onFinished() {
                //TODO do something when the ad is loaded fully
                Log.i("AdsPlay", "------->  loaded PlacementId: " + infeedPlacementId);
            }
        };*/
        //AdsPlayVideoInfeedHolder infeedHolder = (AdsPlayVideoInfeedHolder) findViewById(R.id.masterhead_view);
        //infeedHolder.loadAdData(this, infeedPlacementId,  infeedCallback);


    }

    @Override
    public void onResume() {
        super.onResume();
        mastheadAdview.onResume();
    }



}