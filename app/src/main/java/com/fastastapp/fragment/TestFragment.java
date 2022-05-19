package com.fastastapp.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.fastastapp.model.Test;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "authToken";
    private static final String ARG_PARAM2 = "userId";

    // TODO: Rename and change types of parameters
    private String token;
    private int userId;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    public TestFragment() {
        // Required empty public constructor
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


        floatingActionButton = view.findViewById(R.id.fab);
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

        ArrayList<Test> tests = new ArrayList<>();
        //tests.add(new Test("Test test"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userApi.getTests(userId).enqueue(new Callback<List<Test>>() {
            @Override
            public void onResponse(@NonNull Call<List<Test>> call, @NonNull Response<List<Test>> response) {
                if(response.isSuccessful()) {

                    assert response.body() != null;
                    tests.addAll(response.body());
                   // Toast.makeText(getActivity(),"tests quantity: "+ tests.size(), Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(new TestAdapter(tests));


                }
                else{
                    Toast.makeText(getActivity(), "Test download failed"+ response.code()+" : id="+userId, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Test>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(),"Something crashed --", Toast.LENGTH_SHORT).show();
            }
        });





        return  view;
    }

}