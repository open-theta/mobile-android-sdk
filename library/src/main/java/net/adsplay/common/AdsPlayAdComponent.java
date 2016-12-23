package net.adsplay.common;

import android.app.Activity;
import android.content.Context;

/**
 * Created by trieu on 10/31/16.
 */

public interface AdsPlayAdComponent {
    void loadAdData(Activity activity, int placementId, AdsPlayCallback callback);
    void onMediaReady(AdData adData);
    String getCacheDir();
    void triggerFinishingCallback();
}
