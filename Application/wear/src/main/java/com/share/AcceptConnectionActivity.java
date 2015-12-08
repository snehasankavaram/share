package com.share;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.james.sharedclasses.Contact;
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

public class AcceptConnectionActivity extends Activity implements DataApi.DataListener{

    private ImageButton accept;
    private ImageButton decline;
    private Profile profile;
    private String TAG = "AcceptConnectionActivity";
    private static final String ACCEPTED_CONNECTION = "/ACCEPTED_CONNECTION";
    GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_connection);
        profile = (Profile) getIntent().getSerializableExtra("profile");
        if (profile == null) {
            Log.d(TAG, "Profile is null");
            profile = new Profile("Ben Bodien", "ben@evertask.com", "925-351-1211","CEO at EverTask");
        }

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
        Wearable.DataApi.addListener(mGoogleApiClient, this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting for connection to accept");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                accept = (ImageButton) stub.findViewById(R.id.acceptButton);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        sendMessage(ACCEPTED_CONNECTION, profile.getName());
                    }
                });
                TextView textView = (TextView) stub.findViewById(R.id.contact_name);
                textView.setText(profile.getName());

                ImageView i = (ImageView) stub.findViewById(R.id.imageView);
                int selected = R.drawable.face4;
                if (profile != null && profile.getName() != null) {
                    String uri = String.format("@drawable/%s", profile.getName().replace(" ", "_").toLowerCase());
                    int imageResource = getBaseContext().getResources().getIdentifier(uri, null, getBaseContext().getPackageName());
                    if (imageResource != 0) {
                        selected = imageResource;
                    }
                }
                i.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(getResources(),selected)));
                decline = (ImageButton) stub.findViewById(R.id.declineButton);
                decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                });
            }
        });

//        ImageView img = (ImageView)findViewById(R.id.imageView);
//        img.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face3)));
    }

    private void createNotification(Contact c) {
        Intent intent = new Intent(getBaseContext(), ContactActivity.class);
        intent.putExtra("contact", c);
        PendingIntent openContacts = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(getBaseContext(), AcceptConnectionActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(getBaseContext(), 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getBaseContext())
                        .setSmallIcon(R.drawable.logo)
                        .setContentText("New connection from Brenda Jones")
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.drawable.laura_lee,
                                "Accept contact", openContacts);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getBaseContext());

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/new_contact") == 0) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    DataMap dataMapNewConnection = dataMap.getDataMap("contact");
                    Contact contact = new Contact(dataMapNewConnection);
                    createNotification(contact);
                    Intent i = new Intent(getApplicationContext(), ContactActivity.class);
                    i.putExtra("contact", contact);
                    startActivity(i);
                }
            }
        }

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
