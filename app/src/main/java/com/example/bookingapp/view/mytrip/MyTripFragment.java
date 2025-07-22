package com.example.bookingapp.view.mytrip;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bookingapp.R;
import com.example.bookingapp.data.database.ShareReferenceHelper;
import com.example.bookingapp.data.model.Bill;
import com.example.bookingapp.data.model.Place;
import com.example.bookingapp.data.model.PlaceBill;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.data.repository.BillRepository;
import com.example.bookingapp.data.repository.PlaceBillRepository;
import com.example.bookingapp.data.repository.PlaceRepository;
import com.example.bookingapp.databinding.FragmentMyTripBinding;
import com.example.bookingapp.view.adapter.TripAdapter;
import java.util.ArrayList;
import java.util.List;

public class MyTripFragment extends Fragment {

    private FragmentMyTripBinding binding;
    private PlaceRepository placeRepository;
    private BillRepository billRepository;
    private PlaceBillRepository placeBillRepository;
    private User user;

    public MyTripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyTripBinding.inflate(inflater, container, false);
        binding.topPackage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy thông tin người dùng từ SharedPreferences
        ShareReferenceHelper shareReferenceHelper = new ShareReferenceHelper(requireContext());
        user = (User) shareReferenceHelper.get("user", User.class);
        binding.setUser(user);

        Log.d("MyTripFragment", "User ID: " + (user != null ? user.getId() : "null"));

        // Khởi tạo các repository
        placeRepository = new PlaceRepository(getContext());
        billRepository = new BillRepository(getContext());
        placeBillRepository = new PlaceBillRepository(getContext());

        // Cấu hình RecyclerView
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<Place> purchasedPlaces = new ArrayList<>();
        List<Bill> associatedBills = new ArrayList<>();

        // Lấy danh sách id của PlaceBill theo userId
        List<Integer> placeBillIds = placeBillRepository.getPlaceBillIdsByUserId(user.getId());
        Log.d("MyTripFragment", "Number of PlaceBill IDs found: " + placeBillIds.size());
        if (placeBillIds.isEmpty()) {
            Log.w("MyTripFragment", "No PlaceBills found for user ID: " + user.getId());
        }

        // Duyệt qua các id và dùng getPlaceBillById để lấy PlaceBill
        for (int placeBillId : placeBillIds) {
            Log.d("MyTripFragment", "Fetching PlaceBill ID: " + placeBillId);
            PlaceBill placeBill = placeBillRepository.getPlaceBillById(placeBillId);
            if (placeBill != null) {
                Log.d("MyTripFragment", "Found PlaceBill ID: " + placeBill.getId() + ", Place ID: " + placeBill.getIdPlace());
                Place place = placeRepository.getPlaceById(placeBill.getIdPlace());
                if (place != null) {
                    purchasedPlaces.add(place);
                    // Tìm Bill liên quan (nếu cần)
                    Bill bill = billRepository.getBillByPlaceBillId(placeBill.getId());
                    associatedBills.add(bill != null ? bill : new Bill(-1, -1, placeBill.getId(), 0, user.getId()));
                    Log.d("MyTripFragment", "Added Place: " + place.getName() + " (ID: " + place.getId() + ")");
                } else {
                    Log.w("MyTripFragment", "Place not found for PlaceBill ID: " + placeBill.getIdPlace());
                }
            } else {
                Log.w("MyTripFragment", "PlaceBill not found for ID: " + placeBillId);
            }
        }

        Log.d("MyTripFragment", "Total places found: " + purchasedPlaces.size());
        TripAdapter adapter = new TripAdapter(getContext(), purchasedPlaces, TripAdapter.TYPE_THEME_3, user, associatedBills);
        binding.topPackage.setAdapter(adapter);
        Log.d("MyTripFragment", "Adapter set with " + purchasedPlaces.size() + " items");
    }
}