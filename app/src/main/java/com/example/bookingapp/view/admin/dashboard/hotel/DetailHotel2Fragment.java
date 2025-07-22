package com.example.bookingapp.view.admin.dashboard.hotel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookingapp.R;
import com.example.bookingapp.view.admin.dashboard.DashBoardFragment;
import com.example.bookingapp.viewmodel.AdminViewModel;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailHotel2Fragment extends Fragment {
    private AdminViewModel adminViewModel;
    private Hotel currentHotel;
    private HotelRepository hotelRepository;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_hotel_2, container, false);

        // Khởi tạo AdminViewModel và HotelRepository
        adminViewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);
        hotelRepository = new HotelRepository(requireContext());

        // Ánh xạ các view
        TextView hotelName = view.findViewById(R.id.tvHotelName);
        TextView hotelOverview = view.findViewById(R.id.tvAboutHotel);
        ImageView hotelImage = view.findViewById(R.id.hotelImage);
        TextView hotelRating = view.findViewById(R.id.tvRating);
        TextView hotelPrice = view.findViewById(R.id.tvPrice);
        TextView hotelLocation = view.findViewById(R.id.tvLocation);
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        // Lấy dữ liệu Hotel từ AdminViewModel
        adminViewModel.getHotel().observe(getViewLifecycleOwner(), hotel -> {
            if (hotel != null) {
                currentHotel = hotel;
                hotelName.setText(hotel.getName());
                hotelOverview.setText(hotel.getOverview());
                if (hotel.getImages() != null && !hotel.getImages().isEmpty()) {
                    Picasso.get().load(hotel.getImages().get(0)).into(hotelImage);
                }
                hotelRating.setText(String.format("%.1f", Math.min(hotel.getRate(), 5.0f)));
                hotelPrice.setText(String.format("$%.2f", hotel.getPrice()));
                hotelLocation.setText(hotel.getLocation());
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

            // Hiển thị lại TabLayout trong DashBoardFragment
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

        // Xử lý nút Edit
        btnEdit.setOnClickListener(v -> {
            if (currentHotel == null) {
                Toast.makeText(getContext(), "No hotel data available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển đến EditHotelFragment
            EditHotelFragment fragment = new EditHotelFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_2, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Xử lý nút Delete
        btnDelete.setOnClickListener(v -> {
            if (currentHotel == null) {
                Toast.makeText(getContext(), "No hotel data available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Xóa Hotel khỏi cơ sở dữ liệu
            executorService.execute(() -> {
                boolean success = hotelRepository.deleteHotel(currentHotel.getId());
                requireActivity().runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(getContext(), "Hotel deleted successfully", Toast.LENGTH_SHORT).show();
                        adminViewModel.notifyHotelUpdated(); // Thông báo Hotel đã được cập nhật/xóa

                        // Quay lại DashBoardFragment
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.remove(this);
                        transaction.commit();

                        View container2 = getActivity().findViewById(R.id.fragment_container_2);
                        if (container2 != null) {
                            container2.setVisibility(View.GONE);
                        }

                        // Hiển thị lại TabLayout trong DashBoardFragment
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
                        Toast.makeText(getContext(), "Failed to delete hotel", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        return view;
    }
}