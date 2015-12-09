package com.example.james.sharedclasses;

import com.google.android.gms.wearable.DataMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
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
     * @param username
     * @param password
     */
    public User(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public User(DataMap map) {
        this.username = map.getString("user");
        this.password = map.getString("password");
    }

    public DataMap putToDataMap(DataMap map) {
        map.putString("user", this.username);
        map.putString("password", this.password);
        return map;
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