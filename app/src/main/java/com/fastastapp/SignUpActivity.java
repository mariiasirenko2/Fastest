package com.fastastapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fastastapp.model.User;
import com.fastastapp.retrofit.RetrofitService;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.textfield.TextInputEditText;


import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText regUsername, regEmail, regPassword;

    Button regBtn, goToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //delete Top Bar

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sing_up);

        //on Registration Button Click
        sendDataToSignUp();


        //Go to Login Page
        goToLoginBtn = findViewById(R.id.go_login_button);
        goToLoginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        });


    }

    private void sendDataToSignUp() {
        //find input data objects
        regUsername =findViewById(R.id.signup_username_input);
         regEmail =findViewById(R.id.signup_email_input);
         regPassword =findViewById(R.id.signup_password_input);

        //call retrofit to connect to server

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        //find registration button object
        regBtn = findViewById(R.id.registration_button);
        regBtn.setOnClickListener(view -> {

            //extract data from input objects
            String name = String.valueOf(regUsername.getText());
            String email = String.valueOf(regEmail.getText());
            String password = String.valueOf(regPassword.getText());

            //make new user
            User user = new User(name,email,password);

            //validate input data
            if (!validateEmail() | !validateUsername() | !validatePassword()) {
                return;
            }

            userApi.addNewUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User>
                        response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Successful account creation", Toast.LENGTH_SHORT).show();

                        //redirect to welcome page
                        Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Toast.makeText(SignUpActivity.this,"Something crashed", Toast.LENGTH_SHORT).show();
                    Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "Error occurred");
                }
            });

        });


    }

    private Boolean validateUsername() {
        String val = String.valueOf(regUsername.getText());

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 20) {
            regUsername.setError("Username too long");
            return false;
        } else {
            regUsername.setError(null);
            return true;
        }
    }
    private Boolean validateEmail() {
        String val = String.valueOf(regEmail.getText());
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = String.valueOf(regPassword.getText());
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            return true;
        }
    }


}