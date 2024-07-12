package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project.adapter.CartAdapter;
import com.example.project.databinding.ActivityCartBinding;
import com.example.project.helper.ManagmentCart;
import com.google.android.gms.maps.GoogleMap;

public class Cart extends Base {
    ActivityCartBinding binding;
    private ManagmentCart managementCart;
    private String userEmail;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null && getIntent().hasExtra("user_email")) {
            userEmail = getIntent().getStringExtra("user_email");
        }

        managementCart = new ManagmentCart(this);

        if (managementCart != null) {
            setVariable();
            calculatorCart();
            initCartList();
        } else {
            binding.emptyTxt.setText("Error initializing cart management.");
            binding.emptyTxt.setVisibility(View.VISIBLE);
        }

        binding.checkoutBtn.setOnClickListener(v -> {
            double totalCost = calculatorCart();
            Intent checkoutIntent = new Intent(Cart.this, Checkout.class);
            checkoutIntent.putExtra("user_email", userEmail);
            checkoutIntent.putExtra("total_cost", totalCost);
            checkoutIntent.putExtra("product_list", managementCart.getListCart());

            startActivity(checkoutIntent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    private void initCartList() {
        if (managementCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managementCart.getListCart(), this, this::calculatorCart));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, Home.class);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private double calculatorCart() {
        double shopping = 10;
        double itemTotal = (double) Math.round(managementCart.getTotalFee() * 100) / 100;
        double total = (double) Math.round((managementCart.getTotalFee() + shopping) * 100) / 100;

        binding.subTotalTxt.setText("VND" + itemTotal);
        binding.shoppingTxt.setText("VND" + shopping);
        binding.totalTxt.setText("VND" + total);
        return total;
    }
}