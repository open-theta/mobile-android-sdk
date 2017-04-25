# AdsPlay Mobile SDK for special formats

* Masthead Ad


# Usage

### Copy library and update build.gradle
https://github.com/AdsPLAY/mobile-android-sdk/blob/master/sample_app/build.gradle


### Put WebView tag in layout file
https://github.com/AdsPLAY/mobile-android-sdk/blob/master/sample_app/src/main/res/layout/activity_main.xml
```
<WebView
            android:id="@+id/masthead_adview"
            android:layout_width="fill_parent"
            android:layout_height="272dp"
            />
```

### Register Java code
https://github.com/AdsPLAY/mobile-android-sdk/blob/master/sample_app/src/main/java/net/adsplay/scalablevideo/sample/MainActivity.java
```
    WebView mastheadAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mastheadAdview = (WebView) findViewById(R.id.masthead_adview);
        new AdsPlayMastheadWebview(this,mastheadAdview);
    }

    @Override
    public void onResume() {
        super.onResume();
        mastheadAdview.onResume();
    }
```

# License
```
Copyright 2016 AdsPlay.net
