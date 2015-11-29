package com.example.james.sharedclasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sneha on 11/19/15.
 */
public class Contact implements Serializable {


    private Profile profile;
    private ArrayList<Note> notes;
    private ArrayList <File> files;

    public Contact(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile () {
        return this.profile;
    }


}
