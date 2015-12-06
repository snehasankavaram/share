package com.example.james.sharedclasses;

import retrofit.Call;
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
    Call<ContactProfileWrapper> createContact(@Body CreateContactRequest request);

    @GET("/contacts/index")
    Call<GetContactsRequestWrapper> getContactsForUser(@Query("username") String username);

    @GET("/db_files/index")
    Call<GetFilesRequestWrapper> getFilesForUser(@Query("username") String username);

    @POST("/db_files/create")
    Call<GetFilesRequestWrapper> createFileForUser(@Body CreateFileRequest request);

    @POST("/db_files/update")
    Call<GetFilesRequestWrapper> updateMetadataForFile(@Body UpdateFileMetadataRequest request);

}
