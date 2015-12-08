package com.example.james.sharedclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 12/7/15.
 */
public class GetConnectionEstablishedWrapper {
    @SerializedName("connection")
    @Expose
    private List<Connection> connection = new ArrayList<Connection>();

    public GetConnectionEstablishedWrapper() {

    }

    public List<Connection> getConnection() {
        return connection;
    }

    public void setConnection(List<Connection> connection) {
        this.connection = connection;
    }
}
