package com.example.bookingapp.view.mytrip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Bill;
import com.example.bookingapp.data.model.Hotel;
import com.example.bookingapp.data.repository.BillRepository;
import com.example.bookingapp.data.repository.HotelRepository;
import com.example.bookingapp.view.adapter.HotelAdapter;

import java.util.ArrayList;
import java.util.List;

public class HotelDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HotelAdapter adapter;
    private String placeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        recyclerView = findViewById(R.id.recyclerHotel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int userId = getIntent().getIntExtra("userId", -1);
        placeLocation = getIntent().getStringExtra("placeLocation");

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Nếu không hợp lệ (null hoặc không đúng định dạng "lat,lon"), gán tọa độ mặc định
        if (!isValidCoordinate(placeLocation)) {
            placeLocation = "0.0,0.0";
        }

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadBookedHotels(userId);
    }

    private void loadBookedHotels(int userId) {
        BillRepository billRepo = new BillRepository(this);
        HotelRepository hotelRepo = new HotelRepository(this);

        List<Bill> bills = billRepo.getBillByUserId(String.valueOf(userId));
        List<Hotel> bookedHotels = new ArrayList<>();

        for (Bill bill : bills) {
            Log.d("HotelDebug", "Bill: " + bill.toString());

            if (bill.getHotelBillId() != 0) {
                Hotel hotel = hotelRepo.getHotelById(bill.getHotelBillId());
                if (hotel != null) {
                    Log.d("HotelDebug", "Found hotel: " + hotel.getName());
                    bookedHotels.add(hotel);
                }
            }
        }

        if (bookedHotels.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đặt khách sạn nào.", Toast.LENGTH_SHORT).show();
        }

        adapter = new HotelAdapter(bookedHotels, placeLocation, hotel -> {
            Intent intent = new Intent(HotelDetailActivity.this, HotelBillDetailActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("hotelId", hotel.getId());
            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);
    }

    // Hàm kiểm tra chuỗi tọa độ có đúng định dạng không
    private boolean isValidCoordinate(String loc) {
        if (loc == null) return false;
        String[] parts = loc.split(",");
        if (parts.length != 2) return false;
        try {
            Double.parseDouble(parts[0].trim());
            Double.parseDouble(parts[1].trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
