package com.fastastapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fastastapp.model.User;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText regUsername, regEmail, regPassword;
    private Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //delete Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sing_up);

        //find input data objects
        regUsername = findViewById(R.id.signup_username_input);
        regEmail = findViewById(R.id.signup_email_input);
        regPassword = findViewById(R.id.signup_password_input);


        //find registration button object
        regBtn = findViewById(R.id.registration_button);

        //on Registration Button Click
        sendDataToSignUp();


        //Go to Login Page
        Button goToLoginBtn = findViewById(R.id.go_login_button);
        goToLoginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        });


    }

    private void sendDataToSignUp() {


        regBtn.setOnClickListener(view -> {

            //extract data from input objects
            String name = String.valueOf(regUsername.getText());
            String email = String.valueOf(regEmail.getText());
            String password = String.valueOf(regPassword.getText());

            //make new user
            User user = new User(name, email, password);

            //validate input data
            if (!validateEmail(email) | !validateUsername(name) | !validatePassword(password)) {
                return;
            }

            //call retrofit to connect to server
            UserApi userApi = ServiceGenerator.createService(UserApi.class);

            //try to sign up
            userApi.addNewUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User>
                        response) {

                    if (response.isSuccessful()) {
                        Log.d("SUCCESS", "Successful signup ");

                        //redirect to welcome page
                        Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                        //send token to activity
                        intent.putExtra("authToken", Credentials.basic(email, password));
                        intent.putExtra("userId", response.body() != null ? response.body().getId() : 0);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Log.d("ERROR", "Signup response failed. Response code: " + response.code());

                        Toast.makeText(SignUpActivity.this, "Registration failed. Check input data : " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Log.d("ERROR", "Sighup crashed");

                    Toast.makeText(SignUpActivity.this, "Something crashed", Toast.LENGTH_SHORT).show();
                }
            });

        });


    }

    private Boolean validateUsername(String val) {

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

    private Boolean validateEmail(String val) {
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

    private Boolean validatePassword(String val) {
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