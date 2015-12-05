package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by james on 12/5/15.
 */
public class CreateContactRequest {
    @SerializedName("my_username")
    @Expose
    private String myUserName;
    @SerializedName("contact_username")
    @Expose
    private String contactUserName;

    public CreateContactRequest() {}

    public CreateContactRequest(String my_username, String contact_username) {
        this.myUserName = my_username;
        this.contactUserName = contact_username;
    }

    public String getContactUserName() {
        return contactUserName;
    }

    public void setContactUserName(String user) {
        this.contactUserName = user;
    }

    public String getMyUsername() {
        return myUserName;
    }

    public void setMyUsername(String user) {
        this.myUserName = user;
    }

}
