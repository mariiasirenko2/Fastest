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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

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
     Uri uri;
    ActivityResultLauncher<Intent> sActivityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_test);

        attachQuestions = findViewById(R.id.attach_questions);
        attachStudents = findViewById(R.id.attach_students);
        fileQuestions=findViewById(R.id.file_name_questions);
        generateTest = findViewById(R.id.generateTest);
        getName = findViewById(R.id.input_test_name);

        String testName = String.valueOf(getName.getText());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





        attachQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileDialog(view);
            }
        });

        attachStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileDialog(view);
            }
        });

        sActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                             uri = data.getData();
                            fileQuestions.setText("Добавлено");


                        }
                    }
                });
        generateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadFile(uri,testName);
            }
        });


    }


    public void openFileDialog (View view){
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.addCategory(Intent.CATEGORY_OPENABLE);
        data.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
        data.putExtra(Intent.EXTRA_MIME_TYPES,mimetypes);
        data = Intent.createChooser(data, "Choose a file");
        sActivityResultLauncher.launch(data);
    }

    private  void uploadFile(Uri fileUri, String testName){
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,testName);
        File fileQ = new File(getRealPathFromURI(this,fileUri));
        RequestBody fileQuestions = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), fileQ);
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        UserApi userApi = serviceGenerator.createService(UserApi.class);

        MultipartBody.Part file =MultipartBody.Part.createFormData("questoins",fileQ.getName(),fileQuestions);

        Call<ResponseBody> call =userApi.uploadFiles(descriptionPart,file);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful())
                Toast.makeText(AddNewTest.this, "yea!!!!!!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddNewTest.this, "+", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddNewTest.this, "no(", Toast.LENGTH_SHORT).show();

            }
        }) ;
    }
    public static String getRealPathFromURI(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

}