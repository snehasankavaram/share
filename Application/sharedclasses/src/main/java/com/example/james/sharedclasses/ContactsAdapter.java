package com.example.james.sharedclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sneha on 11/19/15.
 */
public class ContactsAdapter extends ArrayAdapter<Contact>{
    private static class ViewHolder {
        TextView name;
        TextView occupation;
    }

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, R.layout.contact_item, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.contact_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.occupation = (TextView) convertView.findViewById(R.id.occupation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(contact.getProfile().getName());
        viewHolder.occupation.setText(contact.getProfile().getOccupation() + "");
        // Return the completed view to render on screen
        return convertView;
    }
}
