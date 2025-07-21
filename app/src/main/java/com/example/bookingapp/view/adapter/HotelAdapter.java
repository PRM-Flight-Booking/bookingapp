package com.example.bookingapp.view.adapter;

import static com.example.bookingapp.utils.Tool.calculateDistance;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.data.model.Hotel;
import com.example.bookingapp.databinding.ItemHotelBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;
    private OnHotelClickListener onHotelClickListener;
    private String placeLocation;

    // Interface for callback
    public interface OnHotelClickListener {
        void onHotelClick(Hotel hotel);
    }

    // Constructor
    public HotelAdapter(List<Hotel> hotelList,String placeLocation, OnHotelClickListener onHotelClickListener) {
        this.hotelList = hotelList;
        this.onHotelClickListener = onHotelClickListener;
        this.placeLocation = placeLocation;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHotelBinding binding = ItemHotelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HotelViewHolder(binding,onHotelClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.bind(hotel,placeLocation);
    }

    @Override
    public int getItemCount() {
        return hotelList != null ? hotelList.size() : 0;
    }

    // ViewHolder class
    public static class HotelViewHolder extends RecyclerView.ViewHolder {

        private final ItemHotelBinding binding;
        private final  OnHotelClickListener onHotelClickListener;

        public HotelViewHolder(ItemHotelBinding binding, OnHotelClickListener onHotelClickListener1) {
            super(binding.getRoot());
            this.binding = binding;
            this.onHotelClickListener = onHotelClickListener1;


        }

        @SuppressLint("DefaultLocale")
        public void bind(Hotel hotel,String placeLocation) {
            // Load the first image from the images list using Picasso
            if (hotel.getImages() != null && !hotel.getImages().isEmpty()) {
                Picasso.get()
                        .load(hotel.getImages().get(0))
                        .placeholder(R.drawable.default_img) // Placeholder while loading
                        .error(R.drawable.error_img) // Image if loading fails
                        .into(binding.hotelImage);
            } else {
                binding.hotelImage.setImageResource(R.drawable.default_location);
            }
            // Set click listener on the CardView
            binding.card.setOnClickListener(v -> {
                onHotelClickListener.onHotelClick(hotel);
            });

            // Set hotel name
            binding.hotelName.setText(hotel.getName());

            // Set rating
            binding.ratingBar.setRating(hotel.getRate());
            binding.placeRating.setText(String.format("%.1f", hotel.getRate()));

            // Set hotel details (using location as a placeholder for package)
            binding.hotelDetails.setText(hotel.getOverview());

            binding.distance.setText(String.format("%.1fkm",calculateDistance(placeLocation, hotel.getLocation())));

            // Set tag for click callback
            binding.getRoot().setTag(hotel);
            itemView.setTag(hotel);
        }
    }

    // Getter and Setter for hotelList (optional)
    public List<Hotel> getHotelList() {
        return hotelList;
    }

    public void updateHotelList(List<Hotel> hotelList) {
        this.hotelList.clear();
        this.hotelList.addAll(hotelList);
        notifyDataSetChanged();
    }

    // Setter for callback
    public void setOnHotelClickListener(OnHotelClickListener listener) {
        this.onHotelClickListener = listener;
    }
}