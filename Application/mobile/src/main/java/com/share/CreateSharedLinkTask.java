package com.share;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.example.james.sharedclasses.CreateFileRequest;
import com.example.james.sharedclasses.GetFilesRequestWrapper;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.ServerEndpoint;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by james on 12/5/15.
 */
public class CreateSharedLinkTask extends AsyncTask<Void, Void, Boolean> {
    private DropboxAPI mApi;
    private Retrofit retrofit;
    private String localPath;
    private String username;
    private String fileName;
    private Context context;
    private final String TAG = "CreateSharedLinkTask";

    public CreateSharedLinkTask(Context context, Retrofit retrofit, DropboxAPI api, String localPath, String username, String fileName) {
        this.context = context;
        this.retrofit = retrofit;
        this.mApi = api;
        this.localPath = localPath;
        this.username = username;
        this.fileName = fileName;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            ServerEndpoint service = retrofit.create(ServerEndpoint.class);
            DropboxAPI.DropboxLink link = mApi.share(localPath);
            Call<GetFilesRequestWrapper> call = service.createFileForUser(new CreateFileRequest(username, link.url, localPath, fileName));
            try {
                Response<GetFilesRequestWrapper> response = call.execute();
                if (response.isSuccess()) {
                    Log.d(TAG, String.format("Created file: %s %s %s", username, link.url, localPath));
                    reloadFileMetadata(username);
                    return true;
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
        } catch (DropboxException e) {
            Log.d(TAG, e.toString());
        }
        return false;
    }

    public void reloadFileMetadata(String username) {
        ServerEndpoint service = retrofit.create(ServerEndpoint.class);
        Call<GetFilesRequestWrapper> call = service.getFilesForUser(username);
        call.enqueue(new Callback<GetFilesRequestWrapper>() {
            @Override
            public void onResponse(Response<GetFilesRequestWrapper> response, Retrofit retrofit) {
                GetFilesRequestWrapper filesRequestWrapper = response.body();
                LoginUtils.setFileMetadata(context, filesRequestWrapper.getFiles());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
