package com.example.project;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.project.databinding.ActivityDetailBinding;
import com.example.project.helper.ManagmentCart;
import com.example.project.model.Product;
import com.google.android.gms.maps.GoogleMap;

public class Detail extends Base {

    private ActivityDetailBinding binding;
    private Product object;
    private int numberOrder = 1;
    private ManagmentCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementCart = new ManagmentCart(this);
        getBundles();
        statusBarColor();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    private void statusBarColor() {
        Window window = Detail.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Detail.this, R.color.purple));
    }

    private void getBundles() {
        object = (Product) getIntent().getSerializableExtra("object");
        binding.detailName.setText(object.getTitle());
        binding.detailRate.setText("" + object.getRating());
        binding.detailDescription.setText(object.getDescription());
        binding.buyBtn.setText("Add to cart | " + "VND " + object.getPrice());
        Glide.with(this).load(object.getPicUrl().get(0)).into(binding.image);

        binding.buyBtn.setOnClickListener(v -> {
            object.setNumberInCart(numberOrder);
            managementCart.insertProduct(object);
        });
        binding.back.setOnClickListener(v -> finish());
    }
}