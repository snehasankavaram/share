package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sneha on 11/19/15.
 */
public class Contact implements Serializable {
    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("notes")
    @Expose
    private Note notes;

    /**
     * No args constructor for use in serialization
     *
     */
    public Contact() {

    }

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
