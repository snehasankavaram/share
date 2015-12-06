package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by james on 12/6/15.
 */
public class UpdateFileMetadataRequest {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("viewed_by")
    @Expose
    private String viewedBy;

    public UpdateFileMetadataRequest(int id, String viewedBy) {
        this.id = id;
        this.viewedBy = viewedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getViewedBy() {
        return viewedBy;
    }

    public void setViewedBy(String viewedBy) {
        this.viewedBy = viewedBy;
    }
}
