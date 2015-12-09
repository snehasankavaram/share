package com.share;

import android.util.Log;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.FileMetadataWrapper;
import com.example.james.sharedclasses.Profile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

/**
 * Created by james on 12/5/15.
 */
public class DataLayerUtil {
    public static void sendContactsToWear(GoogleApiClient mGoogleApiClient, ArrayList<Contact> contacts, final String TAG) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/contacts");
        ArrayList<DataMap> contactsAsDataMaps = new ArrayList<>();
        for (Contact c : contacts) {
            contactsAsDataMaps.add(c.putToDataMap(new DataMap()));
        }
        putDataMapReq.getDataMap().putDataMapArrayList("contacts", contactsAsDataMaps);
        sendGenericToWear(mGoogleApiClient, putDataMapReq, TAG);
    }

    public static void sendUserDataToWear(GoogleApiClient mGoogleApiClient, String username, Profile profile, final String TAG) {

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/username");

        putDataMapReq.getDataMap().putString("username", username);
        putDataMapReq.getDataMap().putDataMap("profile", profile.putToDataMap(new DataMap()));
        sendGenericToWear(mGoogleApiClient, putDataMapReq, TAG);
    }

    public static void sendFileMetadataToWear(GoogleApiClient mGoogleApiClient, ArrayList<FileMetadataWrapper> filesMetadata, final String TAG) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/files");
        ArrayList<DataMap> dataMaps = new ArrayList<>();
        for (FileMetadataWrapper fileMetadata : filesMetadata) {
            dataMaps.add(fileMetadata.putToDataMap(new DataMap()));
        }

        putDataMapReq.getDataMap().putDataMapArrayList("files", dataMaps);

        sendGenericToWear(mGoogleApiClient, putDataMapReq, TAG);
    }

    public static void sendGenericToWear(GoogleApiClient mGoogleApiClient, PutDataMapRequest putDataMapReq, final String TAG) {
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if(result.getStatus().isSuccess()) {
                    Log.d(TAG, "Data item set: " + result.getDataItem().getUri());
                }
            }
        });
    }

//    public static void sendNewConnectionProfileToWear(GoogleApiClient mGoogleApiClient, GetUserRequestWrapper getUserRequest, final String TAG) {
//        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/new_connection");
//        putDataMapReq.getDataMap().putDataMap("connection", getUserRequest.putToDataMap(new DataMap()));
//        sendGenericToWear(mGoogleApiClient, putDataMapReq, TAG);
//    }
//
//    public static void sendNewContactToWear(GoogleApiClient mGoogleApiClient, Contact contact, final String TAG) {
//        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/new_contact");
//        putDataMapReq.getDataMap().putDataMap("contact", contact.putToDataMap(new DataMap()));
//        sendGenericToWear(mGoogleApiClient, putDataMapReq, TAG);
//    }
}
