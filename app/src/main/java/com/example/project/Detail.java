package com.example.project;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.databinding.ActivityDetailBinding;
import com.example.project.model.Product;

public class Detail extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private Product object;
    private int numberOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        getBundles();

//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_detail);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void getBundles() {
        object= (Product) getIntent().getSerializableExtra("object");
        System.out.println(object);
        int drawableResourceId = this.getResources().getIdentifier(object.getImage(),"drawable",this.getPackageName());
        Glide.with(this).load(drawableResourceId).into(binding.image);
        binding.detailName.setText(object.getName());
        binding.detailRate.setText(object.getRate().toString());
        binding.detailDescription.setText(object.getDescription());
        binding.buyBtn.setText("Add to cart |" + "$" + object.getPrice());

        binding.back.setOnClickListener(v -> finish());
    }
}