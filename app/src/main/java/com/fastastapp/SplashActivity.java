package com.fastastapp;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 3000;

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //delete Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //find logo image and slogan
        image = findViewById(R.id.logo_wight_image);
        slogan = findViewById(R.id.slogan_text);

        //init animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //make animation on logo image and slogan
        image.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        //make animation on switching activities
        new Handler().postDelayed(() -> {

            Intent intent=new Intent(SplashActivity.this,LogInActivity.class);

            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,new Pair<>(image, "logo_image"));
            startActivity(intent,options.toBundle());
        },SPLASH_SCREEN);




    }
}