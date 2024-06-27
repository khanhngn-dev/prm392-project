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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import utils.Auth;
import utils.Navigate;
import utils.https.ApiResponse;
import utils.https.NonNullCallback;
import utils.https.RetrofitClient;
import utils.https.types.request.SignupRequest;
import utils.https.types.response.SignupResponse;

public class SignUp extends AppCompatActivity {
    TextInputEditText emailInput;
    TextInputEditText passwordInput;
    TextInputEditText confirmPasswordInput;
    MaterialButton signUpButton;
    TextView toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        emailInput = findViewById(R.id.sign_up_email_input);
        passwordInput = findViewById(R.id.sign_up_password_input);
        confirmPasswordInput = findViewById(R.id.sign_up_confirm_password_input);
        signUpButton = findViewById(R.id.sign_up_button);
        toLogin = findViewById(R.id.sign_up_subtitle);

        // Set click listener for sign up button
        signUpButton.setOnClickListener(v -> {
            String email = String.valueOf(emailInput.getText());
            String password = String.valueOf(passwordInput.getText());
            String confirmPassword = String.valueOf(confirmPasswordInput.getText());

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Password regex for at least 8 characters, 1 uppercase, 1 lowercase, 1 number
            String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
            if (!password.matches(passwordRegex)) {
                Toast.makeText(this, "Password must be at least 8 characters, 1 uppercase, 1 lowercase, 1 number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            Auth.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            RetrofitClient.getAPIClient(api -> {

                                SignupRequest signupObj = new SignupRequest(id);
                                api.signup(signupObj).enqueue(new NonNullCallback<ApiResponse<SignupResponse>>() {

                                    @Override
                                    public void onResponse(@NonNull Call<ApiResponse<SignupResponse>> call, @NonNull Response<ApiResponse<SignupResponse>> response) {
                                        // Get status code
                                        int statusCode = response.code();

                                        if (statusCode == 201) {
                                            Navigate.navigate(SignUp.this, Home.class);
                                            finish();
                                        } else {
                                            Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ApiResponse<SignupResponse>> call, @NonNull Throwable t) {
                                        Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return null;
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        // Set click listener for login text
        toLogin.setOnClickListener(v -> {
            Navigate.navigate(this, Login.class);
            finish();
        });
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