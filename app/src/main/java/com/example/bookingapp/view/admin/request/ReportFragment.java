package com.example.bookingapp.view.admin.request;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookingapp.data.model.FlightBill;
import com.example.bookingapp.data.model.HotelBill;
import com.example.bookingapp.data.model.PlaceBill;
import com.example.bookingapp.data.repository.BillRepository;
import com.example.bookingapp.data.repository.FlightBillRepository;
import com.example.bookingapp.data.repository.HotelBillRepository;
import com.example.bookingapp.data.repository.PlaceBillRepository;
import com.example.bookingapp.data.repository.UserRepository;
import com.example.bookingapp.databinding.FragmentReportBinding;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    private UserRepository userRepository;
    private PlaceBillRepository placeBillRepository;
    private FlightBillRepository flightBillRepository;
    private HotelBillRepository hotelBillRepository;
    private BillRepository billRepository;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize repositories
        userRepository = new UserRepository(requireContext());
        placeBillRepository = new PlaceBillRepository(requireContext());
        flightBillRepository = new FlightBillRepository(requireContext());
        hotelBillRepository = new HotelBillRepository(requireContext());
        billRepository = new BillRepository(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        try {
            loadData();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() throws ParseException {
        // Hiển thị loading
        binding.loading.setVisibility(View.VISIBLE);

        // Tính toán và hiển thị số liệu
        int totalUsers = userRepository.getAllUsers().size();
        binding.tvTotalUsers.setText(String.valueOf(totalUsers));

        // Tính tổng doanh thu từ vé địa điểm
        float placeRevenue = 0;
        List<PlaceBill> placeBills = placeBillRepository.getAllPlaceBills();
        for (PlaceBill bill : placeBills) {
            placeRevenue += bill.getPrice();
        }
        binding.tvPlaceRevenue.setText(formatCurrency(placeRevenue));

        // Tính tổng doanh thu từ vé máy bay
        float flightRevenue = 0;
        List<FlightBill> flightBills = flightBillRepository.getAllFlightBills();
        for (FlightBill bill : flightBills) {

            flightRevenue += bill.getPrice();
        }
        binding.tvFlightRevenue.setText(formatCurrency(flightRevenue));

        // Tính tổng doanh thu từ đặt phòng khách sạn
        float hotelRevenue = 0;
        List<HotelBill> hotelBills = hotelBillRepository.getAllHotelBills();
        for (HotelBill bill : hotelBills) {

            hotelRevenue += bill.getPrice();
        }
        binding.tvHotelRevenue.setText(formatCurrency(hotelRevenue));

        // Tính tổng doanh thu
        float totalRevenue = placeRevenue + flightRevenue + hotelRevenue;
        binding.tvTotalRevenue.setText(formatCurrency(totalRevenue));

        // Ẩn loading
        binding.loading.setVisibility(View.GONE);
    }

    private String formatCurrency(float amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        return format.format(amount);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}