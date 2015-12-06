package com.share;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.example.james.sharedclasses.LoginUtils;

import java.util.ArrayList;

import retrofit.Retrofit;

/**
 * Created by james on 12/1/15.
 */
public class DropboxFilesAdapter extends ArrayAdapter<DropboxAPI.Entry> {
    private static final String TAG = "DropboxFilesAdapter";
    private static class ViewHolder {
        TextView file_name;
        ImageView imageView;
        ImageButton button;
    }

    private Context context;
    private Retrofit retrofit;
    private DropboxAPI mApi;
    public DropboxFilesAdapter(Context context, ArrayList<DropboxAPI.Entry> files, Retrofit retrofit, DropboxAPI mApi) {
        super(context, R.layout.dropbox_file, files);
        this.context = context;
        this.retrofit = retrofit;
        this.mApi = mApi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.dropbox_file, parent, false);
            viewHolder.file_name = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.button = (ImageButton) convertView.findViewById(R.id.imageButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DropboxAPI.Entry e = getItem(position);
        viewHolder.file_name.setText(e.fileName());
        String uri = "@drawable/" + e.icon + "48";
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        if (imageResource != 0) {
            Log.d("Image resource", uri+ " not found");
            Drawable image = ContextCompat.getDrawable(context, imageResource);
            viewHolder.imageView.setImageDrawable(image);
        }
        final AlertDialog dialog = createDialog(e);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        return convertView;
    }

    private AlertDialog createDialog(final DropboxAPI.Entry entry) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);

        ListView listView = (ListView) dialogView.findViewById(R.id.listView);
        TextView textView = (TextView) dialogView.findViewById(R.id.textView);
        builder.setView(dialogView);

        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String username = LoginUtils.getLoginToken(getContext());
                        Log.d(TAG, "Username: " + username);
                        CreateSharedLinkTask task = new CreateSharedLinkTask(retrofit, mApi, entry.path, username, entry.fileName());
                        task.execute();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
