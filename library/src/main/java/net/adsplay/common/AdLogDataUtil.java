package net.adsplay.common;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by trieu on 11/3/16.
 */

public class AdLogDataUtil {

    static final String[] baseAdLogUrls = {
            "http://log1.adsplay.net/track/videoads",
            "http://log2.adsplay.net/track/videoads",
            "http://log3.adsplay.net/track/videoads",
            "http://log4.adsplay.net/track/videoads",
    };

    static final OkHttpClient client = new OkHttpClient();

    static List<String> buildLogUrl(String metric, AdData adData){
        List<String> urls = new ArrayList<>();

        StringBuilder u = new StringBuilder();
        int adId = adData.getAdId();
        String adBeacon = adData.getAdBeacon();
        int t = (int) (System.currentTimeMillis()/1000L);

        int s = baseAdLogUrls.length;
        String baseUrl = baseAdLogUrls[t % s];

        u.append(baseUrl).append("?metric=").append(metric);
        u.append("&adid=").append(adId);
        u.append("&beacon=").append(adBeacon);
        u.append("&t=").append(t);

        urls.add(u.toString());
        urls.addAll(adData.getTracking3rdUrls());

        return urls;
    }

    public static void log(String metric, AdData adData){
        try {
            List<String> urls = buildLogUrl(metric, adData);
            for(String url : urls){
                Log.e("AdsPlay",metric + " => "+url);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent","AdsPlayAndroidSDK1x6")
                        .build();
                client.newCall(request).execute();
            }
        } catch (Exception e){
            Log.e("AdsPlay",e.toString());
        }
    }
}
