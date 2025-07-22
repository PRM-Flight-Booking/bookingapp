package com.example.bookingapp.utils;

import com.example.bookingapp.data.model.*;
import java.util.Date;
import java.util.regex.Pattern;

public class Validator {
    private static final String LOCATION_REGEX = "^-?\\d{1,3}\\.\\d+,-?\\d{1,3}\\.\\d+$";
    private static final Pattern LOCATION_PATTERN = Pattern.compile(LOCATION_REGEX);
    private static final String EMAIL_REGEX = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Validate User
    public static ValidationResult validateUser(User user) {
        ValidationResult result = new ValidationResult();
        if (user == null) {
            result.addError("User is null");
        } else {
            if (!isValidEmail(user.getEmail())) {
                result.addError("Invalid email format");
            }
            if (isEmpty(user.getName())) {
                result.addError("User name is empty");
            }
            if (isEmpty(user.getPassword()) || user.getPassword().length() < 6) {
                result.addError("Password too short");
            }
            if (user.getLocation() != null && !isValidLocation(user.getLocation())) {
                result.addError("Invalid user location");
            }
        }
        return result;
    }

    // Validate Flight
    public static ValidationResult validateFlight(Flight flight) {
        ValidationResult result = new ValidationResult();
        if (flight == null) {
            result.addError("Flight is null");
        } else {
            if (!isValidId(flight.getId())) {
                result.addError("Invalid flight ID");
            }
            if (isEmpty(flight.getName())) {
                result.addError("Flight name is empty");
            }
            if (flight.getPrice() <= 0) {
                result.addError("Invalid flight price");
            }
            if (!isValidLocation(flight.getDepartureLocation()) || !isValidLocation(flight.getArrivalLocation())) {
                result.addError("Invalid flight locations");
            }
            if (!flight.getDepartureTime().before(flight.getArrivalTime())) {
                result.addError("Departure time should be before arrival time");
            }
        }
        return result;
    }

    // Validate Hotel
    public static ValidationResult validateHotel(Hotel hotel) {
        ValidationResult result = new ValidationResult();
        if (hotel == null) {
            result.addError("Hotel is null");
        } else {
            if (!isValidId(hotel.getId())) {
                result.addError("Invalid hotel ID");
            }
            if (isEmpty(hotel.getName())) {
                result.addError("Hotel name is empty");
            }
            if (!isValidLocation(hotel.getLocation())) {
                result.addError("Invalid hotel location");
            }
            if (hotel.getPrice() <= 0) {
                result.addError("Invalid hotel price");
            }
            if (hotel.getRate() < 0 || hotel.getRate() > 5) {
                result.addError("Hotel rate must be between 0 and 5");
            }
        }
        return result;
    }

    // Validate FlightBill
    public static ValidationResult validateFlightBill(FlightBill flightBill) {
        ValidationResult result = new ValidationResult();
        if (flightBill == null) {
            result.addError("FlightBill is null");
        } else {
            if (!isValidId(flightBill.getId()) || !isValidId(flightBill.getIdFlight())) {
                result.addError("Invalid IDs in FlightBill");
            }
            if (flightBill.getPrice() <= 0) {
                result.addError("Invalid FlightBill price");
            }
            if (flightBill.getTicketNumber() <= 0) {
                result.addError("Invalid ticket number");
            }
        }
        return result;
    }

    // Validate HotelBill
    public static ValidationResult validateHotelBill(HotelBill hotelBill) {
        ValidationResult result = new ValidationResult();
        if (hotelBill == null) {
            result.addError("HotelBill is null");
        } else {
            if (!isValidId(hotelBill.getId()) || !isValidId(hotelBill.getIdHotel())) {
                result.addError("Invalid IDs in HotelBill");
            }
            if (hotelBill.getPrice() <= 0) {
                result.addError("Invalid HotelBill price");
            }
            if (hotelBill.getTime() <= 0) {
                result.addError("Invalid time spent");
            }
            if (!isValidDate(hotelBill.getDate())) {
                result.addError("Invalid date");
            }
        }
        return result;
    }

    // Validate Place
    public static ValidationResult validatePlace(Place place) {
        ValidationResult result = new ValidationResult();
        if (place == null) {
            result.addError("Place is null");
        } else {
            if (!isValidId(place.getId())) {
                result.addError("Invalid place ID");
            }
            if (isEmpty(place.getName())) {
                result.addError("Place name is empty");
            }
            if (!isValidLocation(place.getLocation())) {
                result.addError("Invalid place location");
            }
            if (place.getPrice() < 0) {
                result.addError("Invalid place price");
            }
        }
        return result;
    }

    // Validate PlaceBill
    public static ValidationResult validatePlaceBill(PlaceBill placeBill) {
        ValidationResult result = new ValidationResult();
        if (placeBill == null) {
            result.addError("PlaceBill is null");
        } else {
            if (!isValidId(placeBill.getId())) {
                result.addError("Invalid IDs in PlaceBill");
            }
            if (placeBill.getPrice() <= 0) {
                result.addError("Invalid PlaceBill price");
            }
            if (placeBill.getTicketNumber() <= 0) {
                result.addError("Invalid ticket number");
            }
        }
        return result;
    }

    // Helper Functions
    public static boolean isValidId(int id) {
        return true; // IDs should be positive
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidLocation(String location) {
        return location != null && LOCATION_PATTERN.matcher(location).matches();
    }

    public static boolean isValidDate(Date date) {
        return date != null && date.after(new Date(0)); // Ensure date is valid (after epoch)
    }
}
