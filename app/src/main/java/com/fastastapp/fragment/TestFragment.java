package com.fastastapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fastastapp.AddNewTest;
import com.fastastapp.R;
import com.fastastapp.SignUpActivity;
import com.fastastapp.adapters.MyAdapter;
import com.fastastapp.model.Test;
import com.fastastapp.model.User;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ArrayList<Test> tests;
    private FloatingActionButton floatingActionButton;


    public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        int user_id = getActivity().getIntent().getIntExtra("userId",0);

        init(view);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddNewTest.class);
                startActivity(intent);
            }
        });
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        UserApi loginService =
                ServiceGenerator.createService(UserApi.class, "m@gmail.com", "qwe1@rty");

        loginService.logIn().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){

                }
                else{
                    Toast.makeText(getActivity(), "Failed. Check input data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(),"Something crashed 1", Toast.LENGTH_SHORT).show();
                Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "Error occurred");
            }
        });




        UserApi userApi = serviceGenerator.createService(UserApi.class);
        List<Test> tests = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userApi.getTests(user_id).enqueue(new Callback<List<Test>>() {
            @Override
            public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successful test download", Toast.LENGTH_SHORT).show();

                    for (Test test: response.body()
                         ) {
                        tests.add(test);
                    }
                    //redirect to welcome page


                }
                else{
                    Toast.makeText(getActivity(), "Test download failed"+ response.code()+ " id user "+ user_id, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Test>> call, Throwable t) {
                Toast.makeText(getActivity(),"Something crashed", Toast.LENGTH_SHORT).show();
                Logger.getLogger(TestFragment.class.getName()).log(Level.SEVERE, "Error occurred");
            }
        });

        recyclerView.setAdapter(new MyAdapter((ArrayList<Test>) tests));


        return  view;
    }
    private  void init(View view){
        floatingActionButton = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recview);

    }
}