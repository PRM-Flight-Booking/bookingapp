package com.example.bookingapp.view.home;

import static com.example.bookingapp.utils.Constant.ALL_PLACE;
import static com.example.bookingapp.utils.Constant.POPULAR_PLACE;
import static com.example.bookingapp.utils.Constant.TOP_PLACE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Place;
import com.example.bookingapp.databinding.FragmentSeeAllBinding;
import com.example.bookingapp.view.adapter.PlaceAdapter;
import com.example.bookingapp.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeeAllFragment} factory method to
 * create an instance of this fragment.
 */
public class SeeAllFragment extends Fragment {
    private Integer categoryId = 0;
    private FragmentSeeAllBinding binding;
    private PlaceAdapter placeAdapter;
    private NavController navController;
    private AppViewModel viewModel;
    private List<Place> fullPlaceList;

    public SeeAllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt("category", 0);
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Initialize adapter with empty list
        fullPlaceList = new ArrayList<>();
        placeAdapter = new PlaceAdapter(
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
        binding = FragmentSeeAllBinding.inflate(inflater, container, false);

        // Set up RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(placeAdapter);

        // Set up search box
        setupSearchBox();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        // Observe LiveData based on category
        switch (categoryId) {
            case ALL_PLACE:
                viewModel.getAllPlaceList().observe(getViewLifecycleOwner(), places -> {
                    fullPlaceList = new ArrayList<>(places); // Lưu danh sách đầy đủ để lọc
                    placeAdapter.updateAdapter(places); // Cập nhật adapter
                });
                break;
            case POPULAR_PLACE:
                viewModel.getPopularPlaceList().observe(getViewLifecycleOwner(), places -> {
                    fullPlaceList = new ArrayList<>(places); // Lưu danh sách đầy đủ để lọc
                    placeAdapter.updateAdapter(places); // Cập nhật adapter
                });
                break;
            case TOP_PLACE:
                viewModel.getTopPlaceList().observe(getViewLifecycleOwner(), places -> {
                    fullPlaceList = new ArrayList<>(places); // Lưu danh sách đầy đủ để lọc
                    placeAdapter.updateAdapter(places); // Cập nhật adapter
                });
                break;
            default:
                placeAdapter.updateAdapter(new ArrayList<>());
                fullPlaceList = new ArrayList<>();
                break;
        }
    }

    private void setupSearchBox() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
    }

    private void search(String name) {
        if (name == null || name.trim().isEmpty()) {
            placeAdapter.updateAdapter(new ArrayList<>(fullPlaceList));
            return;
        }
        List<Place> filteredList = new ArrayList<>();
        for (Place place : fullPlaceList) {
            if (place.getName().toLowerCase().contains(name.trim().toLowerCase())) {
                filteredList.add(place);
            }
        }
        placeAdapter.updateAdapter(filteredList);
    }
}