package com.example.bookingapp.view.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityAdminBinding;
import com.example.bookingapp.view.admin.dashboard.DashBoardFragment;
import com.example.bookingapp.view.admin.request.ReportFragment;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            binding.bottomNavigationView.setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });

        // Hiển thị DashBoardFragment mặc định
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container, new DashBoardFragment())
                    .commit();
        }

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.dashboard) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DashBoardFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.request) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new ReportFragment())
                        .commit();
                return true;
            }
            return false;
        });
    }
}