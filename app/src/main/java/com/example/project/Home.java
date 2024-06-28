package com.example.project;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project.adapter.ProductAdapter;
import com.example.project.databinding.ActivityHomeBinding;
import com.example.project.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import utils.Auth;
import utils.Navigate;
import utils.https.ApiResponse;
import utils.https.NonNullCallback;
import utils.https.RetrofitClient;

public class Home extends AppCompatActivity {
    MaterialButton logoutButton;
    TextView currentUserEmail;

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);


        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        logoutButton = findViewById(R.id.home_logout_button);
        currentUserEmail = findViewById(R.id.home_current_user);

    }

    private void initRecyclerView() {
        RetrofitClient.getAPIClient(api -> {
            api.products().enqueue(new NonNullCallback<ApiResponse<List<Product>>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<List<Product>>> call, @NonNull Response<ApiResponse<List<Product>>> response) {
                    // Get status code
                    int statusCode = response.code();

                    if (statusCode == 200 && response.body() != null) {
                        List<Product> products = response.body().getData();

                        ArrayList<Product> items = new ArrayList<>(products);

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(Home.this, 2);
                        binding.prodcutView.setLayoutManager(gridLayoutManager);
                        binding.prodcutView.setAdapter(new ProductAdapter(items));
                    } else {
                        Toast.makeText(Home.this, "Cant get products from server.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<List<Product>>> call, @NonNull Throwable t) {
                    Toast.makeText(Home.this, "Cant get products from server.", Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = Auth.currentUser();
        if (currentUser == null) {
            Navigate.navigate(this, Login.class);
            finish();
            return;
        }

        currentUserEmail.setText(currentUser.getEmail());

        logoutButton.setOnClickListener(v -> {
            binding.prodcutView.setAdapter(null);
            RetrofitClient.resetClient();
            Auth.signOut();
            Navigate.navigate(this, Login.class);
            finish();
        });
    }
}