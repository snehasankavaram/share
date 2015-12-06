package com.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.james.sharedclasses.Metadatum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by james on 12/6/15.
 */
public class MetadataAdapter extends ArrayAdapter<Metadatum> {
    private static final String TAG = "MetadataAdapter";
    private static class ViewHolder {
        TextView user;
        TextView viewed_at;
    }

    public MetadataAdapter(Context context, ArrayList<Metadatum> files) {
        super(context, R.layout.metadata, files);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.metadata, parent, false);
            viewHolder.viewed_at = (TextView) convertView.findViewById(R.id.viewed_at);
            viewHolder.user = (TextView) convertView.findViewById(R.id.user);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Metadatum metadatum = getItem(position);
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try{
            Date date = isoFormat.parse(metadatum.getCreatedAt());
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            viewHolder.viewed_at.setText(outputDateFormat.format(date));
        }
        catch (ParseException e) {
            viewHolder.viewed_at.setText(metadatum.getCreatedAt());
        }

        viewHolder.user.setText(metadatum.getViewUsername());

        return convertView;
    }
}
