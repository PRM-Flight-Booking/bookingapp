package com.example.bookingapp.view.auth;

import static com.example.bookingapp.utils.Constant.USER_TAG;
import static com.example.bookingapp.utils.Tool.insertSampleData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.bookingapp.R;
import com.example.bookingapp.data.database.ShareReferenceHelper;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.databinding.ActivitySplashBinding;
import com.example.bookingapp.view.MainActivity;
import com.example.bookingapp.view.admin.AdminActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000; // 5 seconds
    private static boolean isInitialized = false; // Flag to track first run

    private ActivitySplashBinding binding;
    private ShareReferenceHelper shareReferenceHelper;
    private ScheduledExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent recreation on configuration changes
        if (isTaskRoot() && !isInitialized) {
            binding = ActivitySplashBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            shareReferenceHelper = new ShareReferenceHelper(this);
//
            Boolean darkModeEnabled = shareReferenceHelper.get("dark_mode_enabled", Boolean.class);
            Log.d("SplashActivity", "Dark Mode Enabled: " + darkModeEnabled);
            if (darkModeEnabled != null && darkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            binding.gif.setImageResource(R.drawable.animation1);

            // Initialize executor and schedule task
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                // Run database init and user check in background
                insertSampleData(this);
                User user = shareReferenceHelper.get(USER_TAG, User.class);

                // Switch to main thread for UI update
                runOnUiThread(() -> {
                    Intent intent = new Intent();
                    if(user!=null){
                        if(user.isAdmin()){
                            intent.setClass(SplashActivity.this, AdminActivity.class);
                        }else{
                            intent.setClass(SplashActivity.this, MainActivity.class);
                        }
                    }else{
                        intent.setClass(SplashActivity.this, LoginActivity.class);
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });

                executor.shutdown(); // Shutdown after task completion
                isInitialized = true; // Mark as initialized
            }, SPLASH_TIME_OUT, TimeUnit.MILLISECONDS);
        } else {
            // If not the root task or already initialized, finish immediately
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow(); // Cancel any pending tasks
        }
    }
}