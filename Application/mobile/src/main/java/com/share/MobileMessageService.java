package com.share;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class MobileMessageService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String COLOR = "/Color";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitude;
    private String mLongitude;
    private JSONObject obj;
    private JSONArray jarr;
    private boolean firsttime = true;
    public boolean validUN = false;
    private String color = "";

    private String urlbase = "http://share-backend.herokuapp.com/";

    public void firstTime () {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
//        Log.d("QSS_GETQUAKE", "GAPI gets built");
        this.createUser2(); //TEMPORARY!! FOR TESTING PURPOSES ONLY
    }

    @Override
    public void onDestroy () {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (this.firsttime) {
            this.firstTime();
            this.firsttime = false;
        }

        if( messageEvent.getPath().equalsIgnoreCase( COLOR ) ) {

            mGoogleApiClient.connect();
            this.color = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            this.connect2Server();
            if (mLatitude != null && mLongitude != null) {
//                this.connect2Server();
            }
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
//        Log.d("QSS_GETQUAKE", mLastLocation.toString());
        if (mLastLocation != null) {
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLongitude = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

    @Override
    public void onConnectionSuspended(int i) {}

    private void createUser2() { //THIS IS FOR CREATING USERS
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = urlbase + "users/create";
        JSONObject jsonBody;
        try {
            jsonBody = new JSONObject("{\"username\":\"sarahs\"}");
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //...
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //...
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void connect2Server () { //STILL WORKING ON THIS METHOD

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlbase + "users/show" + "?username=sarahs",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MobileMessageService", "users/show: " + response);
                        if (!response.equals("{\"user\":null}")) {
                            validUN = true;
                        } else {
                            validUN = false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        queue.add(stringRequest);

        if (validUN) {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, urlbase + "connection/create"
                    + "?username=sarahs&latitude=65.9667&longitude=-18.5333&color=" + color,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("MobileMessageService", "connection/create: " + response);
                            //TODO: handshake while loop
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {}
            });
            queue.add(stringRequest2);
        }
    }


}
