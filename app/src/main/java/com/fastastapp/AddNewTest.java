package com.fastastapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fastastapp.model.User;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewTest extends AppCompatActivity {

    Button attachQuestions, attachStudents, generateTest;
    TextView fileQuestions, fileStudents;
     TextInputEditText getName;
     Uri uriQ,uriS;
    UserApi api;
    int userId;
    ActivityResultLauncher<Intent> activityResultLauncherQ,activityResultLauncherS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_test);

        attachQuestions = findViewById(R.id.attach_questions);
        attachStudents = findViewById(R.id.attach_students);
        fileQuestions=findViewById(R.id.file_name_questions);
        fileStudents=findViewById(R.id.file_name_students);

        generateTest = findViewById(R.id.generateTest);
        getName = findViewById(R.id.input_test_name);

        String testName = String.valueOf(getName.getText());

        String token = getIntent().getStringExtra("authToken");
        userId = getIntent().getIntExtra("userId",0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

         api =
                ServiceGenerator.createService(UserApi.class,token);







        attachQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileDialog(view,activityResultLauncherQ);
            }
        });

        attachStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileDialog(view,activityResultLauncherS);
            }
        });

        activityResultLauncherQ = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                             uriQ = data.getData();
                            fileQuestions.setText("Добавлено");


                        }
                    }
                });
        activityResultLauncherS = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uriS = data.getData();
                            fileStudents.setText("Добавлено");


                        }
                    }
                });
        generateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile(uriQ,uriS,testName);

            //    Toast.makeText(AddNewTest.this, "UserId = "+ userId, Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void openFileDialog (View view, ActivityResultLauncher<Intent> activityResultLauncher){
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.addCategory(Intent.CATEGORY_OPENABLE);
        data.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
        data.putExtra(Intent.EXTRA_MIME_TYPES,mimetypes);
        data = Intent.createChooser(data, "Choose a file");
        activityResultLauncher.launch(data);
    }

    private  void uploadFile(Uri fileUri,Uri fileUriS, String testName){

       // RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"),testName);
        File fileQ = new File(getRealPathFromUri(fileUri.getPath()));
        RequestBody fileQuestions = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), fileQ);
        //ServiceGenerator serviceGenerator = new ServiceGenerator();

        MultipartBody.Part file =MultipartBody.Part.createFormData("questionFile",fileQ.getName(),fileQuestions);


        File fileSt = new File(getRealPathFromUri(fileUriS.getPath()));
        RequestBody fileStudents = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUriS)), fileSt);

        MultipartBody.Part fileS =MultipartBody.Part.createFormData("studentFile",fileSt.getName(),fileStudents);
        api.uploadFiles(testName,file,fileS,userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    Toast.makeText(AddNewTest.this, "yea!!!!!!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddNewTest.this, HomePageActivity.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);

                } else
                    Toast.makeText(AddNewTest.this, "+ "+ response.code() , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddNewTest.this, "no(  " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) ;

    }
    public static String getRealPathFromUri(String uri) {


        /*String[] filePathColumn = { MediaStore.Files.FileColumns.DATA };
        String picturePath = "hello";
        Cursor cursor = ctx.getContentResolver().query(uri, filePathColumn,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             picturePath = cursor.getString(columnIndex);
            Log.e("", "picturePath : " + picturePath);
            cursor.close();
        }*/
        return uri.substring(uri.indexOf(":") + 1).trim();
    }

}