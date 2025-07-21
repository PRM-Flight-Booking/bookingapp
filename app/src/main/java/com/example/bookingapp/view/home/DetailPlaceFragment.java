package com.example.bookingapp.view.home;

import static com.example.bookingapp.utils.Tool.convertToAddress;
import static com.example.bookingapp.utils.Tool.parseLocation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Place;
import com.example.bookingapp.data.repository.PlaceRepository;
import com.example.bookingapp.databinding.FragmentDetailPlaceBinding;
import com.example.bookingapp.view.adapter.GalleryAdapter;
import com.example.bookingapp.view.booking.BookingActivity;
import com.example.bookingapp.viewmodel.AppViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailPlaceFragment extends Fragment implements OnMapReadyCallback {
    private FragmentDetailPlaceBinding binding;
    private int idPlace;
    private PlaceRepository placeRepository;
    private Place place;
    private boolean isSaved = false;
    private GoogleMap mMap;
    private AppViewModel viewModel;

    public DetailPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            idPlace = getArguments().getInt("placeId", -1);
            isSaved = getArguments().getInt("isSaved", 0) == 1;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailPlaceBinding.inflate(inflater, container, false);

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        NavController navController = NavHostFragment.findNavController(DetailPlaceFragment.this);
                        navController.popBackStack();
                    }
                }
        );
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        binding.btnBack.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(DetailPlaceFragment.this);
            navController.popBackStack();
        });
        placeRepository = new PlaceRepository(getContext());
        place = placeRepository.getPlaceById(idPlace);
        place.setSaved(isSaved);
        bindData(place);

        // Load Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }



        return binding.getRoot();
    }
    @SuppressLint("DefaultLocale")
    private void bindData(Place place){
        //binding.btnSave.setImageResource(place.isSaved()?R.drawable.baseline_bookmark_added_24:R.drawable.baseline_bookmark_border_24);
        binding.tvDestinationName.setText(place.getName());
        binding.tvLocation.setText(convertToAddress(getContext(),place.getLocation()));
        binding.tvRating.setText(String.format("%.1f",place.getRating()));
        binding.tvAboutTrip.setText(place.getOverview());
        binding.tvPrice.setText(String.format("%.1f$",place.getPrice()));
        binding.btnSave.setImageResource(place.isSaved()? R.drawable.baseline_bookmark_added_24: R.drawable.baseline_bookmark_border_24);


        Picasso.get()
                .load(Uri.parse(place.getImage().get(0)))
                .placeholder(R.drawable.default_img)
                .error(R.drawable.error_img)
                .into(binding.ivDestination);

        binding.ivDestination.setOnClickListener(v->{
            FragmentActivity activity = (FragmentActivity) v.getContext();
            ImageDialogFragment.newInstance(place.getImage().get(0))
                    .show(activity.getSupportFragmentManager(), "image_dialog");
        });

        List<String> urls = place.getImage().subList(1,place.getImage().size());

        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), urls);
        binding.rvGallery.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvGallery.setAdapter(galleryAdapter);

        binding.btnSave.setOnClickListener(v->{
            binding.btnSave.setImageResource(!place.isSaved()? R.drawable.baseline_bookmark_added_24: R.drawable.baseline_bookmark_border_24);
            viewModel.savedPlace(place);
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnBookNow.setOnClickListener(v -> {
            v.setEnabled(false);
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), BookingActivity.class);
                intent.putExtra("placeId", place.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                v.setEnabled(true);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng placeLocation = parseLocation(place.getLocation());
        if(placeLocation!=null){
            mMap.addMarker(new MarkerOptions().position(placeLocation).title(place.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15f));
        }
    }

}