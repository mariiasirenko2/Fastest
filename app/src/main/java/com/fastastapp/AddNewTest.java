package com.fastastapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fastastapp.model.Test;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewTest extends AppCompatActivity {

    private TextView fileQuestions, fileStudents;
    private TextInputEditText getName;
    private Uri uriQ, uriS;
    private UserApi api;
    private int userId;
    private String token;
    private ActivityResultLauncher<Intent> activityResultLauncherQ, activityResultLauncherS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_test);

        // toolbar config
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        Button attachQuestions = findViewById(R.id.attach_questions);
        Button attachStudents = findViewById(R.id.attach_students);
        fileQuestions = findViewById(R.id.file_name_questions);
        fileStudents = findViewById(R.id.file_name_students);

        Button generateTest = findViewById(R.id.generateTest);


        token = getIntent().getStringExtra("authToken");
        userId = getIntent().getIntExtra("userId", 0);


        api = ServiceGenerator.createService(UserApi.class, token);


        attachQuestions.setOnClickListener(view -> openFileDialog(view, activityResultLauncherQ));

        attachStudents.setOnClickListener(view -> openFileDialog(view, activityResultLauncherS));

        activityResultLauncherQ = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uriQ = data.getData();
                        fileQuestions.setText("Добавлено");
                        Log.d("SUCCESS", "Question File attached");

                    }
                });

        activityResultLauncherS = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uriS = data.getData();
                        fileStudents.setText("Добавлено");
                        Log.d("SUCCESS", "Student List File attached");

                    }
                });

        generateTest.setOnClickListener(view -> {
            getName = findViewById(R.id.input_test_name);
            String testName = String.valueOf(getName.getText());
            uploadFile(uriQ, uriS, testName);


        });


    }


    public void openFileDialog(View view, ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.addCategory(Intent.CATEGORY_OPENABLE);
        data.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
        data.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        data = Intent.createChooser(data, "Choose a file");
        activityResultLauncher.launch(data);
    }

    private void uploadFile(Uri fileUri, Uri fileUriS, String testName) {

        File fileQ = new File(getRealPathFromUri(fileUri.getPath()));
        RequestBody fileQuestions = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), fileQ);

        MultipartBody.Part file = MultipartBody.Part.createFormData("questionFile", fileQ.getName(), fileQuestions);

        File fileSt = new File(getRealPathFromUri(fileUriS.getPath()));
        RequestBody fileStudents = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUriS)), fileSt);

        MultipartBody.Part fileS = MultipartBody.Part.createFormData("studentFile", fileSt.getName(), fileStudents);

        api.uploadFiles(testName, file, fileS, userId).enqueue(new Callback<Test>() {


            @Override
            public void onResponse(Call<Test> call, Response<Test> response) {
                if (response.isSuccessful()) {
                    Log.d("SUCCESS", "Files send");

                    Toast.makeText(AddNewTest.this, "Wait a minute. We are generating your test. It can take some time", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddNewTest.this, HomePageActivity.class);

                    //send token to activity
                    intent.putExtra("authToken", token);
                    intent.putExtra("userId", userId);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    Log.d("ERROR", "Files send failed"+response.code());

                    Toast.makeText(AddNewTest.this, "Failed" + response.code(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Test> call, Throwable t) {
                Toast.makeText(AddNewTest.this, "Wait a minute. We are generating your test. It can take some time", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddNewTest.this, HomePageActivity.class);

                //send token to activity
                intent.putExtra("authToken", token);
                intent.putExtra("userId", userId);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


    }

    private static String getRealPathFromUri(String uri) {
        return uri.substring(uri.indexOf(":") + 1).trim();
    }

}