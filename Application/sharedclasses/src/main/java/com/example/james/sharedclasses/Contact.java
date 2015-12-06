package com.example.james.sharedclasses;

import com.google.android.gms.wearable.DataMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

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

    @SerializedName("files")
    @Expose
    private ArrayList<File> files;

    /**
     * No args constructor for use in serialization
     *
     */
    public Contact() {

    }


    public Contact(Profile profile, String notes, ArrayList<File> files) {
        this.profile = profile;
        this.notes = notes;
        this.files = files;
    }

    public Contact(Profile profile) {
        this.profile = profile;
        this.notes = "";
    }

    public Contact(DataMap map) {
        this.profile = new Profile(map.getDataMap("profile"));
        this.notes = map.getString("notes");
        ArrayList<File> files = new ArrayList<>();
        ArrayList<DataMap> filesDataMap = map.getDataMapArrayList("files");
        for (DataMap dataMap : filesDataMap) {
            files.add(new File(dataMap));
        }
        this.files = files;
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
        ArrayList<DataMap> filesDataMap = new ArrayList<>();
        for (File f : this.files) {
            filesDataMap.add(f.putToDataMap(new DataMap()));
        }
        map.putDataMapArrayList("files", filesDataMap);
        return map;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

}
