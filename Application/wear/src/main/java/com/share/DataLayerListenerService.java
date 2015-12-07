package com.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.File;
import com.example.james.sharedclasses.FileMetadataWrapper;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.Metadatum;
import com.example.james.sharedclasses.Profile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

public class DataLayerListenerService extends WearableListenerService {
    private static final String TAG = "DataLayerListener";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "on create called");
        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "connected");
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/username") == 0) {
                    Log.d(TAG, "Data change called on username");
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    String username = dataMap.getString("username");
                    Profile profile = new Profile(dataMap.getDataMap("profile"));
                    Log.d(TAG, username);
                    LoginUtils.setLoginToken(DataLayerListenerService.this, username);
                    LoginUtils.setProfile(DataLayerListenerService.this, profile);

                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else if (item.getUri().getPath().compareTo("/contacts") == 0) {
                    Log.d(TAG, "Data change called on contacts");
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    ArrayList<DataMap> datamapContacts = dataMap.getDataMapArrayList("contacts");
                    ArrayList<Contact> contacts = new ArrayList<>();
                    for (DataMap datamapContact : datamapContacts) {
                        contacts.add(new Contact(datamapContact));
                    }
                    LoginUtils.setContacts(DataLayerListenerService.this, contacts);
                }
                else if (item.getUri().getPath().compareTo("/files") == 0) {
                    Log.d(TAG, "Data change called on files");
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    ArrayList<DataMap> filesDataMap = dataMap.getDataMapArrayList("files");
                    ArrayList<FileMetadataWrapper> currFilesMetadata = LoginUtils.getFileMetadata(DataLayerListenerService.this);
                    ArrayList<FileMetadataWrapper> filesMetadata = new ArrayList<>();
                    for (DataMap map : filesDataMap) {
                        FileMetadataWrapper wrapper = new FileMetadataWrapper(map);
                        filesMetadata.add(wrapper);

                        // Check if this is the item that has been changed
                        boolean found = false;
                        for (FileMetadataWrapper iter : currFilesMetadata) {
                            if (iter.getFile().getRailsID() == wrapper.getFile().getRailsID()) {
                                if (iter.getFile().getViewCount() != wrapper.getFile().getViewCount()) {
                                    fireNotifications(wrapper, iter);
                                    found = true;
                                }
                                break;
                            }
                        }

                        // New item
                        if (!found) {
                            fireNotifications(wrapper);
                        }
                    }
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    private void fireNotifications(FileMetadataWrapper updated, FileMetadataWrapper stale) {
        Log.d(TAG, "FIRE NOTIFICATIONS");
        ArrayList<Metadatum> updatedMetadata = updated.getMetadata();
        ArrayList<Metadatum> staleMetadata = stale.getMetadata();
        for (Metadatum u : updatedMetadata) {
            boolean found = false;
            for (Metadatum s : staleMetadata) {
                if (s.getViewUsername().equals(u.getViewUsername())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fireNotification(updated.getFile(), u);
            }
        }
    }

    private void fireNotifications(FileMetadataWrapper updated) {
        ArrayList<Metadatum> updatedMetadata = updated.getMetadata();
        Log.d(TAG, "FIRE NOTIFICATIONS w/ new file");
        for (Metadatum u : updatedMetadata) {
            fireNotification(updated.getFile(), u);
        }
    }

    private void fireNotification(File f, Metadatum updatedMetadatum) {
        Log.d(TAG, "Fire notification on file " + f.getFileName());
        int notificationId = 001;

        android.support.v4.app.NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.file_icon)
                        .setContentText(String.format("%s has seen %s", updatedMetadatum.getViewUsername(), f.getFileName()));

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
