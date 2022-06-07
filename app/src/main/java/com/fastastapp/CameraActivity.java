package com.fastastapp;

import static org.opencv.android.Utils.bitmapToMat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.fastastapp.PhotoAnalayzer.Scanner;
import com.google.common.util.concurrent.ListenableFuture;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraActivity extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;


    private static final int PERMISSION_REQUEST_CAMERA = 111;
    private static final int PERMISSION_REQUEST_WRITE_EX_STORAGE = 121;

    private Button takePhoto;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private Uri uri;

    static {
        if (!OpenCVLoader.initDebug())
            Log.d("ERROR", "Unable to load OpenCV");
        else
            Log.d("SUCCESS", "OpenCV loaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //set  toolbar configurations
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        //get AuthToken from intent
        String token = getIntent().getStringExtra("authToken");

        takePhoto = findViewById(R.id.bCapture);
        previewView = findViewById(R.id.previewView);



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CAMERA);
        }
        else {
            Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show();

        }

       if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_WRITE_EX_STORAGE);
        }

        takePhoto.setOnClickListener(view -> capturePhoto());

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
}
    private void capturePhoto() {

      long timestamp = System.currentTimeMillis();

      ContentValues contentValues = new ContentValues();
      contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,timestamp);
      contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");

      /*  imageCapture.takePicture(getExecutor(), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                Toast toast = Toast.makeText(CameraActivity.this,"Sucseed image capture",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                image.close();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast toast = Toast.makeText(CameraActivity.this,"Error image capture",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                super.onError(exception);
            }
        });*/


        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        uri = outputFileResults.getSavedUri();
                        Intent intent = new Intent(CameraActivity.this, Result.class);

                       intent.putExtra("uri", uri.toString());
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(intent);
                        //TODO: get file from uri not work . source is empty!!!! FIX
                        Mat source = new Mat() ;

                        try {
                            Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            bitmapToMat(bitmap,source);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast toast;
                        if(source.empty())   toast = Toast.makeText(CameraActivity.this,"----" ,Toast.LENGTH_LONG);
                    else {
                            Scanner scanner = new Scanner(source, 20);

                            QRCodeDetector qrCodeDetector = new QRCodeDetector();
                            String qr = qrCodeDetector.detectAndDecode(source);
                            String qr2 = scanner.detectQRCode();
                             toast = Toast.makeText(CameraActivity.this, "Succeed image capture: QRCode  "+qr+"   "+ qr2 , Toast.LENGTH_LONG);

                             Log.i("QR",qr);
                             Log.i("QR",qr2);
                        }
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast toast = Toast.makeText(CameraActivity.this,"Error image capture " + exception.getMessage(),Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                });

    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setTargetResolution(new Size(720,1280))
                .build();

        //bind to lifecycle:
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);

    }
    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}