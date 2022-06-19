package com.fastastapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fastastapp.R;
import com.fastastapp.model.User;
import com.fastastapp.retrofit.ServiceGenerator;
import com.fastastapp.retrofit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private static final String ARG_PARAM1 = "authToken";

    private String token;

    public AccountFragment() {
    }


    public static AccountFragment newInstance(String param1) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(ARG_PARAM1);
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
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {

                    //set data
                    username.setText(response.body() != null ? response.body().getUsername() : null);
                    email.setText(response.body().getEmail());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Something crashed", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }

}