package net.adsplay.holder;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.adsplay.common.AdData;
import net.adsplay.common.AdLogDataUtil;
import net.adsplay.common.AdPermissionChecker;
import net.adsplay.common.AdsPlayAdComponent;
import net.adsplay.common.AdsPlayCallback;
import net.adsplay.common.AsyncVideoAdLoadTask;

/**
 * Created by trieu on 10/30/16.
 */
public class AdsPlayVideoInfeedHolder extends RelativeLayout implements AdsPlayAdComponent {

    Activity activity;
    private LinearLayout adHolder;
    private ScalableVideoView videoView;
    AdsPlayCallback callback;

    public AdsPlayVideoInfeedHolder(Context context) {
        super(context);
        init();
    }

    public AdsPlayVideoInfeedHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdsPlayVideoInfeedHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ScalableVideoView getVideoView() {
        return this.videoView;
    }

    boolean volumeEnabled = false;
    int width, height;


    private void init() {
        inflate(getContext(), R.layout.holderinfeed, this);
        this.videoView = (ScalableVideoView) findViewById(R.id.masterheadVideoView);
        this.adHolder = (LinearLayout)findViewById(R.id.masterheadHolder);
        this.videoView.setVisibility(GONE);
    }


    @Override
    public void loadAdData(Activity activity, int placementId, AdsPlayCallback callback){
        this.activity = activity;
        this.callback = callback;
        AdPermissionChecker.checkSystemPermissions(activity);
        hideAdView();
        new AsyncVideoAdLoadTask(this).execute(placementId);
    }

    void hideAdView(){
        ViewGroup.LayoutParams params = adHolder.getLayoutParams();
        this.width = params.width;
        this.height = params.height;
        params.height = 0;
        params.width = 0;
        adHolder.setLayoutParams(params);
    }

    void showAdView(AdData adData){
        ViewGroup.LayoutParams params = adHolder.getLayoutParams();
        params.height = this.height;
        params.width = this.width;
        adHolder.setLayoutParams(params);

        String metric = "impression";
        AdLogDataUtil.log(metric,adData);
    }

    void closeAdView(){
        AdsPlayVideoInfeedHolder.this.videoView.setVisibility(GONE);
        AdsPlayVideoInfeedHolder.this.adHolder.setVisibility(GONE);
        this.callback.onFinished();
    }

    @Override
    public void onMediaReady(final AdData adData) {
        if(adData != null){
            try {
                showAdView(adData);
                Log.i("AdsPlay","--------------------------------------------------------------------");
                String file = "file://"+adData.getMedia();
                String adTitle = adData.getTitle();

                Log.i("AdsPlay","-------> playAd file: "+file);
                this.videoView.setVisibility(VISIBLE);

                this.videoView.setDataSource(file);
                this.videoView.setVolume(0, 2);

                this.videoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                    Log.i("AdsPlay","Ad Duration: "+mp.getDuration());
                    AdsPlayVideoInfeedHolder.this.videoView.start();
                    getVideoView().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        closeAdView();
                    }
                    });
                    }
                });

                this.videoView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    AdLogDataUtil.log("click",adData);
                    synchronized(this) {
                        if( !volumeEnabled){
                            volumeEnabled = true;
                            AdsPlayVideoInfeedHolder.this.videoView.setVolume(0, 2);
                        } else {
                            volumeEnabled = false;
                            AdsPlayVideoInfeedHolder.this.videoView.setVolume(0, 0);
                        }
                    }
                    }
                });

            } catch (Exception ioe) {
                Log.i("AdsPlay",ioe.getMessage());
                Log.i("AdsPlay",ioe.toString());
            }
        } else {
            closeAdView();
        }
    }

    @Override
    public String getCacheDir(){
        return this.activity.getCacheDir().getPath();
    }

    public void triggerFinishingCallback(){
        this.callback.onFinished();
    }

}
