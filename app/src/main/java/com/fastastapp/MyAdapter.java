package com.fastastapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fastastapp.model.Test;
import com.fastastapp.model.User;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.PersonViewHolder> {

    private ArrayList<Test> tests;

    public MyAdapter(ArrayList<Test> tests) {
        this.tests = tests;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_frame, parent, false);
        PersonViewHolder holder = new PersonViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder personViewHolder, int i) {
        final Test tmp = tests.get(i);

        personViewHolder.text.setText(tests.get(i).getName());


        personViewHolder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TestDataActivity.class);
                intent.putExtra("text",tmp.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        CardView item_card;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            item_card =(CardView) itemView.findViewById(R.id.card);
            text = itemView.findViewById(R.id.text);
        }
    }



}
