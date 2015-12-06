package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 12/5/15.
 */
public class FileMetadataWrapper {
    @SerializedName("file")
    @Expose
    private File file;

    @SerializedName("metadata")
    @Expose
    private List<Metadatum> metadata = new ArrayList<>();

    public FileMetadataWrapper() {

    }

    public FileMetadataWrapper(File file, List<Metadatum> metadata) {
        this.file = file;
        this.metadata = metadata;
    }

    public List<Metadatum> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadatum> metadata) {
        this.metadata = metadata;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
