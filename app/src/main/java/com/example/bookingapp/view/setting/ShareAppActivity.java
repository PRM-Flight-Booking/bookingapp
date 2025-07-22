package com.example.bookingapp.view.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.databinding.ActivityShareAppBinding;

public class ShareAppActivity extends AppCompatActivity {

    private ActivityShareAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cập nhật đường link Play Store (nếu cần động)
        String playStoreLink = "https://play.google.com/store/apps/details?id=com.example.bookingapp";
        binding.playStoreLink.setText(playStoreLink);

        // Nút Share
        binding.shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out OnlineBookTour: " + playStoreLink);
            startActivity(Intent.createChooser(shareIntent, "Share OnlineBookTour"));
        });

        // Nút Back
        binding.backButton.setOnClickListener(v -> finish());
    }
}