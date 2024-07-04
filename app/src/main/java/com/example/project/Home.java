package com.example.project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project.adapter.ProductAdapter;
import com.example.project.databinding.ActivityHomeBinding;
import com.example.project.manager.SocketManager;
import com.example.project.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import utils.Auth;
import utils.Navigate;
import utils.https.RetrofitClient;

public class Home extends AppCompatActivity {
    MaterialButton logoutButton;
    MaterialButton chatButton;
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
        chatButton = findViewById(R.id.home_chat_button);

    }

    private void initRecyclerView() {
        ArrayList<Product> items = new ArrayList<>();
        items.add(new Product("1", "Apple Watch SE", "watch", "Apple", "Apple", 349.99, 4.9));
        items.add(new Product("2", "Galaxy Watch 4", "watch", "Samsung", "Samsung", 249.99, 4.9));
        items.add(new Product("3", "Amazfit GTS 2", "watch", "Amazfit", "Amazfit", 0.0, 4.9));
        items.add(new Product("4", "Galaxy Watch 7", "watch", "Samsung", "Samsung", 0.0, 4.9));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.prodcutView.setLayoutManager(gridLayoutManager);
        binding.prodcutView.setAdapter(new ProductAdapter(items));
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
            RetrofitClient.resetClient();
            SocketManager.resetClient();
            Navigate.navigate(this, Login.class);
            finish();
        });

        chatButton.setOnClickListener(v -> {
            Navigate.navigate(this, ConversationList.class);
        });
    }
}