package com.example.james.sharedclasses;


/**
 * Created by Sneha on 11/19/15.
 */
public class File {
    private String createdAt;

    private String link;
    //TODO: Sneha- add filename to backend
    private String name;
    private int viewCount;

    //more fields for metadata when added to backend

    public File (String name, String createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }


}
