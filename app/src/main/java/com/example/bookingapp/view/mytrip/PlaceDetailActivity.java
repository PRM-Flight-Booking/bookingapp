package com.example.bookingapp.view.mytrip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Flight;
import com.example.bookingapp.data.model.Place;
import com.example.bookingapp.data.model.User;
import com.squareup.picasso.Picasso;

public class PlaceDetailActivity extends AppCompatActivity {

    private ImageView placeImage;
    private TextView placeName, placeLocation, placeOverview;
    private LinearLayout btnFlightDetails, btnHotelDetails, btnPlaceDetails;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // Ánh xạ view
        placeImage = findViewById(R.id.place_image);
        placeName = findViewById(R.id.place_name);
        placeLocation = findViewById(R.id.place_location);
        placeOverview = findViewById(R.id.place_overview);
        btnFlightDetails = findViewById(R.id.btnFlightDetails);
        btnHotelDetails = findViewById(R.id.btnHotelDetails);
        btnPlaceDetails = findViewById(R.id.btnPlacelDetails);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent
        Place place = (Place) getIntent().getSerializableExtra("place");
        Flight flight = (Flight) getIntent().getSerializableExtra("flight");
        User user = (User) getIntent().getSerializableExtra("user"); // Lấy đúng đối tượng User

        // Gán dữ liệu địa điểm
        if (place != null) {
            placeName.setText(place.getName());
            String address = place.getAddress(this);
            placeLocation.setText(address != null ? address : "Location not available");
            placeOverview.setText(place.getOverview());

            Picasso.get()
                    .load(place.getImage().get(0))
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.error_img)
                    .into(placeImage);
        }

        // Gán sự kiện cho nút xem chuyến bay
        btnFlightDetails.setOnClickListener(v -> {
            Intent intent = new Intent(PlaceDetailActivity.this, FlightDetailActivity.class);
            if (user != null) {
                intent.putExtra("userId", user.getId()); // Truyền đúng userId
            } else {
                intent.putExtra("userId", -1);
            }
            startActivity(intent);
        });


        btnHotelDetails.setOnClickListener(v -> {
            Intent intent = new Intent(PlaceDetailActivity.this, HotelDetailActivity.class);
            if (user != null) {
                intent.putExtra("userId", user.getId());
                intent.putExtra("placeLocation", place.getLocation());

            }
            startActivity(intent);
        });

        btnPlaceDetails.setOnClickListener(v -> {
            Intent intent = new Intent(PlaceDetailActivity.this, PlaceBillDetailActivity.class);
            intent.putExtra("placeBillId", place.getId()); // nếu bạn đang giữ placeBillId
            startActivity(intent);
        });

    }
}
