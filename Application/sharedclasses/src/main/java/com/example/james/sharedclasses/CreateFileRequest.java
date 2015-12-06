package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by james on 12/5/15.
 */
public class CreateFileRequest {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("local_path")
    @Expose
    private String localPath;

    @SerializedName("file_name")
    @Expose
    private String fileName;

    public CreateFileRequest() {

    }

    public CreateFileRequest(String username, String link, String localPath, String fileName) {
        this.username = username;
        this.link = link;
        this.localPath = localPath;
        this.fileName = fileName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
