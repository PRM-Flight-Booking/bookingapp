package com.example.bookingapp.view.mytrip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.PlaceBill;
import com.example.bookingapp.data.repository.PlaceBillRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlaceBillDetailActivity extends AppCompatActivity {
    private TextView tvPrice, tvTicketNumber, tvIdPlace, tvIdUser, tvDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_bill_detail);

        tvPrice = findViewById(R.id.tvPrice);
        tvTicketNumber = findViewById(R.id.tvTicketNumber);
        tvIdPlace = findViewById(R.id.tvIdPlace);
        tvIdUser = findViewById(R.id.tvIdUser);
        tvDate = findViewById(R.id.tvDate);

        Button btnCancelTicket = findViewById(R.id.btnCancelTicket);
        btnCancelTicket.setOnClickListener(v -> finish());

        int placeBillId = getIntent().getIntExtra("placeBillId", -1);
        if (placeBillId == -1) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        PlaceBillRepository repo = new PlaceBillRepository(this);
        PlaceBill pb = repo.getPlaceBillById(placeBillId);
        if (pb != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            tvPrice.setText(pb.getPrice() + " $");
            tvTicketNumber.setText(String.valueOf(pb.getTicketNumber()));
            tvIdPlace.setText(String.valueOf(pb.getIdPlace()));
            tvIdUser.setText(String.valueOf(pb.getIdUser()));
            tvDate.setText(sdf.format(pb.getDate()));
        } else {
            Toast.makeText(this, "Không tìm thấy PlaceBill", Toast.LENGTH_SHORT).show();
        }

    }
}