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
import android.widget.TextView;

import net.adsplay.common.AdData;
import net.adsplay.common.AdLogDataUtil;
import net.adsplay.common.AdPermissionChecker;
import net.adsplay.common.AdsPlayAdComponent;
import net.adsplay.common.AdsPlayCallback;
import net.adsplay.common.AsyncVideoAdLoadTask;

/**
 * Created by trieu on 10/30/16.
 */
public class AdsPlayHolderVideo extends RelativeLayout implements AdsPlayAdComponent {

    Activity activity;
    private LinearLayout adHolder;
    private ScalableVideoView videoView;
    private TextView txttitle;
    AdsPlayCallback callback;
    //private TextView txtdescription
    //private Button btnClose;

    public AdsPlayHolderVideo(Context context) {
        super(context);
        init();
    }

    public AdsPlayHolderVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdsPlayHolderVideo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ScalableVideoView getVideoView() {
        return this.videoView;
    }

    boolean volumeEnabled = false;
    int width, height;


    private void init() {
        inflate(getContext(), R.layout.holdervideo, this);

        this.videoView = (ScalableVideoView) findViewById(R.id.masterheadVideoView);
        this.txttitle = (TextView) findViewById(R.id.masterheadTitle);
        this.adHolder = (LinearLayout)findViewById(R.id.masterheadHolder);

        this.videoView.setVisibility(GONE);
        this.txttitle.setVisibility(GONE);
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
        AdsPlayHolderVideo.this.videoView.setVisibility(GONE);
        AdsPlayHolderVideo.this.txttitle.setVisibility(GONE);
        AdsPlayHolderVideo.this.adHolder.setVisibility(GONE);
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
                this.txttitle.setVisibility(VISIBLE);

                this.videoView.setDataSource(file);
                this.videoView.setVolume(0, 2);
                this.txttitle.setText(adTitle);

                this.videoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                    Log.i("AdsPlay","Ad Duration: "+mp.getDuration());
                    AdsPlayHolderVideo.this.videoView.start();
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
                            AdsPlayHolderVideo.this.videoView.setVolume(0, 2);
                        } else {
                            volumeEnabled = false;
                            AdsPlayHolderVideo.this.videoView.setVolume(0, 0);
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
