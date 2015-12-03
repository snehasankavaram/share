package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Serializable User class
 */
public class User {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("connection_id")
    @Expose(serialize = false)
    private String connectionId;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param occupation
     * @param phone
     * @param username
     * @param email
     * @param name
     * @param password
     */
    public User(String name, String username, String password, String email, String phone, String occupation) {

        this.username = username;
        this.password = password;
        this.profile = new Profile(name, email, phone, occupation);
    }

    public User(String name, String occupation) {
        this(name, "user", "pass", "email", "phone", occupation);
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The connectionId
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     *
     * @param connectionId
     * The connection_id
     */
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }
}