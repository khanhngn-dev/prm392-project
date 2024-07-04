package com.example.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.project.Detail;
import com.example.project.databinding.ViewholderPupListBinding;
import com.example.project.model.Product;


import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Viewholder> {
    ArrayList<Product> items;
    Context context;

    public ProductAdapter(ArrayList<Product> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProductAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPupListBinding binding=ViewholderPupListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.Viewholder holder, int position) {
        holder.binding.name.setText(items.get(position).getTitle());
        holder.binding.type.setText(items.get(position).getDescription());
        holder.binding.price.setText("VND" + items.get(position).getPrice());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions=requestOptions.transform(new CenterCrop());

        Glide.with(context).load(items.get(position).getPicUrl().get(0)).apply(requestOptions).into(holder.binding.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Detail.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPupListBinding binding;
        public Viewholder(ViewholderPupListBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
