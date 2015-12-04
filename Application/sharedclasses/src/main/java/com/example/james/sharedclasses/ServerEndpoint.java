package com.example.james.sharedclasses;

import retrofit.Call;
import retrofit.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by james on 12/3/15.
 */
public interface ServerEndpoint {
    @GET("/users/show")
    Call<GetUserRequestWrapper> getUser(@Query("username") String username);

    @POST("/users/create")
    Call<GetUserRequestWrapper> createUser(@Body GetUserRequestWrapper user);

    @POST("/contacts/create")
    Call<Response> createContact(@Body String my_username, @Body String contact_username);

    @GET("/contacts/index")
    Call<GetContactsRequestWrapper> getContactsForUser(@Query("username") String username);
}
