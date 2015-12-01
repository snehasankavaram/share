package com.share;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.Note;
import com.example.james.sharedclasses.Profile;

import java.util.ArrayList;

/**
 * Created by james on 11/28/15.
 */
public class ContactsAdapter extends BaseAdapter {
    ArrayList<Contact> contactsList = new ArrayList<>();
    //create random contacts for now, but fetch contacts from backend and add to ArrayList
    String [] names = {"Sally Smith", "Bob Jones", "Dylan Christopher Johnson", "Carry George", "Jonas Thomson"};
    String [] occupations = {"CEO", "Software Engineer", "Self employed", "Artist", "Writer"};
    String [] notes = {"Sally Smith", "Bob Jones", "Dylan Christopher Johnson", "Carry George", "Jonas Thomson"};


    private Context ctx = null;

    public ContactsAdapter(Context context) {
        this.ctx = context;

        for (int i = 0; i < names.length; i++) {
            Profile p = new Profile(names[i], occupations[i]);
            Note n = new Note(notes[i]);
            Contact c = new Contact(p, n);
            contactsList.add(c);
        }
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ImageView imgView = new ImageView(this.ctx);
        imgView.setScaleType(ImageView.ScaleType.FIT_END);
        imgView.setPadding(8, 8, 8, 8);
        imgView.setImageResource(R.drawable.face4);
        imgView.setAdjustViewBounds(Boolean.TRUE);
        imgView.setContentDescription(contactsList.get(arg0).getProfile().getName());
        imgView.setMaxHeight(100);
        imgView.setMaxWidth(100);

        return imgView;
    }

    @Override
    public long getItemId(int arg0) {
       return R.drawable.face4;
    }

    @Override
    public Object getItem(int arg0) {
        return contactsList.get(arg0);
    }
}
