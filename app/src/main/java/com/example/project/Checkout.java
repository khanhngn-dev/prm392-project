package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project.databinding.ActivityCartBinding;
import com.example.project.model.Order;
import com.example.project.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;

public class Checkout extends Base {
    private EditText phoneEditText;
    private WebView webView;
    private Handler handler;
    private Runnable closeWebViewRunnable;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        TextView emailTextView = findViewById(R.id.email);
        TextView totalCostTextView = findViewById(R.id.totalTxt);
        phoneEditText = findViewById(R.id.phone);
        webView = findViewById(R.id.webview);

        // Retrieve the email from the Intent
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("user_email");
        double totalCost = intent.getDoubleExtra("total_cost", 0.0);

        List<Product> productList = (List<Product>) intent.getSerializableExtra("product_list");

        // Set the email to the TextView
        if (userEmail != null) {
            emailTextView.setText(userEmail);
        } else {
            emailTextView.setText("Email not provided.");
        }


        totalCostTextView.setText("VND" + totalCost);

        findViewById(R.id.checkoutBtn).setOnClickListener(v -> {
            String phoneNumber = phoneEditText.getText().toString().trim();
            if (isPhoneNumberValid(phoneNumber)) {
                Order order = new Order(UUID.randomUUID(), userEmail, productList ,phoneNumber, "success", totalCost);
                createOrderInFirebase(order);
                showWebView();
                webView.loadUrl("https://fake-vnpay-for-prm.vercel.app/");
            } else {
                Toast.makeText(Checkout.this, "Invalid phone number. It should start with 0 and have 10 digits.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isPhoneNumberValid(@NonNull String phoneNumber) {
        return phoneNumber.length() == 10 && phoneNumber.startsWith("0") && TextUtils.isDigitsOnly(phoneNumber);
    }


    private void createOrderInFirebase(Order order) {

        DatabaseReference ordersRef = database.getReference("Orders");

        // Push the order object to the database
        ordersRef.push().setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Checkout.this, "Order created successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Checkout.this, "Failed to create order. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
    }

    private void showWebView() {
        webView.setVisibility(View.VISIBLE);
        setupWebView();

        // Start the timer to close the WebView after 15 minutes
        handler = new Handler(Looper.getMainLooper());
        closeWebViewRunnable = this::closeWebView;
        handler.postDelayed(closeWebViewRunnable, 15 * 60 * 1000); // 15 minutes
    }

    private void closeWebView() {
        webView.setVisibility(View.GONE);
        Toast.makeText(this, "WebView closed.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE) {
            closeWebView();
            handler.removeCallbacks(closeWebViewRunnable);
        } else {
            super.onBackPressed();
        }
    }
}
