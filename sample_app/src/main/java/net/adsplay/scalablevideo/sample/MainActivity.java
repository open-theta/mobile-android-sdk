package net.adsplay.scalablevideo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.adsplay.common.AdsPlayCallback;
import net.adsplay.holder.AdsPlayHolderImage;
import net.adsplay.holder.AdsPlayHolderVideo;


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
        AdsPlayHolderImage holderImage = (AdsPlayHolderImage) findViewById(R.id.banner_view);
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
        AdsPlayHolderVideo holderVideo = (AdsPlayHolderVideo) findViewById(R.id.masterhead_view);
        holderVideo.loadAdData(this, infeedPlacementId,  infeedCallback);
    }
}