package net.adsplay.common;

/**
 * Created by mac on 12/26/16.
 */

public class AdTrackingEvent {
    String event;
    String url;
    int delay;

    public AdTrackingEvent(String event, String url, int delay) {
        this.event = event;
        this.url = url;
        this.delay = delay;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
