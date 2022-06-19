package com.fastastapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastastapp.AddNewTest;
import com.fastastapp.R;
import com.fastastapp.adapters.TestAdapter;
import com.fastastapp.model.TestNameId;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestFragment extends Fragment {


    private static final String ARG_PARAM1 = "authToken";
    private static final String ARG_PARAM2 = "userId";

    private String token;
    private int userId;
    private RecyclerView recyclerView;

    public TestFragment() {
    }


    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(ARG_PARAM1);
            userId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);


        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recview);

        floatingActionButton.setOnClickListener(view1 -> {

            Intent intent = new Intent(getActivity(), AddNewTest.class);
            intent.putExtra("authToken", token);
            intent.putExtra("userId", userId);


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        UserApi userApi =
                ServiceGenerator.createService(UserApi.class, token);

        ArrayList<TestNameId> tests = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userApi.getTests(userId).enqueue(new Callback<List<TestNameId>>() {
            @Override
            public void onResponse(@NonNull Call<List<TestNameId>> call, @NonNull Response<List<TestNameId>> response) {
                if(response.isSuccessful()) {
                    Log.d("SUCCESS", "Successful Test Load ");

                    tests.addAll(response.body() != null ? response.body() : null);

                    recyclerView.setAdapter(new TestAdapter(tests,token,userId));

                }
                else{
                    Log.d("ERROR", "Test Load response failed. Response code: " + response.code());

                    Toast.makeText(getActivity(), "Test download failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<TestNameId>> call, @NonNull Throwable t) {
                Log.d("ERROR", "Test Load crashed");

                Toast.makeText(getActivity(),"Crashed ", Toast.LENGTH_SHORT).show();
            }
        });

        
        return  view;
    }

}