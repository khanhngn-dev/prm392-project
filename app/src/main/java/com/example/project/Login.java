package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import utils.Auth;
import utils.Navigate;

public class Login extends Base {
    TextInputEditText emailInput;
    TextInputEditText passwordInput;
    MaterialButton loginButton;
    TextView toSignUp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);
        loginButton = findViewById(R.id.login_button);
        toSignUp = findViewById(R.id.login_subtitle);
        progressBar = findViewById(R.id.progressBar);

        // Set click listener for login button
        loginButton.setOnClickListener(v -> {
            loading(true);

            String email = String.valueOf(emailInput.getText());
            String password = String.valueOf(passwordInput.getText());

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                loading(false);
                return;
            }

            Auth.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Navigate.navigate(this, Home.class);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            loading(false);
                        }
                    });
        });

        // Set click listener for sign up text
        toSignUp.setOnClickListener(v -> {
            Navigate.navigate(this, SignUp.class);
            finish();
        });
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            loginButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = Auth.currentUser();
        if (currentUser != null) {
            Navigate.navigate(this, Home.class);
            finish();
        }
    }
}