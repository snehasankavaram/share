package com.example.james.sharedclasses;

import com.google.android.gms.wearable.DataMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by james on 12/4/15.
 */
public class GetUserRequestWrapper {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("profile")
    @Expose
    private Profile profile;

    public GetUserRequestWrapper() {

    }

    public GetUserRequestWrapper(User user, Profile profile) {
        this.user = user;
        this.profile = profile;
    }

    public GetUserRequestWrapper(DataMap dataMap) {
        this.user = new User(dataMap.getDataMap("user"));
        this.profile = new Profile(dataMap.getDataMap("profile"));
    }

    public DataMap putToDataMap(DataMap dataMap) {
        dataMap.putDataMap("user", this.user.putToDataMap(new DataMap()));
        dataMap.putDataMap("profile", this.profile.putToDataMap(new DataMap()));
        return dataMap;
    }

    /**
     *
     * @return
     * The user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     * The profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     *
     * @param profile
     * The profile
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
