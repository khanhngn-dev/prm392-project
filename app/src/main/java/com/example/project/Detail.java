package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.databinding.ActivityDetailBinding;
import com.example.project.helper.ManagmentCart;
import com.example.project.model.Product;

public class Detail extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private Product object;
    private int numberOrder = 1;
    private ManagmentCart managementCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        getBundles();
        managementCart= new ManagmentCart(this);
        statusBarColor();
    }

    private void statusBarColor() {
        Window window=Detail.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Detail.this, R.color.purple));
    }
    private void getBundles() {
        object= (Product) getIntent().getSerializableExtra("object");
        System.out.println(object);
        binding.detailName.setText(object.getTitle());
        binding.detailRate.setText("" + object.getRating());
        binding.detailDescription.setText(object.getDescription());
        binding.buyBtn.setText("Add to cart | " + "$ " + object.getPrice());

        binding.buyBtn.setOnClickListener(v -> {
            object.setNumberInCart(numberOrder);
            managementCart.insertProduct(object);
        });
        binding.back.setOnClickListener(v -> finish());
    }
}