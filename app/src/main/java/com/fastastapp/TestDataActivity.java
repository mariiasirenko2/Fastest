package com.fastastapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fastastapp.adapters.VariantAdapter;
import com.fastastapp.model.User;
import com.fastastapp.model.Variant;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestDataActivity extends AppCompatActivity {
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data);

        text=(TextView) findViewById(R.id.test_name);
        text.setText(getIntent().getStringExtra("text"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        List<Variant> variants= new ArrayList<>();
        variants.add(new Variant("Вариант 1",10));
        variants.add(new Variant("Вариант 2",9));
        variants.add(new Variant("Вариант 3",10));
        variants.add(new Variant("Вариант 4",7));
        variants.add(new Variant("Вариант 5",10));
        variants.add(new Variant("Вариант 6",3));
        variants.add(new Variant("Вариант 7",1));


        ServiceGenerator serviceGenerator = new ServiceGenerator();
        UserApi loginService =
                ServiceGenerator.createService(UserApi.class, "m1@gmail.com", "qwe1@rty");

        loginService.logIn().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){

                }
                else{
                    Toast.makeText(TestDataActivity.this, "Failed. Check input data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(TestDataActivity.this,"Something crashed 1", Toast.LENGTH_SHORT).show();
                Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "Error occurred");
            }
        });



        RecyclerView rv = findViewById(R.id.recview_variant);
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        rv.setAdapter(new VariantAdapter(variants));

    }
}