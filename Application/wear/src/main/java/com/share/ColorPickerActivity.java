package com.share;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;

public class ColorPickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                ImageView i1 = (ImageView) findViewById(R.id.imageView1);
                ImageView i2 = (ImageView) findViewById(R.id.imageView2);
                ImageView i3 = (ImageView) findViewById(R.id.imageView3);
                ImageView i4 = (ImageView) findViewById(R.id.imageView4);
                i1.setBackgroundColor(Color.parseColor("#78A85C"));
                i2.setBackgroundColor(Color.parseColor("#46806D"));
                i3.setBackgroundColor(Color.parseColor("#C08669"));
                i4.setBackgroundColor(Color.parseColor("#B06073"));
            }
        });
    }
}
