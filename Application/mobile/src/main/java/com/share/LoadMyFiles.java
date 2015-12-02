package com.share;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.util.List;

/**
 * Created by james on 12/1/15.
 */
public class LoadMyFiles extends AsyncTask<Void, Long, Boolean> {
    private DropboxAPI<?> mApi;
    DropboxFilesAdapter adapter;

    List<DropboxAPI.Entry> files;

    public LoadMyFiles(DropboxAPI<?> api, DropboxFilesAdapter dbAdapter) {
        mApi = api;
        adapter = dbAdapter;
    }

    @Override
    protected void onPreExecute() {
        // start loading animation maybe?
        adapter.clear(); // clear "old" entries (optional)
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            DropboxAPI.Entry dirent =  mApi.metadata("/", 1000, null, true, null);
            if (!dirent.isDir || dirent.contents == null) {
                // It's not a directory, or there's nothing in it
                Log.d("load files", "not a directory");
                return false;
            }
            files = dirent.contents;
            return true;

        } catch (DropboxException e) {
            System.out.println("Something went wrong: " + e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // stop the loading animation or something
        if (result && files != null) {
            Log.d("Added files", "files");
            adapter.addAll(files);
        }
    }
}
