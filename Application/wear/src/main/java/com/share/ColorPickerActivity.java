package com.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class ColorPickerActivity extends Activity {
    ImageView[] imageViews = new ImageView[4];

    String mNode; // the connected device to send the message to
    GoogleApiClient mGoogleApiClient;
    public static String TAG = "ColorPicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        Context context = getApplicationContext();
        CharSequence text = "select a color";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d("connected from watch", "test");
//                        CapabilityApi.CapabilityListener capabilityListener =
//                                new CapabilityApi.CapabilityListener() {
//                                    @Override
//                                    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
//                                        updateTranscriptionCapability(capabilityInfo);
//                                    }
//                                };
//
//                        Wearable.CapabilityApi.addCapabilityListener(
//                                mGoogleApiClient,
//                                capabilityListener,
//                                "message_passer");
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        /* Connection was interrupted */
                    }
                })
                .build();

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

    private void updateTranscriptionCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();

        mNode = pickBestNodeId(connectedNodes);
    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

//    private void sendMessage(final String path, String message) {
//
//        new Thread( new Runnable() {
//            @Override
//            public void run() {
//                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
//                for (Node node : nodes.getNodes()) {
//                    Log.d(TAG, node.getId());
//                    Wearable.MessageApi.sendMessage(
//                            mGoogleApiClient, node.getId(), path, null).setResultCallback(
//
//                            new ResultCallback<MessageApi.SendMessageResult>() {
//                                @Override
//                                public void onResult(MessageApi.SendMessageResult sendMessageResult) {
//
//                                    if (!sendMessageResult.getStatus().isSuccess()) {
//                                        Log.e(TAG, "Failed to send message with status code: "
//                                                + sendMessageResult.getStatus().getStatusCode());
//                                    }
//                                }
//                            }
//                    );
////        }
//                }
//            }
//        }).start();
//    }

//    Will send a server POST request of (color, timestamp, location) -> user_id
    private void sendColorToPhone(ImageView v) {
        ColorDrawable drawable = (ColorDrawable) v.getBackground();
        String c = String.format("%06X", (0xFFFFFF & drawable.getColor()));
        Log.d("Color", c);
        mGoogleApiClient.connect();
        sendMessage("/Color", c);
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
}
