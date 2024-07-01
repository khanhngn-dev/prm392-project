package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project.adapter.ProductAdapter;
import com.example.project.databinding.ActivityHomeBinding;
import com.example.project.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DriverManager;
import java.util.ArrayList;

import utils.Auth;
import utils.Navigate;

public class Home extends Base {
    MaterialButton logoutButton;
    TextView currentUserEmail;

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();

        // Initialize views
        logoutButton = findViewById(R.id.home_logout_button);
        currentUserEmail = findViewById(R.id.home_current_user);
    }

    private void initRecyclerView() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Product> items = new ArrayList<>();
        System.out.println(items);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot);
                if(snapshot.exists()) {
                    for(DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Product.class));
                    }
                    if(!items.isEmpty()){
                        binding.prodcutView.setLayoutManager(new GridLayoutManager(Home.this, 2));
                        binding.prodcutView.setAdapter(new ProductAdapter(items));
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
            Auth.signOut();
            Navigate.navigate(this, Login.class);
            finish();
        });
    }
}