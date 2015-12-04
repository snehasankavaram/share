package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 12/4/15.
 */
public class GetContactsRequestWrapper {
    @SerializedName("contacts")
    @Expose
    private List<ContactProfileWrapper> contacts = new ArrayList<ContactProfileWrapper>();

    /**
     * No args constructor for use in serialization
     *
     */
    public GetContactsRequestWrapper() {
    }

    /**
     *
     * @param contacts
     */
    public GetContactsRequestWrapper(List<ContactProfileWrapper> contacts) {
        this.contacts = contacts;
    }

    /**
     *
     * @return
     * The contacts
     */
    public List<ContactProfileWrapper> getContacts() {
        return contacts;
    }

    /**
     *
     * @param contacts
     * The contacts
     */
    public void setContacts(List<ContactProfileWrapper> contacts) {
        this.contacts = contacts;
    }
}
