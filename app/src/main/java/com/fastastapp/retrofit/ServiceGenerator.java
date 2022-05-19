package com.fastastapp.retrofit;


import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fastastapp.SignUpActivity;
import com.google.gson.Gson;

import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "";//http://your_IP:tomcat_port  ex: http://111.111.11.11:8080

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()));
    private static Retrofit retrofit = builder.build();



    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            Log.i("INFO -------------"," Token: "+ authToken);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient
                        .build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }
}
