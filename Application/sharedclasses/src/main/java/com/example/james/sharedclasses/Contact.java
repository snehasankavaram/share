package com.example.james.sharedclasses;

import com.google.android.gms.wearable.DataMap;
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
    private String notes;

    /**
     * No args constructor for use in serialization
     *
     */
    public Contact() {

    }

    public Contact(Profile profile, String notes) {
        this.profile = profile;
        this.notes = notes;
    }

    public Contact(Profile profile) {
        this.profile = profile;
        this.notes = "";
    }

    public Contact(DataMap map) {
        this(new Profile(map.getDataMap("profile")), map.getString("notes"));
    }

    public Profile getProfile () {
        return this.profile;
    }

    public void setProfile (Profile profile) {this.profile = profile;}

    public String getNotes () {
        return this.notes;
    }

    public void setNotes (String notes) {this.notes = notes;}

    public DataMap putToDataMap(DataMap map) {
        map.putDataMap("profile", this.profile.putToDataMap(new DataMap()));
        map.putString("notes", this.notes);
        return map;
    }

}
