package com.example.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.project.Detail;
import com.example.project.databinding.ViewholderPupListBinding;
import com.example.project.model.Product;


import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Viewholder> {
    ArrayList<Product> items;
    Context context;
    ViewholderPupListBinding binding;

    public ProductAdapter(ArrayList<Product> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProductAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ViewholderPupListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.Viewholder holder, int position) {
        binding.name.setText(items.get(position).getName());
        binding.type.setText(items.get(position).getDescription());
        binding.price.setText("$" + items.get(position).getPrice());

        int drawableResourced = holder.itemView.getResources().getIdentifier(items.get(position).getImage(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context).load(drawableResourced).transform(new GranularRoundedCorners(30,30,0,0)).into(binding.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Detail.class);
            System.out.println(intent);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public Viewholder(ViewholderPupListBinding binding) {
            super(binding.getRoot());
        }
    }
}
