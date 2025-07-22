package com.example.bookingapp.view.mytrip;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Bill;
import com.example.bookingapp.data.model.HotelBill;
import com.example.bookingapp.data.repository.BillRepository;
import com.example.bookingapp.data.repository.HotelBillRepository;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HotelBillDetailActivity extends AppCompatActivity {
    private TextView tvDate, tvPrice, tvTime, tvHotelId, tvTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_bill_detail);

        tvDate = findViewById(R.id.tvDate);
        tvPrice = findViewById(R.id.tvPrice);
        tvTime = findViewById(R.id.tvTime);
        tvHotelId = findViewById(R.id.tvHotelId);
        tvTicket = findViewById(R.id.tvTicket);

        int userId = getIntent().getIntExtra("userId", -1);
        int hotelId = getIntent().getIntExtra("hotelId", -1);

        Button btnCancelTicket = findViewById(R.id.btnCancelTicket);
        btnCancelTicket.setOnClickListener(v -> finish());

        if (userId == -1 || hotelId == -1) {
            Toast.makeText(this, "Thiếu thông tin", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy hotelBillId từ bảng Bill
        BillRepository billRepo = new BillRepository(this);
        List<Bill> bills = billRepo.getBillByUserId(String.valueOf(userId));
        int hotelBillId = -1;

        for (Bill bill : bills) {
            if (bill.getHotelBillId() != 0) {
                HotelBillRepository hbr = new HotelBillRepository(this);
                HotelBill temp = hbr.getHotelBillById(bill.getHotelBillId());
                if (temp != null && temp.getIdHotel() == hotelId) {
                    hotelBillId = temp.getId(); // lấy id để dùng lại
                    break;
                }
            }
        }

        if (hotelBillId == -1) {
            Toast.makeText(this, "Không tìm thấy hóa đơn", Toast.LENGTH_SHORT).show();
            return;
        }

        HotelBillRepository hotelBillRepository = new HotelBillRepository(this);
        HotelBill hotelBill = hotelBillRepository.getHotelBillById(hotelBillId);

        if (hotelBill != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvDate.setText(sdf.format(hotelBill.getDate()));
            tvPrice.setText(hotelBill.getPrice() + " $");
            tvTime.setText(String.valueOf(hotelBill.getTime()));
            tvHotelId.setText(String.valueOf(hotelBill.getIdHotel()));
            tvTicket.setText(String.valueOf(hotelBill.getTicketNumber()));

        }
    }
}