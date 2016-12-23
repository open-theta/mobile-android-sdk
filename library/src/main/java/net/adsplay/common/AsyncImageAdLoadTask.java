package net.adsplay.common;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by trieu on 12/15/16.
 */

public class AsyncImageAdLoadTask extends AsyncTask<Integer, String, AdData> {

    static boolean rendered = false;
    AdsPlayAdComponent adsPlayAdComponent;

    public AsyncImageAdLoadTask(AdsPlayAdComponent adsPlayAdComponent) {
        this.adsPlayAdComponent = adsPlayAdComponent;
    }

    /**
     * Before starting background thread
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("AdsPlay","Starting AsyncImageAdLoadTask");
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected AdData doInBackground(Integer... placementIds) {
        if(placementIds.length==0 || rendered){
            return null;
        }
        int placementId = placementIds[0];
        int adType = AdData.ADTYPE_IMAGE_DISPLAY_AD;
        String uuid = UserProfileUtil.getUUID();
        AdData adData = AdDataLoader.getAdData(uuid, placementId, adType);
        return adData;
    }

    /**
     * After completing background task
     **/
    @Override
    protected void onPostExecute(AdData adData) {
        rendered = true;
        Log.i("AdsPlay","--> onPostExecute : "+adData);
        this.adsPlayAdComponent.onMediaReady(adData);
    }

}
