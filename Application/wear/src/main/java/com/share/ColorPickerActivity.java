package com.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ColorPickerActivity extends Activity {
    ImageView[] imageViews = new ImageView[4];
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

//                i1.setBackgroundColor(Color.parseColor("#ff6666"));
//                i2.setBackgroundColor(Color.parseColor("#b3fb6a"));
//                i3.setBackgroundColor(Color.parseColor("#75b2f0"));
//                i4.setBackgroundColor(Color.parseColor("#cf79ec"));

                imageViews[0] = i1;
                imageViews[1] = i2;
                imageViews[2] = i3;
                imageViews[3] = i4;

                for (ImageView i : imageViews) {
                    i.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendColorToPhone((ImageView) v);
                            Intent i = new Intent(getApplicationContext(), AcceptConnectionActivity.class);
                            startActivity(i);
                        }
                    });
                }
            }
        });
    }

//    Will send a server POST request of (color, timestamp, location) -> user_id
    private void sendColorToPhone(ImageView v) {
        ColorDrawable drawable = (ColorDrawable) v.getBackground();
        String c = String.format("#%06X", (0xFFFFFF & drawable.getColor()));
        Log.d("Color", c);

    }
}
