package com.share;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.james.sharedclasses.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TheirDropboxFileActivity extends AppCompatActivity {
    File file;
    WebView webView;
    private final String TAG = "TheirDropboxFiles";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_their_dropbox_file);

        file = (File) getIntent().getSerializableExtra("file");
        Log.d(TAG, file.getLink());

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new Callback());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        LoadRedirectedDropboxLinkTask task = new LoadRedirectedDropboxLinkTask(file.getLink());
        task.execute();
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

    public class LoadRedirectedDropboxLinkTask extends AsyncTask<Void, Void, String> {
        private String shortenedURL;

        public LoadRedirectedDropboxLinkTask(String url) {
            this.shortenedURL = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            String redirectedUrl = null;
            try {
                URL inputURL = new URL(shortenedURL);
                URLConnection conn = inputURL.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                System.out.println("Redirected URL: " + conn.getURL());
                redirectedUrl = conn.getURL().toString();
                is.close();

            } catch (MalformedURLException e) {
                Log.d(TAG, "Please input a valid URL");
            } catch (IOException ioe) {
                Log.d(TAG, "Can not connect to the URL");
            }

            return redirectedUrl;
        }

        @Override
        protected void onPostExecute(String result) {
            String shareLink = result.replaceFirst("https://www", "https://dl");
            webView.loadUrl(
                    "http://docs.google.com/gview?embedded=true&url=" + shareLink);
        }
    }
}
