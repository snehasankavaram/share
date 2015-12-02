package com.share;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;

import java.util.ArrayList;

/**
 * Created by james on 12/1/15.
 */
public class DropboxFilesAdapter extends ArrayAdapter<DropboxAPI.Entry>{
    private static class ViewHolder {
        TextView file_name;
        ImageView imageView;
    }

    private Context context;
    public DropboxFilesAdapter(Context context, ArrayList<DropboxAPI.Entry> files) {
        super(context, R.layout.dropbox_file, files);
        this.context = context;
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

        return convertView;
    }
}
