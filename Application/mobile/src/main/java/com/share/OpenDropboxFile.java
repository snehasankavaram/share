package com.share;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

/**
 * Created by james on 12/1/15.
 */
public class OpenDropboxFile extends AsyncTask<Void, Long, Boolean>{
    DropboxAPI.Entry entry;
    DropboxAPI mApi;
    Context context;
    DropboxAPI.DropboxLink link;

    public OpenDropboxFile(DropboxAPI.Entry ent, DropboxAPI api, Context context) {
        entry = ent;
        mApi = api;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            link = mApi.media(entry.path, false);
            Log.d("Link url:", link.url);
            return true;
        }
        catch (DropboxException e){
            Log.d("dropbox exception", e.toString());
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Log.d("Loading DB file", "on new activity");
            Intent i = new Intent(context, DBFileActivity.class);
            i.putExtra("url", link.url);
            i.putExtra("local_path", entry.path);
            i.putExtra("file_name", entry.fileName());
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(i);
        }
    }
}
