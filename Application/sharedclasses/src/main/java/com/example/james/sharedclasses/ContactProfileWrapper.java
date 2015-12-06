package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by james on 12/4/15.
 */
public class ContactProfileWrapper {
    @SerializedName("contact")
    @Expose
    private Contact contact;
    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("files")
    @Expose
    private ArrayList<File> files;

    /**
     * No args constructor for use in serialization
     *
     */
    public ContactProfileWrapper() {
    }

    /**
     *
     * @param contact
     * @param profile
     * @param files
     */
    public ContactProfileWrapper(Contact contact, Profile profile, ArrayList<File> files) {
        this.contact = contact;
        this.profile = profile;
        this.files = files;
    }

    /**
     *
     * @return
     * The contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     *
     * @param contact
     * The contact
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     *
     * @return
     * The profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     *
     * @param profile
     * The profile
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
}
