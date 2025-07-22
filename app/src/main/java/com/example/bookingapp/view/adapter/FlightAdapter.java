package com.example.bookingapp.view.adapter;

import static com.example.bookingapp.utils.Tool.calculateFlightDuration;
import static com.example.bookingapp.utils.Tool.convertToAddress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.data.model.Flight;
import com.example.bookingapp.databinding.ItemFlightBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private final Context context;
    private List<Flight> flightList;
    private final OnFlightClickListener listener;

    public interface OnFlightClickListener {
        void onFlightClick(Flight flight);
    }

    public FlightAdapter(Context context, List<Flight> flightList, OnFlightClickListener listener) {
        this.context = context;
        this.flightList = flightList;
        this.listener = listener;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateFlightList(List<Flight> newFlightList) {
        this.flightList = new ArrayList<>(newFlightList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFlightBinding binding = ItemFlightBinding.inflate(LayoutInflater.from(context), parent, false);
        return new FlightViewHolder(binding);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        // Bind data to views
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        holder.binding.departureTime.setText(dateFormat.format(flight.getDepartureTime()));

        holder.binding.departureLocation.setText(convertToAddress(context, flight.getDepartureLocation()));

        holder.binding.arriveTime.setText(dateFormat.format(flight.getArrivalTime()));

        holder.binding.arriveLocation.setText(convertToAddress(context, flight.getArrivalLocation()));
        holder.binding.name.setText(flight.getName());
        holder.binding.price.setText("$" + String.format("%.1f", flight.getPrice()));
        holder.binding.time.setText(calculateFlightDuration(flight.getDepartureTime(), flight.getArrivalTime()));

        // Set the click listener for the flight item
        holder.itemView.setOnClickListener(v -> listener.onFlightClick(flight));
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        ItemFlightBinding binding;

        public FlightViewHolder(ItemFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
