package com.share;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.james.sharedclasses.CreateFileRequest;
import com.example.james.sharedclasses.GetFilesRequestWrapper;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.ServerEndpoint;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DBFileActivity extends Activity {
    private WebView webView;
    private Retrofit retrofit;
    private final String TAG = "DBFileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbfile);

        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.WEBSITE_URL))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final String url = (String) getIntent().getSerializableExtra("url");
        final String localPath  = (String) getIntent().getSerializableExtra("local_path");
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new Callback());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl(
                "http://docs.google.com/gview?embedded=true&url=" + url);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DBFileActivity.this);
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ServerEndpoint service = retrofit.create(ServerEndpoint.class);
                                final String username = LoginUtils.getLoginToken(getBaseContext());
                                Log.d(TAG, "Username: "+ username);
                                Call<GetFilesRequestWrapper> call = service.createFileForUser(new CreateFileRequest(username, url, localPath));
                                call.enqueue(new retrofit.Callback<GetFilesRequestWrapper>() {
                                    @Override
                                    public void onResponse(Response<GetFilesRequestWrapper> response, Retrofit retrofit) {
                                        if (response.isSuccess()) {
                                            Log.d(TAG, String.format("Created file: %s %s %s", username, url, localPath));
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

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            Log.d("Override URL called", url);
            webView.loadUrl(url);
            return(false);
        }
    }
}
