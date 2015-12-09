package com.share;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.ContactProfileWrapper;
import com.example.james.sharedclasses.File;
import com.example.james.sharedclasses.FileMetadataWrapper;
import com.example.james.sharedclasses.GetContactsRequestWrapper;
import com.example.james.sharedclasses.GetFilesRequestWrapper;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.Profile;
import com.example.james.sharedclasses.ServerEndpoint;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PollMetadataService extends Service {
    private Retrofit retrofit;
    private final String TAG ="PollMetadataService";
    private String user;
    private static final int INTERVAL = 10*1000;
    private static final int SECOND = 1000;

    public PollMetadataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "on start called");
        user = LoginUtils.getLoginToken(this);
//        OkHttpClient client = new OkHttpClient();
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        client.interceptors().add(interceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.WEBSITE_URL))
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Kick off new work to do
        createAndStartTimer();
        return START_STICKY;
    }

    public ArrayList<FileMetadataWrapper> getFileMetadata(String username) {
        ArrayList<FileMetadataWrapper> fileMetadata = new ArrayList<>();
        ServerEndpoint service = retrofit.create(ServerEndpoint.class);
        Call<GetFilesRequestWrapper> call = service.getFilesForUser(username);
        try{
            Response<GetFilesRequestWrapper> response = call.execute();
            if (response.isSuccess()) {
                GetFilesRequestWrapper filesRequestWrapper = response.body();
                fileMetadata.addAll(filesRequestWrapper.getFiles());
            }
            else {
                int statusCode = response.code();
                // handle request errors yourself
                ResponseBody errorBody = response.errorBody();
                try {
                    Log.d(TAG, String.format("Error: %d with body: %s", statusCode, errorBody.string()));
                }
                catch (IOException e) {
                    Log.d(TAG, e.toString());
                }
            }

        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        return fileMetadata;
    }

    public ArrayList<Contact> getContacts(String username) {
        ArrayList<Contact> contactsList = new ArrayList<>();
        ServerEndpoint service = retrofit.create(ServerEndpoint.class);
        Call<GetContactsRequestWrapper> call = service.getContactsForUser(username);
        try {
            Response<GetContactsRequestWrapper> response = call.execute();
            GetContactsRequestWrapper responseWrapper = response.body();
            if (responseWrapper != null) {
                List<ContactProfileWrapper> contactsWrapper =  responseWrapper.getContacts();
                for (ContactProfileWrapper contactWrapper: contactsWrapper) {
                    Contact c = contactWrapper.getContact();
                    Profile p = contactWrapper.getProfile();
                    ArrayList<File> files = contactWrapper.getFiles();
                    Log.d(TAG, p.getName());
                    for (File f : files) {
                        Log.d(TAG, "Get contacts. Filename: " + f.getFileName());
                    }
                    c.setFiles(files);
                    c.setProfile(p);
                    contactsList.add(c);
                }
            }

        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
        return contactsList;
    }

    private void createAndStartTimer() {
        CountDownTimer timer = new CountDownTimer(INTERVAL, SECOND) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<FileMetadataWrapper> fileMetadata = getFileMetadata(user);
                        LoginUtils.setFileMetadata(getBaseContext(), fileMetadata);
                        ArrayList<Contact> contacts = getContacts(user);
                        LoginUtils.setContacts(getBaseContext(), contacts);
                    }
                }).start();
                createAndStartTimer();
            }
        };
        timer.start();
    }
}
