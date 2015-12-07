package com.share;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.example.james.sharedclasses.FileMetadataWrapper;
import com.example.james.sharedclasses.GetFilesRequestWrapper;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.ServerEndpoint;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PollMetadataService extends Service {
    private Retrofit retrofit;
    private final String TAG ="PollMetadataService";
    private String user;
    private static final int INTERVAL = 20*1000;
    private static final int SECOND = 1000;

    public PollMetadataService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "on create called");
        user = LoginUtils.getLoginToken(this);
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.WEBSITE_URL))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

    private void createAndStartTimer() {
        CountDownTimer timer = new CountDownTimer(INTERVAL, SECOND) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<FileMetadataWrapper> fileMetadata = getFileMetadata(user);
                        LoginUtils.setFileMetadata(getBaseContext(), fileMetadata);
                    }
                }).start();
                createAndStartTimer();
            }
        };
        timer.start();
    }
}
