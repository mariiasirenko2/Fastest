package com.fastastapp.retrofit;

import com.fastastapp.model.Test;
import com.fastastapp.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi {

    @POST("/fastest/signup")
    Call<User> addNewUser(@Body User user);

    @GET("/fastest/profile")
    Call<User> logIn ();

    @GET("/fastest/profile/{idUser}/GetTests")
    Call<List<Test>>getTests(@Path("idUser") int idUser);


    @Multipart
    @POST("/profile/{idUser}/GenerateTest")
    Call<ResponseBody> uploadFiles(
            @Part("testName")RequestBody testName,
            @Part MultipartBody.Part fileQuestions
            );

}
