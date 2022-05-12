package com.fastastapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastastapp.databinding.ActivityHomePageBinding;
import com.fastastapp.model.Test;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new TestFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()){
                        case R.id.home:
                            replaceFragment(new TestFragment());
                            break;
                        case R.id.photo:
                            Toast.makeText(HomePageActivity.this,"Photoshoot", Toast.LENGTH_SHORT).show();
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
