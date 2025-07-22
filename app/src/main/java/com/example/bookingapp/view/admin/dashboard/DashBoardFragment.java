package com.example.bookingapp.view.admin.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.data.database.ShareReferenceHelper;
import com.example.bookingapp.view.adapter.DashBoardAdapter;
import com.example.bookingapp.view.admin.dashboard.flight.EditFlightFragment;
import com.example.bookingapp.view.admin.dashboard.hotel.DetailHotel2Fragment;
import com.example.bookingapp.view.admin.dashboard.hotel.EditHotelFragment;
import com.example.bookingapp.view.admin.dashboard.place.DetailPlace2Fragment;
import com.example.bookingapp.view.admin.dashboard.place.EditPlaceFragment;
import com.example.bookingapp.view.auth.LoginActivity;
import com.example.bookingapp.viewmodel.AdminViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class DashBoardFragment extends Fragment implements DashBoardAdapter.OnItemClickListener {


    private PlaceRepository placeRepository;
    private FlightRepository flightRepository;
    private HotelRepository hotelRepository;

    private RecyclerView recyclerViewPlaces;
    private TabLayout tabLayout;
    private DashBoardAdapter dashBoardAdapter;
    private AdminViewModel adminViewModel;

    public DashBoardFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        recyclerViewPlaces = view.findViewById(R.id.recyclerViewPlaces);
        tabLayout = view.findViewById(R.id.tabLayout);

        placeRepository = new PlaceRepository(requireContext());
        flightRepository = new FlightRepository(requireContext());
        hotelRepository = new HotelRepository(requireContext());

        // Khởi tạo AdminViewModel
        adminViewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        loadData(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadData(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Lắng nghe sự kiện Place updated
        adminViewModel.getPlaceUpdated().observe(getViewLifecycleOwner(), updated -> {
            if (updated != null && updated) {
                loadData(tabLayout.getSelectedTabPosition()); // Làm mới danh sách
            }
        });

        // Lắng nghe sự kiện Flight updated
        adminViewModel.getFlightUpdated().observe(getViewLifecycleOwner(), updated -> {
            if (updated != null && updated) {
                loadData(tabLayout.getSelectedTabPosition()); // Làm mới danh sách
            }
        });

        // Lắng nghe sự kiện Hotel updated
        adminViewModel.getHotelUpdated().observe(getViewLifecycleOwner(), updated -> {
            if (updated != null && updated) {
                loadData(tabLayout.getSelectedTabPosition()); // Làm mới danh sách
            }
        });


        Button logoutButton = view.findViewById(R.id.btLogout);
        logoutButton.setOnClickListener(v -> {
            ShareReferenceHelper shareReferenceHelper = new ShareReferenceHelper(getContext());
            shareReferenceHelper.clear();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        });


        return view;
    }

    private void loadData(int index) {
        switch (index) {
            case 1:
                getDataFlight();
                break;
            case 2:
                getDataHotel();
                break;
            default:
                getDataPlace();
        }
    }

    private void getDataPlace() {
        List<Place> places = placeRepository.getAllPlaces();
        setRecyclerViewAdapter(places);
    }

    private void getDataFlight() {
        List<Flight> flights = flightRepository.getAllFlights();
        setRecyclerViewAdapter(flights);
    }

    private void getDataHotel() {
        List<Hotel> hotels = hotelRepository.getAllHotels();
        setRecyclerViewAdapter(hotels);
    }

    private void setRecyclerViewAdapter(List<?> items) {
        dashBoardAdapter = new DashBoardAdapter(getContext(), items, this);
        recyclerViewPlaces.setAdapter(dashBoardAdapter);
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onEdit(Object item) {
        if (item == null) {
            Log.e("DashBoardFragment", "Item is null!");
            return;
        }

        if (item instanceof Place) {
            Place place = (Place) item;
            Log.d("DashBoardFragment", "Edit Place: " + place.toString());
            adminViewModel.setPlace(place);

            EditPlaceFragment fragment = new EditPlaceFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container_2, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            View container2 = getActivity().findViewById(R.id.fragment_container_2);
            if (container2 != null) {
                container2.setVisibility(View.VISIBLE);
            }

            View tabLayoutView = getView().findViewById(R.id.tabLayout);
            if (tabLayoutView != null) {
                tabLayoutView.setVisibility(View.GONE);
            }
        } else if (item instanceof Flight) {
            Flight flight = (Flight) item;
            Log.d("DashBoardFragment", "Edit Flight: " + flight.toString());
            adminViewModel.setFlight(flight);

            EditFlightFragment fragment = new EditFlightFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container_2, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            View container2 = getActivity().findViewById(R.id.fragment_container_2);
            if (container2 != null) {
                container2.setVisibility(View.VISIBLE);
            }

            View tabLayoutView = getView().findViewById(R.id.tabLayout);
            if (tabLayoutView != null) {
                tabLayoutView.setVisibility(View.GONE);
            }
        } else if (item instanceof Hotel) {
            Hotel hotel = (Hotel) item;
            Log.d("DashBoardFragment", "Edit Hotel: " + hotel.toString());
            adminViewModel.setHotel(hotel);

            EditHotelFragment fragment = new EditHotelFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container_2, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            View container2 = getActivity().findViewById(R.id.fragment_container_2);
            if (container2 != null) {
                container2.setVisibility(View.VISIBLE);
            }

            View tabLayoutView = getView().findViewById(R.id.tabLayout);
            if (tabLayoutView != null) {
                tabLayoutView.setVisibility(View.GONE);
            }
        } else {
            Log.e("DashBoardFragment", "Item is not a Place, Flight, or Hotel!");
        }
    }

    @Override
    public void onDeleteClick(Object item) {
        if (item == null) {
            Log.e("DashBoardFragment", "Item is null!");
            return;
        }
        if (item instanceof Place) {
            Place place = (Place) item;
            placeRepository.deletePlace(place.getId());
            getDataPlace();
        } else if (item instanceof Flight) {
            Flight flight = (Flight) item;
            flightRepository.deleteFlight(flight.getId());
            getDataFlight();
        } else if (item instanceof Hotel) {
            Hotel hotel = (Hotel) item;
            hotelRepository.deleteHotel(hotel.getId());
            getDataHotel();
        }
    }

    @Override
    public void onItemClick(Object item) {
        if (item instanceof Place) {
            Place place = (Place) item;
            Log.d("DashBoardFragment", "Item clicked: " + place.toString());
            adminViewModel.setPlace(place);

            DetailPlace2Fragment fragment = new DetailPlace2Fragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container_2, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            View container2 = getActivity().findViewById(R.id.fragment_container_2);
            if (container2 != null) {
                container2.setVisibility(View.VISIBLE);
            }

            View tabLayoutView = getView().findViewById(R.id.tabLayout);
            if (tabLayoutView != null) {
                tabLayoutView.setVisibility(View.GONE);
            }
        } else if (item instanceof Hotel) {
            Hotel hotel = (Hotel) item;
            Log.d("DashBoardFragment", "Item clicked: " + hotel.toString());
            adminViewModel.setHotel(hotel);

            DetailHotel2Fragment fragment = new DetailHotel2Fragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container_2, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            View container2 = getActivity().findViewById(R.id.fragment_container_2);
            if (container2 != null) {
                container2.setVisibility(View.VISIBLE);
            }

            View tabLayoutView = getView().findViewById(R.id.tabLayout);
            if (tabLayoutView != null) {
                tabLayoutView.setVisibility(View.GONE);
            }
        }
    }
}