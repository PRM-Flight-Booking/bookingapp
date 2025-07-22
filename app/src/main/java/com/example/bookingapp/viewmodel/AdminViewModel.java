package com.example.bookingapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookingapp.data.model.Flight;
import com.example.bookingapp.data.model.Hotel;
import com.example.bookingapp.data.model.Place;

public class AdminViewModel extends ViewModel {
    private final MutableLiveData<Place> selectedPlace = new MutableLiveData<>();
    private final MutableLiveData<Flight> selectedFlight = new MutableLiveData<>();
    private final MutableLiveData<Hotel> selectedHotel = new MutableLiveData<>();
    private final MutableLiveData<Boolean> placeUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> flightUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> hotelUpdated = new MutableLiveData<>(); // Thêm LiveData cho Hotel

    public void setPlace(Place place) {
        selectedPlace.setValue(place);
    }

    public void setFlight(Flight flight) {
        selectedFlight.setValue(flight);
    }

    public void setHotel(Hotel hotel) {
        selectedHotel.setValue(hotel);
    }

    public void notifyPlaceUpdated() {
        placeUpdated.setValue(true);
    }

    public void notifyFlightUpdated() {
        flightUpdated.setValue(true);
    }

    public void notifyHotelUpdated() { // Thêm phương thức notifyHotelUpdated
        hotelUpdated.setValue(true);
    }

    public LiveData<Place> getPlace() {
        return selectedPlace;
    }

    public LiveData<Flight> getFlight() {
        return selectedFlight;
    }

    public LiveData<Hotel> getHotel() {
        return selectedHotel;
    }

    public LiveData<Boolean> getPlaceUpdated() {
        return placeUpdated;
    }

    public LiveData<Boolean> getFlightUpdated() {
        return flightUpdated;
    }

    public LiveData<Boolean> getHotelUpdated() { // Thêm getter cho hotelUpdated
        return hotelUpdated;
    }
}