package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
     */
    public ContactProfileWrapper(Contact contact, Profile profile) {
        this.contact = contact;
        this.profile = profile;
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
}
