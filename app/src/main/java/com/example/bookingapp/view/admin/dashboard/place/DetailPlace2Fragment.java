package com.example.bookingapp.view.admin.dashboard.place;

import android.app.AlertDialog;
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
import com.example.bookingapp.viewmodel.AdminViewModel;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailPlace2Fragment extends Fragment {

    private AdminViewModel adminViewModel;
    private Place currentPlace;
    private PlaceRepository placeRepository;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_place_2, container, false);

        // Khởi tạo AdminViewModel
        adminViewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        // Khởi tạo PlaceRepository (giả sử bạn đã có một lớp PlaceRepository để xử lý database)
        placeRepository = new PlaceRepository(getContext()); // TODO: Khởi tạo PlaceRepository thực tế

        // Xử lý nút Back
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.remove(this);
            transaction.commit();

            View container2 = getActivity().findViewById(R.id.fragment_container_2);
            if (container2 != null) {
                container2.setVisibility(View.GONE);
            }

            // Hiển thị lại BottomNavigationView (nếu đã ẩn trước đó)
            View bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.VISIBLE);
            }
        });

        // Xử lý nút Edit Place
        Button btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            EditPlaceFragment fragment = new EditPlaceFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_2, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Xử lý nút Delete Place
        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            if (currentPlace != null) {
                showDeleteConfirmationDialog(currentPlace);
            } else {
                Toast.makeText(getContext(), "No place data available", Toast.LENGTH_SHORT).show();
            }
        });

        // Hiển thị dữ liệu Place
        adminViewModel.getPlace().observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                currentPlace = place;
                TextView placeName = view.findViewById(R.id.tvDestinationName);
                placeName.setText(place.getName());
                TextView placeOverview = view.findViewById(R.id.tvAboutTrip);
                placeOverview.setText(place.getOverview());
                ImageView placeImage = view.findViewById(R.id.vpImages);
                Picasso.get().load(place.getImage().get(0)).into(placeImage);
                TextView placeRating = view.findViewById(R.id.tvRating);
                placeRating.setText(String.format("%.1f", Math.min(place.getRating(), 5.0f)));
                TextView placePrice = view.findViewById(R.id.tvPrice);
                placePrice.setText(String.format("$%.2f", place.getPrice()));
                TextView placeLocation = view.findViewById(R.id.tvLocation);
                placeLocation.setText(place.getLocation());
            }
        });

        return view;
    }

    // Phương thức hiển thị hộp thoại xác nhận xóa
    private void showDeleteConfirmationDialog(Place place) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this place?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Xử lý xóa Place
                    executorService.execute(() -> {
                        try {
                            placeRepository.deletePlace(place.getId()); // Xóa Place từ database
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Place deleted successfully", Toast.LENGTH_SHORT).show();

                                // Quay lại DashBoardFragment
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.remove(this);
                                transaction.commit();

                                View container2 = getActivity().findViewById(R.id.fragment_container_2);
                                if (container2 != null) {
                                    container2.setVisibility(View.GONE);
                                }

                                // Hiển thị lại BottomNavigationView (nếu đã ẩn trước đó)
                                View bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
                                if (bottomNav != null) {
                                    bottomNav.setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (Exception e) {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Failed to delete place: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}