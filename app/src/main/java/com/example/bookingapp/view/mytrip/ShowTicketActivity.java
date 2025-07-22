package com.example.bookingapp.view.mytrip;

import static com.example.bookingapp.utils.Tool.convertToAddress;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Flight;
import com.example.bookingapp.data.model.FlightBill;
import com.example.bookingapp.data.repository.FlightBillRepository;
import com.example.bookingapp.data.repository.FlightRepository;

public class ShowTicketActivity extends AppCompatActivity {

    private TextView tvPassengerName, tvTicket, tvFlightNumber, tvPrice;
    private TextView tvDepartureCode, tvArrivalCode;
    private Button btnDownloadTicket;
    private ImageView imgQRCode;

    private FlightRepository flightRepo;
    private FlightBillRepository flightBillRepo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ticket);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // Ánh xạ view
        tvPassengerName = findViewById(R.id.tvPassengerName2);
        tvTicket = findViewById(R.id.tvTicket2);
        tvPrice = findViewById(R.id.tvPrice2);
        tvFlightNumber = findViewById(R.id.tvFlightNumber2);
        imgQRCode = findViewById(R.id.imgQRCode);
        btnDownloadTicket = findViewById(R.id.btnDownloadTicket);
        tvDepartureCode = findViewById(R.id.tvDepartureCode);
        tvArrivalCode = findViewById(R.id.tvArrivalCode);

        Button btnCancelTicket = findViewById(R.id.btnCancelTicket);
        btnCancelTicket.setOnClickListener(v -> finish());

        // Khởi tạo repository
        flightRepo = new FlightRepository(this);
        flightBillRepo = new FlightBillRepository(this);

        // Lấy flightBillId từ intent
        int flightBillId = getIntent().getIntExtra("flightBillId", -1);
        Log.d("ShowTicket", "Received flightBillId = " + flightBillId);

        if (flightBillId != -1) {
            FlightBill flightBill = flightBillRepo.getFlightBillById(flightBillId);
            if (flightBill != null) {
                Flight flight = flightRepo.getFlightById(flightBill.getIdFlight());
                if (flight != null) {
                    showDataOnUI(flightBill, flight);
                    return;
                } else {
                    Log.e("ShowTicket", "Không tìm thấy thông tin chuyến bay với id=" + flightBill.getIdFlight());
                }
            } else {
                Log.e("ShowTicket", "Không tìm thấy FlightBill với id=" + flightBillId);
            }
        } else {
            Log.e("ShowTicket", "Không nhận được flightBillId từ intent");
        }

        showEmptyState(); // Nếu có lỗi gì đó thì hiển thị trạng thái rỗng
    }

    private void showDataOnUI(FlightBill bill, Flight flight) {
        tvPassengerName.setText(String.valueOf(bill.getIdUser()));
        tvTicket.setText(String.valueOf(bill.getTicketNumber()));
        tvFlightNumber.setText(String.valueOf(bill.getIdFlight()));
        tvPrice.setText(String.format("%.2f", bill.getPrice()));

        if (flight != null) {
            tvDepartureCode.setText(convertToAddress(this, flight.getDepartureLocation()));
            tvArrivalCode.setText(convertToAddress(this, flight.getArrivalLocation()));
        }

        Log.d("ShowTicket", "Hiển thị thông tin vé thành công");
    }

    private void showEmptyState() {
        tvPassengerName.setText("No Data");
        tvTicket.setText("No Data");
        tvFlightNumber.setText("No Data");
        tvPrice.setText("No Data");
        tvDepartureCode.setText("No Data");
        tvArrivalCode.setText("No Data");
        Log.w("ShowTicket", "Dữ liệu không khả dụng, hiển thị trạng thái rỗng");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
