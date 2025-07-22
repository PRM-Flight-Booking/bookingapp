package com.example.bookingapp.view.admin.dashboard.hotel;

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
import com.example.bookingapp.view.admin.dashboard.DashBoardFragment;
import com.example.bookingapp.viewmodel.AdminViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditHotelFragment extends Fragment {

    private AdminViewModel adminViewModel;
    private Hotel currentHotel;
    private HotelRepository hotelRepository;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_hotel, container, false);

        // Khởi tạo AdminViewModel
        adminViewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        // Khởi tạo HotelRepository
        hotelRepository = new HotelRepository(requireContext());

        // Ánh xạ các view
        TextInputEditText edtHotelName = view.findViewById(R.id.edtHotelName);
        TextInputEditText edtPrice = view.findViewById(R.id.edtPrice);
        TextInputEditText edtImageUrl = view.findViewById(R.id.edtImageUrl);
        TextInputEditText edtOverview = view.findViewById(R.id.edtOverview);
        Button btnSave = view.findViewById(R.id.btnSave);
        ImageButton btnBack = view.findViewById(R.id.btnBack);

        // Lấy dữ liệu Hotel từ AdminViewModel
        adminViewModel.getHotel().observe(getViewLifecycleOwner(), hotel -> {
            if (hotel != null) {
                currentHotel = hotel;
                // Hiển thị dữ liệu hiện tại
                edtHotelName.setText(hotel.getName());
                edtPrice.setText(String.valueOf(hotel.getPrice()));
                edtImageUrl.setText(hotel.getImages() != null && !hotel.getImages().isEmpty() ? hotel.getImages().get(0) : "");
                edtOverview.setText(hotel.getOverview());
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
            if (currentHotel == null) {
                Toast.makeText(getContext(), "No hotel data available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy dữ liệu từ các trường nhập liệu
            String newName = edtHotelName.getText().toString().trim();
            String newPriceStr = edtPrice.getText().toString().trim();
            String newImageUrl = edtImageUrl.getText().toString().trim();
            String newOverview = edtOverview.getText().toString().trim();

            // Kiểm tra dữ liệu hợp lệ
            if (newName.isEmpty() || newPriceStr.isEmpty() || newOverview.isEmpty()) {
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

            // Cập nhật dữ liệu Hotel
            currentHotel.setName(newName);
            currentHotel.setPrice(newPrice);
            currentHotel.setOverview(newOverview);

            // Cập nhật image (giả sử image là List<String> và chỉ chỉnh sửa ảnh đầu tiên)
            ArrayList<String> newImages = new ArrayList<>();
            if (!newImageUrl.isEmpty()) {
                newImages.add(newImageUrl);
            }
            currentHotel.setImages(newImages);

            // Lưu dữ liệu vào database
            executorService.execute(() -> {
                boolean success = hotelRepository.updateHotel(currentHotel);
                requireActivity().runOnUiThread(() -> {
                    if (success) {
                        // Cập nhật dữ liệu trong AdminViewModel để các fragment khác nhận được thay đổi
                        adminViewModel.setHotel(currentHotel);
                        adminViewModel.notifyHotelUpdated(); // Thông báo Hotel đã được cập nhật
                        Toast.makeText(getContext(), "Hotel updated successfully", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getContext(), "Failed to update hotel", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        return view;
    }
}