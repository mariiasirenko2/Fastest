package com.fastastapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastastapp.adapters.VariantAdapter;
import com.fastastapp.model.Variant;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VariantsActivity extends AppCompatActivity {
    private List<Variant> variants;
    private UserApi api;
    private int idUser, idTest;
    private String testName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data);

        //toolbar config
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        //get intent data
        testName = getIntent().getStringExtra("testName");
        idTest = getIntent().getIntExtra("idTest", 0);
        String token = getIntent().getStringExtra("token");
        idUser = getIntent().getIntExtra("idUser", 0);

        //set test name
        TextView text = (TextView) findViewById(R.id.test_name);
        text.setText(testName);


        api = ServiceGenerator.createService(UserApi.class, token);

        //get variants from DB and call adapter
        loadVariants();

        //on btn click get files with questions and blanks for answers
        Button downloadBlanks = findViewById(R.id.download_tasks);
        downloadBlanks.setOnClickListener(view -> loadVariantFiles());

        Button downloadResults = findViewById(R.id.download_results);
        downloadResults.setOnClickListener(view -> loadResults());


    }

    private void loadVariants() {


        api.getVariantsOfTest(idUser, idTest).enqueue(new Callback<List<Variant>>() {
            @Override
            public void onResponse(@NonNull Call<List<Variant>> call, @NonNull Response<List<Variant>> response) {

                if (response.isSuccessful()) {
                    variants = new ArrayList<>();
                    variants.addAll(Objects.requireNonNull(response.body()));

                    //set variants
                    RecyclerView rv = findViewById(R.id.recview_variant);
                    rv.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(VariantsActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv.setLayoutManager(layoutManager);

                    rv.setAdapter(new VariantAdapter(variants));

                } else {
                    Toast.makeText(VariantsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Variant>> call, @NonNull Throwable t) {
                Toast.makeText(VariantsActivity.this, "Crashed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadVariantFiles() {

        Toast.makeText(VariantsActivity.this, "Please wait a few seconds. Files are downloading....", Toast.LENGTH_LONG).show();

        api.getBlanks(idUser, idTest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    //create a file in specified directory
                    File file = new File(getExternalFilesDir(null) + File.separator + testName + File.separator + testName + "Blanks.docx");

                    //try to fill file with data
                    try {
                        FileUtils.writeByteArrayToFile(file, Objects.requireNonNull(response.body()).bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(VariantsActivity.this, "Blank download pass successful", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(VariantsActivity.this, "Failed" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(VariantsActivity.this, "Crashed", Toast.LENGTH_SHORT).show();

            }
        });

        api.getVariants(idUser, idTest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    //create a file in specified directory
                    File file = new File(getExternalFilesDir(null) + File.separator + testName + File.separator + testName + "Questions.docx");

                    //try to fill file with data
                    try {
                        assert response.body() != null;
                        FileUtils.writeByteArrayToFile(file, response.body().bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(VariantsActivity.this, "Variants download pass successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VariantsActivity.this, "Failed" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(VariantsActivity.this, "Crashed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadResults() {
        api.getResults(idUser, idTest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    //create a file in specified directory
                    File file = new File(getExternalFilesDir(null) + File.separator + testName + File.separator + testName + "Marks.docx");

                    //try to fill file with data
                    try {
                        FileUtils.writeByteArrayToFile(file, Objects.requireNonNull(response.body()).bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(VariantsActivity.this, "Marks download pass successful", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(VariantsActivity.this, "Failed" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(VariantsActivity.this, "Crashed", Toast.LENGTH_SHORT).show();

            }
        });
    }
}

