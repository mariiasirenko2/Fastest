package com.fastastapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fastastapp.model.User;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.textfield.TextInputEditText;


import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText regEmail, regPassword;
    private Button callSignUp, callHomePage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //delete Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        //on LogIn Button Click
        sendDataToLogin();

        //Go to Sign Up Page
        callSignUp = findViewById(R.id.redirect_signup_button);
        callSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


    }

    private void sendDataToLogin() {
        //find input data objects
        regEmail = findViewById(R.id.login_username_input);
        regPassword = findViewById(R.id.login_password_input);

        //find registration button object
        callHomePage = findViewById(R.id.login_button);


        callHomePage.setOnClickListener(view -> {

            //extract data from input objects
            String email = String.valueOf(regEmail.getText());
            String pass = String.valueOf(regPassword.getText());


            //TODO: deltete when all will be done
          //  Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);
           // startActivity(intent);

            ServiceGenerator serviceGenerator = new ServiceGenerator();
            //generate an authHeader
            UserApi loginService =
                    serviceGenerator.createService(UserApi.class, email, pass);

            //try to log in
            loginService.logIn().enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(LogInActivity.this, "Successful Login "  , Toast.LENGTH_SHORT).show();

                        User tmp = response.body();
                        //redirect to Home Page
                        Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);

                        //send token to activity
                        intent.putExtra("authToken", Credentials.basic(email, pass));
                        intent.putExtra("userId", response.body().getId());

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {

                        Toast.makeText(LogInActivity.this, "Failed. Check input data " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                    Toast.makeText(LogInActivity.this, "Something crashed", Toast.LENGTH_SHORT).show();

                    Logger.getLogger(LogInActivity.class.getName()).log(Level.SEVERE, "Error to Login. Server not response");

                }
            });

        });
    }


}
