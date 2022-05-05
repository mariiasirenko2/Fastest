package com.fastastapp.retrofit;

import com.fastastapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @POST("/fastest/signup")
    Call<User> addNewUser(@Body User user);

    @GET("/fastest/profile")
    Call<User> logIn ();

}
