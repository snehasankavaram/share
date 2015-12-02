package com.example.james.sharedclasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sneha on 11/19/15.
 */
public class Contact implements Serializable {


    private Profile profile;
    private Note notes;

    public Contact(Profile profile, Note notes) {
        this.profile = profile;
        this.notes = notes;
    }

    public Contact(Profile profile) {
        this.profile = profile;
        this.notes = new Note("");
    }

    public Profile getProfile () {
        return this.profile;
    }

    public Note getNotes () {
        return this.notes;
    }


}
