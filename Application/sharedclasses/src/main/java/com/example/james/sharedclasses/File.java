package com.example.james.sharedclasses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sneha on 11/19/15.
 */
public class File {
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


}
