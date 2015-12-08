package com.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.james.sharedclasses.Profile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class ColorPickerActivity extends Activity implements DataApi.DataListener {
    ImageView[] imageViews = new ImageView[4];

    String mNode; // the connected device to send the message to
    GoogleApiClient mGoogleApiClient;
    public static String TAG = "ColorPicker";

    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        CharSequence text = "select a color";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(ColorPickerActivity.this, text, duration);
        toast.show();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d("connected from watch", "test");
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        /* Connection was interrupted */
                    }
                })
                .build();

        progressDialog = new ProgressDialog(ColorPickerActivity.this);
        progressDialog.setMessage("Waiting for contact");
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Canceled progress dialog");
                mGoogleApiClient.connect();
                sendMessage("/cancel", "true");
                dialog.dismiss();
            }
        });
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);

        Wearable.DataApi.addListener(mGoogleApiClient, this);

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

                findViewById(R.id.layout).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                Log.d(TAG, "Action down");
                                x1 = event.getX();
                                return true;
                            case MotionEvent.ACTION_UP:
                                Log.d(TAG, "Action up");
                                x2 = event.getX();
                                float deltaY = x2 - x1;
                                if (Math.abs(deltaY) > MIN_DISTANCE) {
                                    // Swipe from left to right
                                    Log.d(TAG, "Swipe left to right");
                                    if (x2 > x1) {
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        return true;
                                    }
                                }
                                break;
                        }

                        for (ImageView i : imageViews) {
                            if (event.getAction() == MotionEvent.ACTION_UP) { // click event
                                Rect childRect = new Rect();
                                i.getGlobalVisibleRect(childRect);
                                // check if event is within child's boundaries
                                if (childRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                                    sendColorToPhone(i);
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            }
        });
    }

//    Will send a server POST request of (color, timestamp, location) -> user_id
    private void sendColorToPhone(ImageView v) {
        ColorDrawable drawable = (ColorDrawable) v.getBackground();
        String c = String.format("%06X", (0xFFFFFF & drawable.getColor()));
        Log.d("Color", c);
        mGoogleApiClient.connect();
        sendMessage("/Color", c);
        progressDialog.show();
    }

    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.connect();
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }


    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/new_connection") == 0) {
                    Log.d(TAG, "Data change called on new connection");
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    DataMap dataMapNewConnection = dataMap.getDataMap("profile");
                    Profile profile = new Profile(dataMapNewConnection);
                    Intent intent = new Intent(getApplicationContext(), AcceptConnectionActivity.class);
                    intent.putExtra("profile", profile);
                    startActivity(intent);
                }
            }
        }
    }
}
