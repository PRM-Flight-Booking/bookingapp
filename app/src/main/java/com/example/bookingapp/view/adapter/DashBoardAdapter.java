package com.example.bookingapp.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Flight;
import com.example.bookingapp.data.model.Hotel;
import com.example.bookingapp.data.model.Place;
import com.example.bookingapp.data.repository.FlightRepository;
import com.example.bookingapp.data.repository.HotelRepository;
import com.example.bookingapp.data.repository.PlaceRepository;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.ItemViewHolder> {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Context context;
    private List<?> itemList;
    private OnItemClickListener listener;
    private PlaceRepository placeRepository;
    private FlightRepository flightRepository;
    private HotelRepository hotelRepository;

    public interface OnItemClickListener {
        void onEdit(Object item);
        void onDeleteClick(Object item);
        void onItemClick(Object item);
    }

    public DashBoardAdapter(Context context, List<?> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = itemList.get(position);
        if (item instanceof Place) {
            return R.layout.item_place_4;
        } else if (item instanceof Flight) {
            return R.layout.item_flight_2;
        } else if (item instanceof Hotel) {
            return R.layout.item_hotel_2;
        }
        return -1;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ItemViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Object item = itemList.get(position);

        if (item instanceof Place) {
            Place place = (Place) item;
            holder.txtName.setText(place.getName());
            holder.txtDetail.setText("⭐ " + place.getRating());
            Picasso.get().load(place.getImage().get(0)).into(holder.imgItem);
        } else if (item instanceof Flight) {
            Flight flight = (Flight) item;
            holder.txtName.setText(flight.getName());
            holder.txtDetail.setText(flight.getDepartureLocation() + " → " + flight.getArrivalLocation());
        } else if (item instanceof Hotel) {
            Hotel hotel = (Hotel) item;
            holder.txtName.setText(hotel.getName());
            holder.txtDetail.setText(hotel.getName());
            Picasso.get().load(hotel.getImages().get(0)).into(holder.imgItem);
        }

        // Gán sự kiện cho các nút
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog(holder.itemView.getContext(), item));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDetail;
        ImageView imgItem;
        Button btnEdit, btnDelete;

        public ItemViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == R.layout.item_place_4) {
                txtName = itemView.findViewById(R.id.txtPlaceName);
                txtDetail = itemView.findViewById(R.id.txtPlaceRating);
                imgItem = itemView.findViewById(R.id.imgPlace);
            } else if (viewType == R.layout.item_flight_2) {
                txtName = itemView.findViewById(R.id.txtFlightName);
                txtDetail = itemView.findViewById(R.id.txtFlightRoute);
                imgItem = null; // Không có ảnh trong layout
            } else if (viewType == R.layout.item_hotel_2) {
                txtName = itemView.findViewById(R.id.txtHotelName);
                txtDetail = itemView.findViewById(R.id.txtHotelLocation);
                imgItem = itemView.findViewById(R.id.imgHotel);
            }

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showDeleteConfirmationDialog(Context context, Object item) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", (dialog, which) -> listener.onDeleteClick(item))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}