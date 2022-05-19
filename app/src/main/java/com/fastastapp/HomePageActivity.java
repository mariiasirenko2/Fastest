package com.fastastapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fastastapp.databinding.ActivityHomePageBinding;
import com.fastastapp.fragment.AccountFragment;
import com.fastastapp.fragment.TestFragment;
import com.fastastapp.model.User;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {

    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up binding
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());

        //get AuthToken
        String token = getIntent().getStringExtra("authToken");
        int userId = getIntent().getIntExtra("userId",0);

        Bundle bundle = new Bundle();
        bundle.putString("authToken", token );
        bundle.putInt("userId",userId);

        //place starter fragment - Test
        setContentView(binding.getRoot());
        TestFragment testFragment = new TestFragment();
        testFragment.setArguments(bundle);
        replaceFragment(testFragment);

        //toolbar configs
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());




       binding.bottomNavigationView.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()){
                        case R.id.home:

                            TestFragment testFragmentNew = new TestFragment();
                            testFragmentNew.setArguments(bundle);
                            replaceFragment(testFragmentNew);
                            break;
                        case R.id.photo:
                            Intent intent = new Intent(HomePageActivity.this, CameraActivity.class);
                            intent.putExtra("authToken", token);
                            intent.putExtra("userId", userId);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case R.id.p:

                            AccountFragment accountFragment = new AccountFragment();
                            accountFragment.setArguments(bundle);
                            replaceFragment(accountFragment);
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
