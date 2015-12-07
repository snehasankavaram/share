package com.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.james.sharedclasses.File;
import com.example.james.sharedclasses.FilesAdapter;
import com.example.james.sharedclasses.GetFilesRequestWrapper;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.ServerEndpoint;
import com.example.james.sharedclasses.UpdateFileMetadataRequest;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Sneha on 11/26/15.
 */
public class FileFragment extends Fragment {
    ArrayList<File> fileList;
    private static final String TAG = "FileFragment";
    private FilesAdapter adapter;
    private Retrofit retrofit;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        fileList = (ArrayList<File>) args.get("files");

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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.contact_files, container, false);

        adapter = new FilesAdapter(getContext(), fileList);

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                File file = (File) adapter.getItemAtPosition(position);
                Intent i = new Intent(getActivity(), TheirDropboxFileActivity.class);
                updateFileMetadata(file);
                i.putExtra("file", file);
                startActivity(i);
            }
        });
        listView.setAdapter(adapter);


        //Integer.toString(args.getInt(ARG_OBJECT)));
        return rootView;
    }

    private void updateFileMetadata(final File file) {
        ServerEndpoint service = retrofit.create(ServerEndpoint.class);
        Call<GetFilesRequestWrapper> call = service.updateMetadataForFile(new UpdateFileMetadataRequest(file.getRailsID(), LoginUtils.getLoginToken(getActivity())));
        call.enqueue(new Callback<GetFilesRequestWrapper>() {

            @Override
            public void onResponse(Response<GetFilesRequestWrapper> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d(TAG, "Successfully updated :" + file.getFileName());
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
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
