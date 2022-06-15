package com.fastastapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fastastapp.R;
import com.fastastapp.VariantsActivity;
import com.fastastapp.model.TestNameId;

import java.util.ArrayList;

public  class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private final ArrayList<TestNameId> tests;
    private final String token;
    private final int idUser;

    public TestAdapter(ArrayList<TestNameId> tests, String token, int idUser) {
        this.tests = tests;
        this.token =token;
        this.idUser =idUser;
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

        testViewHolder.text.setText(tests.get(position).getName());

        testViewHolder.item_card.setOnClickListener(view -> {

            //redirect to variant details
            Intent intent = new Intent(view.getContext(), VariantsActivity.class);
            intent.putExtra("idTest", tests.get(position).getId());
            intent.putExtra("token", token);
            intent.putExtra("idUser", idUser);
            intent.putExtra("testName", tests.get(position).getName());


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
            item_card = itemView.findViewById(R.id.card);
            text = itemView.findViewById(R.id.text_test_name);
        }
    }



}
