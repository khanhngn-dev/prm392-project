package com.example.project;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project.adapter.CartAdapter;
import com.example.project.databinding.ActivityCartBinding;
import com.example.project.helper.ManagmentCart;
import com.google.android.gms.maps.GoogleMap;

public class Cart extends Base {
    ActivityCartBinding binding;
    private ManagmentCart managementCart;
    private String userEmail;

    NotificationChannel channel;

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

        createNotificationChannel();
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
        sendNotification(managementCart.getListCart().size());
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

    private void createNotificationChannel() {
        if (ContextCompat.checkSelfPermission(
                this, "android.permission.POST_NOTIFICATION") ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel("cart", "Cart", NotificationManager.IMPORTANCE_DEFAULT);
            }
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, "android.permission.POST_NOTIFICATION")) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            Toast.makeText(this, "Must enabled notification", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNotification(int count) {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            Toast.makeText(this, "Notification is disabled", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "cart")
                .setSmallIcon(R.drawable.baseline_shopping_cart_24)
                .setContentTitle("Cart")
                .setContentText(count == 0 ? "Your cart is empty" : "You have " + count + " items in your cart")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setChannelId("cart")
                .setNumber(count);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());
    }
}