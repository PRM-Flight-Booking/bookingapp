package com.example.bookingapp.view.auth;

import static com.example.bookingapp.utils.Constant.USER_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.data.database.ShareReferenceHelper;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.data.repository.UserRepository;
import com.example.bookingapp.databinding.ActivityLoginBinding;
import com.example.bookingapp.view.MainActivity;
import com.example.bookingapp.view.admin.AdminActivity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize repository
        userRepository = new UserRepository(this);

        // Navigate to Register screen
        binding.registerButton.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Handle login button click
        binding.loginButton.setOnClickListener(v -> attemptLogin());

        // Load users in background to prevent UI thread blocking
        Executors.newSingleThreadExecutor().execute(() -> {
            List<User> users = userRepository.getAllUsers();
            Log.d("Users", users.toString());
        });
    }

    // Method to validate input and attempt login
    private void attemptLogin() {
        String email = Objects.requireNonNull(binding.email.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.editTextPassword.getText()).toString().trim();

        // Input validation
        if (email.isEmpty()) {
            binding.email.setError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            binding.editTextPassword.setError("Password is required");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check user credentials
        String passwordFromDB = userRepository.getPasswordByEmail(email);
        if (passwordFromDB == null) {
            Toast.makeText(this, "Invalid email.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!passwordFromDB.equals(password)) {
                Toast.makeText(this, "Invalid password.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                try {
                    User user = userRepository.getUserByAuth(email, password);
                    // Save user info to SharedPreferences
                    ShareReferenceHelper shareReferenceHelper = new ShareReferenceHelper(this);
                    shareReferenceHelper.save(USER_TAG, user);

                    // Navigate based on user role
                    Intent intent = new Intent();
                    if (user.isAdmin()) {
                        intent.setClass(this, AdminActivity.class);
                    } else {
                        intent.setClass(this, MainActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e("LoginError", e.toString());
                    Toast.makeText(this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Validate email format
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}
