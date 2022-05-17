package com.fastastapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fastastapp.R;
import com.fastastapp.model.Variant;

import java.util.List;

public  class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.PersonViewHolder> {

    private List<Variant> list;

    public VariantAdapter(List<Variant> list, Context context) {
        this.list = list;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.variant_card, viewGroup, false);

        PersonViewHolder holder = new PersonViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VariantAdapter.PersonViewHolder holder, int position) {
        holder.variantName.setText("Вариант "+ (position+1));
        holder.mark.setText(list.get(position).getMark()+"/ 10");

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        public TextView variantName;
        public TextView mark;


        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            variantName = itemView.findViewById(R.id.variant_name);
            mark = itemView.findViewById(R.id.mark_text);
        }
    }
}