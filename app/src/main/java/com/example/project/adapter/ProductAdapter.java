package com.example.project.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.project.databinding.ViewholderPupListBinding;
import com.example.project.model.Product;
import com.example.project.model.ProductDescription;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils.https.types.response.ProductDescriptionResponse;

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
        binding = ViewholderPupListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new Viewholder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.Viewholder holder, int position) {
        binding.name.setText(items.get(position).getName());
        binding.price.setText("VND" + items.get(position).getPrice());

        String description = items.get(position).getDescription();
        Map<String, String> map = new HashMap<>();
        String[] pairs = new Gson().fromJson(description, String[].class);
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            map.put(keyValue[0], keyValue[1]);
        }
        String json = new Gson().toJson(map);
        ProductDescriptionResponse productDescriptionResponse = new Gson().fromJson(json, ProductDescriptionResponse.class);
        ProductDescription productDescription = productDescriptionResponse.toModel();

        binding.type.setText(productDescription.getCpu() + " - " + productDescription.getRam());

        String imageUrl = items.get(position).getImageUrl();
        Glide.with(context).load(imageUrl).transform(new GranularRoundedCorners(30, 30, 0, 0)).into(binding.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
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
