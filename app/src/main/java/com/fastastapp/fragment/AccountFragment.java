package com.fastastapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fastastapp.R;
import com.fastastapp.SignUpActivity;
import com.fastastapp.model.User;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private static final String ARG_PARAM1 = "authToken";
    private static final String ARG_PARAM2 = "param2";

    private String token;
    private String mParam2;

    public AccountFragment() {
    }


    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //Find Text views to set right dara
        TextView username = view.findViewById(R.id.profile_name);
        TextView email = view.findViewById(R.id.profile_email);

        UserApi loginService =
                ServiceGenerator.createService(UserApi.class, token);

        loginService.logIn().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                   // Toast.makeText(getActivity(), "Login Successful (Account Fragment)", Toast.LENGTH_SHORT).show();

                    //set data
                    username.setText(response.body().getUsername());
                    email.setText(response.body().getEmail());
                } else {
                    Toast.makeText(getActivity(), "Failed. Check input data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Something crashed 1", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }

}