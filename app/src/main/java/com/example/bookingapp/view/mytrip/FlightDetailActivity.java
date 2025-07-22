package com.example.bookingapp.view.mytrip;

import static com.example.bookingapp.utils.Tool.convertToAddress;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Flight;
import com.example.bookingapp.data.model.FlightBill;
import com.example.bookingapp.data.repository.FlightBillRepository;
import com.example.bookingapp.data.repository.FlightRepository;

import java.util.List;

public class FlightDetailActivity extends AppCompatActivity {
    private static final String TAG = "FlightDetailActivity";

    private TextView tvDate, tvDeparture, tvArrival, tvAirline;
    private Button btnShowTicket;

    private FlightBill flightBill; // dùng để lưu vé để xem chi tiết

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_detail);

        Log.d(TAG, "onCreate: FlightDetailActivity started");

        // Ánh xạ view
        tvDate = findViewById(R.id.tvDate);
        tvDeparture = findViewById(R.id.tvDeparture);
        tvArrival = findViewById(R.id.tvArrival);
        tvAirline = findViewById(R.id.tv_airline);
        btnShowTicket = findViewById(R.id.btnDownloadTicket);

        // Nhận userId từ Intent
        int userId = getIntent().getIntExtra("userId", -1);
        Log.d(TAG, "Received userId from intent: " + userId);
        if (userId == -1) {
            Log.e(TAG, "User ID not received!");
            Toast.makeText(this, "Không thể xác định người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khởi tạo repository
        FlightRepository flightRepo = new FlightRepository(this);
        FlightBillRepository flightBillRepo = new FlightBillRepository(this);

        // Lấy danh sách các vé máy bay đã đặt bởi người dùng
        List<FlightBill> userFlightBills = flightBillRepo.getFlightBillsByUserId(userId);
        Log.d(TAG, "Tổng số chuyến bay đã đặt bởi userId " + userId + ": " + userFlightBills.size());

        if (userFlightBills.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đặt chuyến bay nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy vé đầu tiên trong danh sách để hiển thị (có thể cải tiến thêm sau)
        flightBill = userFlightBills.get(0);

        // Lấy thông tin chuyến bay từ vé
        Flight flight = flightRepo.getFlightById(flightBill.getIdFlight());

        if (flight != null) {
            Log.d(TAG, "Tìm thấy chuyến bay: id=" + flight.getId() + ", name=" + flight.getName());

            // Hiển thị thông tin lên giao diện
            tvDate.setText(flight.getDepartureTime().toString());
            tvDeparture.setText(convertToAddress(this, flight.getDepartureLocation()));
            tvArrival.setText(convertToAddress(this, flight.getArrivalLocation()));
            tvAirline.setText(flight.getName());
        } else {
            Log.e(TAG, "Không tìm thấy thông tin chuyến bay!");
            Toast.makeText(this, "Không tìm thấy thông tin chuyến bay!", Toast.LENGTH_SHORT).show();
        }

        // Nút xem vé
        btnShowTicket.setOnClickListener(v -> {
            Log.d(TAG, "btnShowTicket clicked");

            if (flightBill != null) {
                Log.d(TAG, "Mở ShowTicketActivity với flightBillId = " + flightBill.getId());
                Intent intent = new Intent(FlightDetailActivity.this, ShowTicketActivity.class);
                intent.putExtra("flightBillId", flightBill.getId());
                startActivity(intent);
            } else {
                Log.e(TAG, "Không tìm thấy flightBill khi nhấn xem vé!");
                Toast.makeText(this, "Không tìm thấy vé để hiển thị!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút quay lại
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "btnBack clicked - finish activity");
            finish();
        });
    }
}
