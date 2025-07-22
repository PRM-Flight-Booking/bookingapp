package com.example.bookingapp.view.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.data.model.ValidationResult;
import com.example.bookingapp.data.repository.UserRepository;
import com.example.bookingapp.databinding.ActivityRegisterBinding;
import com.example.bookingapp.utils.LocationManager;
import com.example.bookingapp.utils.Validator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityRegisterBinding binding;
    private UserRepository userRepository;
    private LocationManager locationManager;
    private GoogleMap mMap;
    private Marker currentMarker;
    private String selectedLocation = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize dependencies
        userRepository = new UserRepository(this);
        locationManager = new LocationManager(this);

        // Register button click listener
        binding.registerButton.setOnClickListener(view -> {
            attemptRegister();
        });

        binding.loginLink.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        // Validate password match
        binding.confirmPassword.addTextChangedListener(passwordWatcher);

        // Load Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable "My Location" button if permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getUserCurrentLocation();
        } else {
            requestLocationPermission();
        }

        // Handle map click for selecting a location
        mMap.setOnMapClickListener(this::updateMapLocation);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            getUserCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserCurrentLocation() {
        locationManager.getCurrentLocation(this, this::updateMapLocation);
    }

    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            validatePasswordMatch();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private void validatePasswordMatch() {
        String password = binding.password.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();
        binding.textinputError.setVisibility(password.equals(confirmPassword) ? View.GONE : View.VISIBLE);
    }

    private void updateMapLocation(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.remove();
        }
        selectedLocation = latLng.latitude + "," + latLng.longitude;
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void attemptRegister() {
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        String name = binding.userName.getText().toString();

        // Validate user input
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || selectedLocation.isEmpty()) {
            Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password match
        if (!password.equals(binding.confirmPassword.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate user
        User user = new User(email, selectedLocation, name, password);
        ValidationResult result = Validator.validateUser(user);
        if (result.isValid()) {
            // Check if user already exists
            if (userRepository.createUser(user) != -1) {
                onRegisterSuccess();
            } else {
                onRegisterError("User already exists");
            }
        } else {
            onRegisterError(result.getErrors().get(0));
        }
    }

    private void onRegisterSuccess() {
        Toast.makeText(this, "Register successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void onRegisterError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
