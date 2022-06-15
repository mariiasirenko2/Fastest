package com.fastastapp.retrofit;

import com.fastastapp.model.Chars;
import com.fastastapp.model.Test;
import com.fastastapp.model.TestNameId;
import com.fastastapp.model.User;
import com.fastastapp.model.Variant;

import java.io.InputStream;
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
    Call<User> logIn();

    @GET("/fastest/profile/{idUser}/GetTests")
    Call<List<TestNameId>> getTests(@Path("idUser") int idUser);


    @Multipart
    @POST("/fastest/profile/{idUser}/GenerateTest")
    Call<Test> uploadFiles(
            @Part("testName") String testName,
            @Part MultipartBody.Part fileQuestions,
            @Part MultipartBody.Part fileStudents,
            @Path("idUser") int idUser
    );


    @GET("/fastest/profile/{idUser}/Tests/{idTest}/V")
    Call<List<Variant>> getVariantsOfTest(@Path(value = "idUser") int idUser,
                                          @Path(value = "idTest") int idTest);


    @GET("/fastest/profile/{idUser}/Variant/{idVariant}/T")
    Call<List<Chars>> getAnswer(@Path(value = "idUser") int idUser,
                                @Path(value = "idVariant") int idVariant);

    @GET("/fastest/profile/{idUser}/Tests/{idTest}/Blanks")
    Call<ResponseBody> getBlanks(@Path(value = "idUser") int idUser,
                                 @Path(value = "idTest") int idTest);

    @GET("/fastest/profile/{idUser}/Tests/{idTest}/Documents")
    Call<ResponseBody> getVariants(@Path(value = "idUser") int idUser,
                                   @Path(value = "idTest") int idTest);

    @POST("/fastest/profile/{idUser}/Variant/{idVariant}")
    Call<ResponseBody> setMarkToVariant(@Path(value = "idUser") int idUser,
                                        @Path(value = "idVariant") int idVariant,
                                        @Body int mark);


    @GET("/fastest/profile/{idUser}/Tests/{idTest}/Results")
    Call<ResponseBody> getResults(@Path(value = "idUser") int idUser,
                            @Path(value = "idTest") int idTest);

}
