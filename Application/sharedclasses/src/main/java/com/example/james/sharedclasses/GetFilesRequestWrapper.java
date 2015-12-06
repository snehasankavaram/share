package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 12/5/15.
 */
public class GetFilesRequestWrapper {
    @SerializedName("files")
    @Expose
    private List<FileMetadataWrapper> files = new ArrayList<>();

    /**
     * No args constructor for use in serialization
     *
     */
    public GetFilesRequestWrapper() {

    }

    public GetFilesRequestWrapper(List<FileMetadataWrapper> files) {
        this.files = files;
    }

    public List<FileMetadataWrapper> getFiles() {
        return files;
    }

    public void setFiles(List<FileMetadataWrapper> files) {
        this.files = files;
    }

}
