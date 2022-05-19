package com.fastastapp.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fastastapp.R;
import com.fastastapp.TestDataActivity;
import com.fastastapp.model.Test;

import java.util.ArrayList;

public  class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private final ArrayList<Test> tests;

    public TestAdapter(ArrayList<Test> tests) {
        this.tests = tests;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //set up fragment in activity
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_frame, parent, false);

        return new TestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder testViewHolder, int position) {
        // get specific element from list by it position in view
        final Test tmp = tests.get(position);

        if(tmp.getName()=="")tmp.setName("Test "+ (position+1));


        testViewHolder.text.setText(tmp.getName());

        testViewHolder.item_card.setOnClickListener(view -> {

            Intent intent = new Intent(view.getContext(), TestDataActivity.class);

            //send by intent data about test name
            intent.putExtra("text",tmp.getName());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {

        private final TextView text;
        private final CardView item_card;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);

            //find list element
            item_card =(CardView) itemView.findViewById(R.id.card);

            text = itemView.findViewById(R.id.text_test_name);
        }
    }



}
