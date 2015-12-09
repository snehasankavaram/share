package com.share;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.james.sharedclasses.Connection;
import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.ContactProfileWrapper;
import com.example.james.sharedclasses.CreateConnectionRequest;
import com.example.james.sharedclasses.CreateContactRequest;
import com.example.james.sharedclasses.GetConnectionEstablishedWrapper;
import com.example.james.sharedclasses.GetUserRequestWrapper;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.ServerEndpoint;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MobileMessageService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String COLOR = "/Color";
    private static final String ACCEPTED_CONNECTION = "/ACCEPTED_CONNECTION";
    private static final String CANCELED = "/cancel";

    private GoogleApiClient mGoogleApiClient;
    private Retrofit retrofit;
    Handler handler;
    RequestQueue queue;
    private Location mLastLocation;
    private final String TAG = "MobileMessageService";
    private String mLatitude;
    private String mLongitude;
    private String color = "";
    private GetConnectionTask getConnectionTask;

    @Override
    public void onCreate() {
        handler = new Handler();
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();

        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.WEBSITE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        queue = Volley.newRequestQueue(this);
    }

    @Override
    public void onDestroy () {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase( COLOR ) ) {
            this.color = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            getConnectionTask  = new GetConnectionTask(retrofit.create(ServerEndpoint.class), color);
            getConnectionTask.execute();
            Log.d("got message", this.color);
        }
        else if ( messageEvent.getPath().equalsIgnoreCase( ACCEPTED_CONNECTION )) {
            String their_username = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, String.format("Adding %s as a contact", their_username));
            addContact(their_username);
        }
        else if ( messageEvent.getPath().equalsIgnoreCase( CANCELED)) {
            if (getConnectionTask != null) {
                Log.d(TAG, "Canceled task");
                getConnectionTask.cancel(true);
            }
        }
    }

    private boolean createConnection(String color) {
        ServerEndpoint service = retrofit.create(ServerEndpoint.class);
        final String username = LoginUtils.getLoginToken(this);
        Call<GetConnectionEstablishedWrapper> call = service.createConnection(new CreateConnectionRequest(username, color));
        try {
            Response<GetConnectionEstablishedWrapper> response = call.execute();
            if (response.isSuccess()) {
                return true;
            } else {
                int statusCode = response.code();

                // handle request errors yourself
                ResponseBody errorBody = response.errorBody();
                try {
                    Log.d(TAG, String.format("Error: %d with body: %s", statusCode, errorBody.string()));
                }
                catch (IOException e) {
                    Log.d(TAG, String.format(e.toString()));
                }
            }
        }
        catch (IOException e){
            Log.d(TAG, e.toString());
        }
        return false;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("Got this", "connection");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLongitude = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

    @Override
    public void onConnectionSuspended(int i) {}

    private void addContact (String contact_username) {
        ServerEndpoint service = retrofit.create(ServerEndpoint.class);
        String username = LoginUtils.getLoginToken(this);
        Call<ContactProfileWrapper> call = service.createContact(new CreateContactRequest(username, contact_username));
        call.enqueue(new Callback<ContactProfileWrapper>() {
            @Override
            public void onResponse(retrofit.Response<ContactProfileWrapper> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d(TAG, "Successfully added contact");
                    ContactProfileWrapper contactProfileWrapper = response.body();

                    ArrayList<Contact> contacts = LoginUtils.getContacts(getBaseContext());
                    Contact c = contactProfileWrapper.getContact();
                    c.setProfile(contactProfileWrapper.getProfile());
                    c.setFiles(contactProfileWrapper.getFiles());
//                    DataLayerUtil.sendNewContactToWear(mGoogleApiClient, c, TAG);
                    Gson gson = new Gson();
                    sendMessage("/new_contact", gson.toJson(c));

                    Log.d(TAG, String.format("Added contact: %s with contactID: %d",contactProfileWrapper.getContact().getProfile().getName(), contactProfileWrapper.getContact().getContactId()));
                    contacts.add(c);
                    LoginUtils.setContacts(getBaseContext(), contacts);
                    Intent intent = new Intent(getApplicationContext(), ContactPageActivity.class);
                    intent.putExtra("contact", c);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    int statusCode = response.code();

                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        Log.d(TAG, String.format("Error: %d with body: %s", statusCode, errorBody.string()));
                    }
                    catch (IOException e) {
                        Log.d(TAG, String.format(e.toString()));
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                // handle execution failures like no internet connectivity
            }
        });

    }

    public class GetConnectionTask extends AsyncTask<Void, Void, GetUserRequestWrapper> {
        private String color;
        private ServerEndpoint serverEndpoint;
        public GetConnectionTask(ServerEndpoint serverEndpoint, String color) {
            this.color = color;
            this.serverEndpoint = serverEndpoint;
        }
        @Override
        protected GetUserRequestWrapper doInBackground(Void... params) {
            boolean createdConnection = false;
            while (true) {
                if (isCancelled()) {
                    break;
                }
                if (!createdConnection) {
                    createdConnection = createConnection(color);
                    if (!createdConnection) {
                        return null;
                    }
                }
                else {
                    String possibleProfile = checkConnectionEstablished(color);
                    if (possibleProfile == null) {
                        try{
                            Log.d(TAG, "Didn't find connection yet, will try again in 1 second");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Log.d(TAG, e.toString());
                            return null;
                        }

                        // Haven't found other connection yet, restart loop.
                        continue;
                    }
                    Call<GetUserRequestWrapper> requestWrapper = serverEndpoint.getUser(possibleProfile);
                    try{
                        Response<GetUserRequestWrapper> response = requestWrapper.execute();
                        if (response.isSuccess()) {
                            GetUserRequestWrapper userRequest = response.body();
                            return userRequest;
                        }
                        else {
                            int statusCode = response.code();

                            // handle request errors yourself
                            ResponseBody errorBody = response.errorBody();
                            try {
                                Log.d(TAG, String.format("Error: %d with body: %s", statusCode, errorBody.string()));
                            }
                            catch (IOException e) {
                                Log.d(TAG, String.format(e.toString()));
                            }

                        }
                    }
                    catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }

                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(GetUserRequestWrapper result) {
            if (result != null) {
                Log.d(TAG, "Get Connection task postExecute called");
                Gson gson = new Gson();
                sendMessage("/new_connection", gson.toJson(result));
//                DataLayerUtil.sendNewConnectionProfileToWear(mGoogleApiClient, result, TAG);
            }
        }
    }

    private String checkConnectionEstablished(String color) {
        ServerEndpoint service = retrofit.create(ServerEndpoint.class);
        String username = LoginUtils.getLoginToken(MobileMessageService.this);
        Call<GetConnectionEstablishedWrapper> getConnectionCall = service.checkConnectionEstablished(username, color);
        try{
            Response<GetConnectionEstablishedWrapper> response = getConnectionCall.execute();
            if (response.isSuccess()) {
                GetConnectionEstablishedWrapper wrapper = response.body();
                for (Connection connection : wrapper.getConnection()) {
                    if (!connection.getUsername().equals(username)) {
                        return connection.getUsername();
                    }
                }
            }
            else {
                int statusCode = response.code();

                // handle request errors yourself
                ResponseBody errorBody = response.errorBody();
                try {
                    Log.d(TAG, String.format("Error: %d with body: %s", statusCode, errorBody.string()));
                }
                catch (IOException e) {
                    Log.d(TAG, String.format(e.toString()));
                }

            }
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
        return null;
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
