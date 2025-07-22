package com.example.bookingapp.view.admin.dashboard.flight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Flight;
import com.example.bookingapp.data.repository.FlightRepository;
import com.example.bookingapp.view.admin.dashboard.DashBoardFragment;
import com.example.bookingapp.viewmodel.AdminViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditFlightFragment extends Fragment {

    private AdminViewModel adminViewModel;
    private Flight currentFlight;
    private FlightRepository flightRepository;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_flight, container, false);

        // Khởi tạo AdminViewModel
        adminViewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        // Khởi tạo FlightRepository
        flightRepository = new FlightRepository(requireContext());

        // Ánh xạ các view
        TextInputEditText edtFlightNumber = view.findViewById(R.id.edtFlightNumber);
        TextInputEditText edtPrice = view.findViewById(R.id.edtPrice);
        Button btnSave = view.findViewById(R.id.btnSave);
        ImageButton btnBack = view.findViewById(R.id.btnBack);

        // Lấy dữ liệu Flight từ AdminViewModel
        adminViewModel.getFlight().observe(getViewLifecycleOwner(), flight -> {
            if (flight != null) {
                currentFlight = flight;
                // Hiển thị dữ liệu hiện tại
                edtFlightNumber.setText(flight.getName());
                edtPrice.setText(String.valueOf(flight.getPrice()));
            }
        });

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.remove(this);
            transaction.commit();

            View container2 = getActivity().findViewById(R.id.fragment_container_2);
            if (container2 != null) {
                container2.setVisibility(View.GONE);
            }

            // Hiển thị lại TabLayout
            Fragment dashBoardFragment = getParentFragmentManager().findFragmentById(R.id.frame_container);
            if (dashBoardFragment instanceof DashBoardFragment) {
                View tabLayout = dashBoardFragment.getView().findViewById(R.id.tabLayout);
                if (tabLayout != null) {
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }

            // Hiển thị lại BottomNavigationView (nếu đã ẩn trước đó)
            View bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.VISIBLE);
            }
        });

        // Xử lý nút Save
        btnSave.setOnClickListener(v -> {
            if (currentFlight == null) {
                Toast.makeText(getContext(), "No flight data available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy dữ liệu từ các trường nhập liệu
            String newFlightNumber = edtFlightNumber.getText().toString().trim();
            String newPriceStr = edtPrice.getText().toString().trim();

            // Kiểm tra dữ liệu hợp lệ
            if (newFlightNumber.isEmpty() || newPriceStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            float newPrice;
            try {
                newPrice = Float.parseFloat(newPriceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật dữ liệu Flight
            currentFlight.setName(newFlightNumber);
            currentFlight.setPrice(newPrice);

            // Lưu dữ liệu vào database
            executorService.execute(() -> {
                boolean success = flightRepository.updateFlight(currentFlight);
                requireActivity().runOnUiThread(() -> {
                    if (success) {
                        // Cập nhật dữ liệu trong AdminViewModel để các fragment khác nhận được thay đổi
                        adminViewModel.setFlight(currentFlight);
                        adminViewModel.notifyFlightUpdated(); // Thông báo Flight đã được cập nhật
                        Toast.makeText(getContext(), "Flight updated successfully", Toast.LENGTH_SHORT).show();

                        // Quay lại DashBoardFragment
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.remove(this);
                        transaction.commit();

                        View container2 = getActivity().findViewById(R.id.fragment_container_2);
                        if (container2 != null) {
                            container2.setVisibility(View.GONE);
                        }

                        // Hiển thị lại TabLayout
                        Fragment dashBoardFragment = getParentFragmentManager().findFragmentById(R.id.frame_container);
                        if (dashBoardFragment instanceof DashBoardFragment) {
                            View tabLayout = dashBoardFragment.getView().findViewById(R.id.tabLayout);
                            if (tabLayout != null) {
                                tabLayout.setVisibility(View.VISIBLE);
                            }
                        }

                        // Hiển thị lại BottomNavigationView (nếu đã ẩn trước đó)
                        View bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
                        if (bottomNav != null) {
                            bottomNav.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to update flight", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        return view;
    }
}