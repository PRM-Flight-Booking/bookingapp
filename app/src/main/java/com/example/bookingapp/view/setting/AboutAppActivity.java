package com.example.bookingapp.view.setting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.databinding.ActivityAboutAppBinding;

public class AboutAppActivity extends AppCompatActivity {

    private ActivityAboutAppBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Cập nhật phiên bản ứng dụng
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            binding.appVersion.setText("Version " + versionName);
        } catch (Exception e) {
            binding.appVersion.setText("Version 1.0.0"); // Giá trị mặc định
        }

        // Nút Back
        binding.backButton.setOnClickListener(v -> finish());
    }
}