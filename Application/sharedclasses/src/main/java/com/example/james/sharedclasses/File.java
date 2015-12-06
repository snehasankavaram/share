package com.example.james.sharedclasses;


import com.google.android.gms.wearable.DataMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sneha on 11/19/15.
 */
public class File implements Serializable{
    @SerializedName("id")
    @Expose(serialize = false)
    private int railsID;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("view_count")
    @Expose
    private int viewCount;

    @SerializedName("local_path")
    @Expose
    private String localPath;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("file_name")
    @Expose
    private String fileName;

    public File() {

    }

    /**
     * Used for mocking purposes
     * @param fileName
     * @param createdAt
     */
    public File (String fileName, String createdAt ) {
        this.fileName = fileName;
        this.createdAt = createdAt;
    }

    public File (DataMap dataMap) {
        this.railsID = dataMap.getInt("railsID");
        this.link = dataMap.getString("link");
        this.viewCount = dataMap.getInt("viewCount");
        this.localPath = dataMap.getString("localPath");
        this.createdAt = dataMap.getString("createdAt");
        this.fileName = dataMap.getString("fileName");
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getRailsID() {
        return railsID;
    }

    public void setRailsID(int railsID) {
        this.railsID = railsID;
    }

    public DataMap putToDataMap (DataMap map) {
        map.putInt("railsID", this.railsID);
        map.putString("link", this.link);
        map.putInt("viewCount", this.viewCount);
        map.putString("localPath", this.localPath);
        map.putString("createdAt", this.createdAt);
        map.putString("fileName", this.fileName);
        return map;
    }
}
