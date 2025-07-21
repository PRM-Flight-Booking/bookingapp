package com.example.bookingapp.view.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Place;
import com.example.bookingapp.databinding.FragmentSavedBinding;
import com.example.bookingapp.view.adapter.PlaceAdapter;
import com.example.bookingapp.viewmodel.AppViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedFragment} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment {
    private FragmentSavedBinding binding;
    private NavController navController;
    private PlaceAdapter adapter;
    private AppViewModel viewModel;

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Initialize adapter with empty list
        adapter = new PlaceAdapter(
                requireContext(),
                new ArrayList<>(),
                PlaceAdapter.TYPE_THEME_3,
                this::onItemClick,
                this::onBookmarkClick
        );
    }

    private void onItemClick(Place place) {
        Bundle bundle = new Bundle();
        bundle.putInt("placeId", place.getId());
        bundle.putInt("isSaved", place.isSaved()?1:0);
        navController.navigate(R.id.detailPlaceFragment, bundle);
    }
    private void onBookmarkClick(Place place) {
        // Use ViewModel to handle saving/removing place
        viewModel.savedPlace(place);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);

        // Set up RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        // Observe saved places from ViewModel
        viewModel.getSavedPlaces().observe(getViewLifecycleOwner(), savedPlaces -> {
            adapter.updateAdapter(savedPlaces); // Cập nhật adapter khi danh sách thay đổi
        });
    }
}