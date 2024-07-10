package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project.adapter.ProductAdapter;
import com.example.project.databinding.ActivityHomeBinding;
import com.example.project.model.Product;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import utils.Auth;
import utils.Navigate;

public class Home extends Base implements OnMapReadyCallback {
    MaterialButton logoutButton;
    MaterialButton cartButton;
    TextView currentUserEmail;
    String email;
    private ActivityHomeBinding binding;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        initMap();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        logoutButton = findViewById(R.id.home_logout_button);
        cartButton = findViewById(R.id.cart_btn);
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

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move the camera
        LatLng store = new LatLng(10.875256, 106.800539);
        mMap.addMarker(new MarkerOptions().position(store).title("Laptop Store"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(store));
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
        email = currentUser.getEmail();

        logoutButton.setOnClickListener(v -> {
            Auth.signOut();
            Navigate.navigate(this, Login.class);
            finish();
        });

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Cart.class);
            intent.putExtra("user_email", email);
            startActivity(intent);
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
