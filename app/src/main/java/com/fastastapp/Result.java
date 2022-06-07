package com.fastastapp;

import static org.opencv.android.Utils.bitmapToMat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fastastapp.PhotoAnalayzer.Scanner;

import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;

import java.io.IOException;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String uri = getIntent().getStringExtra("uri");
        Uri myUri = Uri.parse(uri);
        Mat source = new Mat() ;
        Bitmap bitmap = null,bitmap1=null;

        try {
             bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);


        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView image = findViewById(R.id.imageView2);
        image.setImageBitmap(bitmap);

        TextView t = findViewById(R.id.textView);
        Scanner s = new Scanner(source,20);


        bitmapToMat(bitmap,source);

        s.setLogging(false);
        try {
            bitmap1 = s.scan();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView image2 = findViewById(R.id.imageView);
        image2.setImageBitmap(bitmap1);

        t.setText(s.detectQRCode());
    }
}