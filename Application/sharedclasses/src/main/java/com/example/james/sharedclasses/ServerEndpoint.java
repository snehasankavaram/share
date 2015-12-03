package com.example.james.sharedclasses;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by james on 12/3/15.
 */
public interface ServerEndpoint {
    @GET("/users/show/{username}")
    Call<User> getUser(@Path("username") String username);

    @POST("/users/create")
    Call<User> createUser(@Body User user);
}
