package com.example.bookingapp.view.home;

import static com.example.bookingapp.utils.Constant.ALL_PLACE;
import static com.example.bookingapp.utils.Constant.POPULAR_PLACE;
import static com.example.bookingapp.utils.Constant.TOP_PLACE;

import android.annotation.SuppressLint;
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
import com.example.bookingapp.databinding.FragmentHomeBinding;
import com.example.bookingapp.view.adapter.PlaceAdapter;
import com.example.bookingapp.viewmodel.AppViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private PlaceAdapter allPlaceAdapter;
    private PlaceAdapter mostPopularAdapter;
    private PlaceAdapter topPlaceAdapter;
    private NavController navController;
    private AppViewModel viewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Initialize adapters with empty lists
        allPlaceAdapter = new PlaceAdapter(
                requireContext(),
                new ArrayList<>(),
                PlaceAdapter.TYPE_THEME_2,
                this::onItemClick,
                this::onBookmarkClick
        );
        mostPopularAdapter = new PlaceAdapter(
                requireContext(),
                new ArrayList<>(),
                PlaceAdapter.TYPE_THEME_3,
                this::onItemClick,
                this::onBookmarkClick
        );
        topPlaceAdapter = new PlaceAdapter(
                requireContext(),
                new ArrayList<>(),
                PlaceAdapter.TYPE_THEME_1,
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Set up user info
        binding.address.setText(viewModel.getUser().getAddress(requireContext()));
        binding.avt.setImageResource(viewModel.getUser().getAvatar());

        // Set up RecyclerViews
        binding.allPackage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.popularPackage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.topPackage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Set adapters
        binding.allPackage.setAdapter(allPlaceAdapter);
        binding.popularPackage.setAdapter(mostPopularAdapter);
        binding.topPackage.setAdapter(topPlaceAdapter);

        // Set up "See All" buttons
        setupButtonSeeAll();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (navController == null) {
            navController = NavHostFragment.findNavController(this);
        }

        // Observe LiveData from ViewModel
        viewModel.getAllPlaceList().observe(getViewLifecycleOwner(), places -> {
            allPlaceAdapter.updateAdapter(places);
            binding.numItemAll.setText(places.size() + " Places");
        });

        viewModel.getPopularPlaceList().observe(getViewLifecycleOwner(), mostPopularAdapter::updateAdapter);
        viewModel.getTopPlaceList().observe(getViewLifecycleOwner(), topPlaceAdapter::updateAdapter);
    }

    private void setupButtonSeeAll() {
        binding.seeAllPlace.setOnClickListener(v -> {
            if (navController != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("category", ALL_PLACE);
                navController.navigate(R.id.action_navigation_home_to_seeAllFragment, bundle);
            }
        });
        binding.seeAllPopular.setOnClickListener(v -> {
            if (navController != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("category", POPULAR_PLACE);
                navController.navigate(R.id.action_navigation_home_to_seeAllFragment, bundle);
            }
        });
        binding.seeAllTop.setOnClickListener(v -> {
            if (navController != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("category", TOP_PLACE);
                navController.navigate(R.id.action_navigation_home_to_seeAllFragment, bundle);
            }
        });
    }
}