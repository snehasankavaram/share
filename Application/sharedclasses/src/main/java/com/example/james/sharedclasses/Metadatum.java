package com.example.james.sharedclasses;

import com.google.android.gms.wearable.DataMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by james on 12/5/15.
 */
public class Metadatum {
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("view_username")
    @Expose
    private String viewUsername;
    public Metadatum () {

    }

    public Metadatum (String viewUsername, String createdAt) {
        this.viewUsername = viewUsername;
        this.createdAt = createdAt;
    }

    public Metadatum (DataMap dataMap) {
        this.createdAt = dataMap.getString("createdAt");
        this.viewUsername = dataMap.getString("viewUsername");
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getViewUsername() {
        return viewUsername;
    }

    public void setViewUsername(String viewUsername) {
        this.viewUsername = viewUsername;
    }

    public DataMap putToDataMap(DataMap dataMap) {
        dataMap.putString("createdAt", this.createdAt);
        dataMap.putString("viewUsername", this.viewUsername);
        return dataMap;
    }
}
