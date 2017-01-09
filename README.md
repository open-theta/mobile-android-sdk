# AdsPlay Mobile SDK for special format

There are 2 types of mobile ad formats:
* Infeed Video Ad
* Display Image Banner





# Usage

### Infeed Video Ad tag in layout file
![alt text](https://raw.githubusercontent.com/AdsPLAY/mobile-android-sdk/master/specs/infeed.png "AdsPlay Infeed in App View")
```
    <net.adsplay.holder.AdsPlayHolderVideo
                    android:id="@+id/masterhead_view"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    />
```

### Masthead Video Ad tag in layout file
![alt text](https://raw.githubusercontent.com/AdsPLAY/mobile-android-sdk/master/specs/masthead.png "AdsPlay Masthead in App View")
```
    <net.adsplay.holder.AdsPlayHolderVideo
                    android:id="@+id/masterhead_view"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    />
```

### Display Image Banner tag in layout file

```
    <net.adsplay.holder.AdsPlayHolderImage
            android:id="@+id/banner_view"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            />
```

### Sample usage in source code

![alt text](http://adsplay.net/img/adsplay-mobile-demo.jpg "AdsPlay Demo App View")

```
public class MainActivity extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for mobile banner
        ((AdsPlayHolderImage)findViewById(R.id.banner_view)).loadDataAdUnit(this, 1008);

        //for mobile infeed video
        ((AdsPlayHolderVideo)findViewById(R.id.masterhead_view)).loadDataAdUnit(this, 1009);
    }
}
```

# License
```
Copyright 2016 AdsPlay.net
