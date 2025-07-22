package com.example.bookingapp.view.setting;

import static com.example.bookingapp.utils.Constant.USER_TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.R;
import com.example.bookingapp.data.database.ShareReferenceHelper;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.data.repository.UserRepository;
import com.example.bookingapp.databinding.ActivityProfileBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ProfileActivity extends AppCompatActivity implements OnMapReadyCallback, ImageBottomSheetFragment.OnImageSelectedListener {

    private ActivityProfileBinding binding;
    private GoogleMap mMap;
    private ShareReferenceHelper helper;
    private UserRepository userRepository;
    private String location;
    private int avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize dependencies
        userRepository = new UserRepository(this);
        helper = new ShareReferenceHelper(this);

        // Load current user information
        User user = helper.get(USER_TAG, User.class);
        Log.d("User", user.toString());
        showInformation(user);

        // Set up button click listeners
        binding.backButton.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        binding.saveButton.setOnClickListener(v -> {
            onProfileSaved(binding.userName.getText().toString(), avatar, location);
        });

        binding.uploadImg.setOnClickListener(v -> onUploadImg());

        // Load the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("New Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            location = latLng.latitude + "," + latLng.longitude;
        });

        User user = helper.get(USER_TAG, User.class);
        location = user.getLocation();
        String[] locationParts = location.split(",");
        LatLng userLocation = new LatLng(Double.parseDouble(locationParts[0].trim()), Double.parseDouble(locationParts[1].trim()));
        mMap.addMarker(new MarkerOptions().position(userLocation).title(user.getLocation()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    // Show user information on the profile screen
    public void showInformation(User user) {
        binding.userName.setText(user.getName());
        binding.userEmail.setText(user.getEmail());
        binding.userAvatar.setImageResource(user.getAvatar());
        avatar = user.getAvatar();
    }

    // Save updated profile information
    public void onProfileSaved(String name, int avatar, String location) {
        User user = helper.get(USER_TAG, User.class);
        if(name.isEmpty()){
            Toast.makeText(ProfileActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        user.setName(name);
        user.setAvatar(avatar);
        user.setLocation(location);
        userRepository.updateUser(user);
        helper.save(USER_TAG, user);
        saveInformation("Profile update successful");
    }

    // Show a Toast message indicating the success of the save action
    public void saveInformation(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Show the image selection bottom sheet
    public void onUploadImg() {
        int[] imageResources = new int[]{R.drawable.img_0, R.drawable.img_1, R.drawable.img_2,
                R.drawable.img_3, R.drawable.img_4};
        ImageBottomSheetFragment fragment = ImageBottomSheetFragment.newInstance(imageResources);
        fragment.setOnImageSelectedListener(this); // Set the image selection listener
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    // Handle image selection
    @Override
    public void onImageSelected(int imageResourceId) {
        avatar = imageResourceId; // Update the avatar resource ID
        binding.userAvatar.setImageResource(imageResourceId); // Update the avatar image on the screen
    }
}
