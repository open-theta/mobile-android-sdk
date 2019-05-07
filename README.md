# Open SDK for Mobile Ad Tech 

* Masthead Ad
* Popup Ad
* Standard Display Banner


# Usage

### Copy library and update build.gradle
sample_app/build.gradle


### Put WebView tag in layout file
sample_app/src/main/res/layout/activity_main.xml
```
<WebView
            android:id="@+id/masthead_adview"
            android:layout_width="fill_parent"
            android:layout_height="272dp"
            />
```

### Register Java code
sample_app/src/main/java/net/adsplay/scalablevideo/sample/MainActivity.java
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
Copyright 2019 OpenTheta.com
