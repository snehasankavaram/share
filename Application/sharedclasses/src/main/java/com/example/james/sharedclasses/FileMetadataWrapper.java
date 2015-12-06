package com.example.james.sharedclasses;

import com.google.android.gms.wearable.DataMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by james on 12/5/15.
 */
public class FileMetadataWrapper {
    @SerializedName("file")
    @Expose
    private File file;

    @SerializedName("metadata")
    @Expose
    private ArrayList<Metadatum> metadata = new ArrayList<>();

    public FileMetadataWrapper() {

    }

    public FileMetadataWrapper(File file, ArrayList<Metadatum> metadata) {
        this.file = file;
        this.metadata = metadata;
    }

    public FileMetadataWrapper(DataMap dataMap) {
        this.file = new File(dataMap.getDataMap("file"));
        this.metadata = new ArrayList<>();
        for (DataMap map : dataMap.getDataMapArrayList("metadata")) {
            this.metadata.add(new Metadatum(map));
        }
    }

    public ArrayList<Metadatum> getMetadata() {
        return metadata;
    }

    public void setMetadata(ArrayList<Metadatum> metadata) {
        this.metadata = metadata;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public DataMap putToDataMap(DataMap dataMap) {
        dataMap.putDataMap("file", this.file.putToDataMap(new DataMap()));
        ArrayList<DataMap> metadataMap = new ArrayList<>();
        for (Metadatum datum : this.metadata) {
            metadataMap.add(datum.putToDataMap(new DataMap()));
        }
        dataMap.putDataMapArrayList("metadata", metadataMap);
        return dataMap;
    }

}
