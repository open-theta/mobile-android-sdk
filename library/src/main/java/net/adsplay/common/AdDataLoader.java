package net.adsplay.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by trieu on 11/3/16.
 */

public class AdDataLoader {

    static final String[] baseAdDeliveryUrls = {
            "http://d1.adsplay.net/get",
            "http://d2.adsplay.net/get",
            "http://d4.adsplay.net/get",
            "http://d3.adsplay.net/get",
    };

    public static String getAdUrl(String uuid, int placementId, int adType){
        int t = (int) (System.currentTimeMillis()/1000L);
        int s = baseAdDeliveryUrls.length;
        String baseUrl = baseAdDeliveryUrls[t % s];
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?placement=").append(placementId);
        url.append("&uuid=").append(uuid);
        url.append("&adtype=").append(adType);
        url.append("&t=").append(t);
        return url.toString();
    }

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5L, TimeUnit.SECONDS)
            .writeTimeout(5L, TimeUnit.SECONDS);


    public static AdData getAdData(String uuid, int placementId, int adType){
        OkHttpClient client = httpClient.build();
        AdData adData = null;
        try {
            String adUrl = getAdUrl(uuid, placementId, adType);
            Log.i("AdsPlay", adUrl);
            Request request = new Request.Builder()
                    .url(adUrl)
                    .addHeader("User-Agent","AdsPlayAndroidSDK")
                    .build();
            Response response = client.newCall(request).execute();
            String rs = response.body().string();
            Log.i("AdsPlay",rs);

            JSONObject ad = new JSONObject(rs);

            if(ad.get("adId") == null){
                return null;
            }
            int adId = ad.getInt("adId");
            int adType2 = ad.getInt("adType");

            String media = ad.getString("adMedia");
            String clickActionText = ad.getString("clickActionText");
            String adBeacon = ad.getString("adBeacon");

            if(adType == AdData.ADTYPE_IMAGE_DISPLAY_AD){
                media = "http:" + media;
            }
            String clickUrl = ad.getString("clickthroughUrl");
            adData = new AdData(adId
                    , media
                    , clickActionText
                    , clickUrl
            );

            //for AdsPlay log tracking
            adData.setAdBeacon(adBeacon);

            //for 3rd party log tracking
            JSONArray adTracking3rdUrls = ad.getJSONArray("adTracking3rdUrls");
            List<AdTrackingEvent> listData;
            if (adTracking3rdUrls != null) {
                listData = new ArrayList<>(adTracking3rdUrls.length());
                for (int i=0;i<adTracking3rdUrls.length();i++){
                    JSONObject o = adTracking3rdUrls.getJSONObject(i);
                    if(o != null){
                        listData.add(new AdTrackingEvent(o.getString("event"), o.getString("url"),o.getInt("delay")));
                    }
                }
            } else {
                listData = new ArrayList<>(0);
            }
            adData.setTracking3rdUrls(listData);


        } catch (Exception e){
            Log.i("AdsPlay",e.toString());
        }
        return adData;
    }
}
