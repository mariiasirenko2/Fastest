package com.fastastapp;

import static org.opencv.android.Utils.bitmapToMat;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fastastapp.PhotoAnalayzer.Scanner;
import com.fastastapp.model.Chars;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;

import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Result extends AppCompatActivity {
    private Scanner scanner;
    private int variantNumber;
    private UserApi userApi;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //toolbar configs
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        //get intent data
        String uri = getIntent().getStringExtra("uri");
        String token = getIntent().getStringExtra("authToken");
        idUser = getIntent().getIntExtra("userId", 0);

        userApi = ServiceGenerator.createService(UserApi.class, token);

        //find file by uri and try to save it in bitmap
        Uri myUri = Uri.parse(uri);

        //try to get taken photo
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //make a OpenCV entity to store image
        Mat source = new Mat();
        bitmapToMat(bitmap, source);

        scanner = new Scanner(source);
        String qr = scanner.detectQRCode();


        if (qr.isEmpty()) variantNumber = 0;
        else variantNumber = Integer.parseInt(qr);


        List<Chars> answers = new ArrayList<>();

        userApi.getAnswer(idUser, variantNumber).enqueue(new Callback<List<Chars>>() {
            @Override
            public void onResponse(Call<List<Chars>> call, Response<List<Chars>> response) {
                if (response.isSuccessful()) {
                    //get right answers
                    answers.addAll(response.body());

                    //pass them to scanner
                    scanner.addAnswers(answers);

                    //try to get processed image
                    Bitmap processed = null;
                    try {
                        processed = scanner.scan();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set processed image and mark to layout
                    ImageView image2 = findViewById(R.id.imageView);
                    image2.setImageBitmap(processed);

                    TextView text = findViewById(R.id.textView);
                    text.setText("Оцінка: " + scanner.getMark());


                    //send mark to server
                    setMarkToVariant();


                } else {
                    Toast.makeText(Result.this, "Failed", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<Chars>> call, Throwable t) {
                Toast.makeText(Result.this, "Crashed", Toast.LENGTH_LONG).show();

            }
        });



    }



    public void setMarkToVariant() {
        userApi.setMarkToVariant(idUser, variantNumber, scanner.getMark()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                } else {
                    Toast.makeText(Result.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Result.this, "Crashed", Toast.LENGTH_LONG).show();

            }
        });
    }
}