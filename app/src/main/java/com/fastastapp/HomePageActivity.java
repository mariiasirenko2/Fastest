package com.fastastapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fastastapp.databinding.ActivityHomePageBinding;
import com.fastastapp.fragment.AccountFragment;
import com.fastastapp.fragment.TestFragment;

public class HomePageActivity extends AppCompatActivity {

    ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new TestFragment());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()){
                        case R.id.home:
                            replaceFragment(new TestFragment());
                            break;
                        case R.id.photo:
                            Toast.makeText(HomePageActivity.this,"Photoshoot", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomePageActivity.this, CameraActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.p:
                           // Toast.makeText(HomePageActivity.this,"test", Toast.LENGTH_SHORT).show();
                            replaceFragment(new AccountFragment());
                            break;
                    }
                    return true;
                });

    }
    private void  replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}
