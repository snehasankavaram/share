package com.share;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.Profile;

public class AcceptConnectionActivity extends Activity {

    private ImageButton accept;
    private ImageButton decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_connection);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                accept = (ImageButton) stub.findViewById(R.id.acceptButton);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createNotification();
                        Contact c = new Contact(new Profile("Sally Smith", "CEO"));
                        Intent i = new Intent(getApplicationContext(), ContactActivity.class);
                        i.putExtra("contact", c);
                        startActivity(i);

                    }
                });
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
    }

    private void createNotification() {
        Contact c = new Contact(new Profile("Sarah Jones", "CEO"));
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
                        .setContentText("New connection from Sarah Jones")
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.drawable.face4,
                                "Accept contact", openContacts);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getBaseContext());

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
