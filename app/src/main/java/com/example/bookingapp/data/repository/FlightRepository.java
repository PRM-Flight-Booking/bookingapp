package com.example.bookingapp.data.repository;

import static com.example.bookingapp.utils.Constant.DATE_TIME_FORMAT;
import static com.example.bookingapp.utils.Tool.calculateDistance;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.privacysandbox.tools.core.model.Constant;

import com.example.bookingapp.data.database.DatabaseHelper;
import com.example.bookingapp.data.model.Flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FlightRepository {
    private final SQLiteDatabase db;

    public FlightRepository(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Create a new Flight in the database.
     */
    public long createFlight(Flight flight) {
        if (flight == null) return -1;

        ContentValues values = new ContentValues();
        values.put("name", flight.getName());
        values.put("price", flight.getPrice());
        values.put("departureLocation", flight.getDepartureLocation());
        values.put("arrivalLocation", flight.getArrivalLocation());

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        values.put("departureTime", sdf.format(flight.getDepartureTime()));
        values.put("arrivalTime", sdf.format(flight.getArrivalTime()));

        return db.insert("Flight", null, values); // Returns generated ID
    }

    /**
     * Convert Cursor to Flight object.
     */
    private Flight convertCursorToFlight(Cursor cursor) {
        if (cursor == null) return null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());

            return new Flight(
                    sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("arrivalTime"))),
                    cursor.getString(cursor.getColumnIndexOrThrow("departureLocation")),
                    sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("departureTime"))),
                    cursor.getString(cursor.getColumnIndexOrThrow("arrivalLocation")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Auto-incremented ID
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("price"))
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all Flights from the database.
     */
    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Flight", null)) {
            while (cursor.moveToNext()) {
                Flight flight = convertCursorToFlight(cursor);
                if (flight != null) {
                    flights.add(flight);
                }
            }
        }
        return flights;
    }

    /**
     * Get Flight by ID.
     */
    public Flight getFlightById(int id) {
        try (Cursor cursor = db.rawQuery("SELECT * FROM Flight WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                return convertCursorToFlight(cursor);
            }
        }
        return null;
    }

    /**
     * Find Flights by Departure & Destination Location within a given radius.
     */
    public List<Flight> getFlightsByLocation(String departureLocation, String arrivalLocation, int radius) {
        List<Flight> flights = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Flight", null)) {
            while (cursor.moveToNext()) {
                String dbDepLoc = cursor.getString(cursor.getColumnIndexOrThrow("departureLocation"));
                String dbDestLoc = cursor.getString(cursor.getColumnIndexOrThrow("arrivalLocation"));

                double depDistance = calculateDistance(departureLocation, dbDepLoc);
                double destDistance = calculateDistance(arrivalLocation, dbDestLoc);

                if (depDistance <= radius && destDistance <= radius) {
                    flights.add(convertCursorToFlight(cursor));
                }
            }
        }
        return flights;
    }

    /**
     * Update Flight details.
     */
    public boolean updateFlight(Flight flight) {
        if (flight == null) return false;

        ContentValues values = new ContentValues();
        values.put("name", flight.getName());
        values.put("price", flight.getPrice());
        values.put("departureLocation", flight.getDepartureLocation());
        values.put("arrivalLocation", flight.getArrivalLocation());

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        values.put("departureTime", sdf.format(flight.getDepartureTime()));
        values.put("arrivalTime", sdf.format(flight.getArrivalTime()));

        int rows = db.update("Flight", values, "id = ?", new String[]{String.valueOf(flight.getId())});
        return rows > 0;
    }

    /**
     * Delete a Flight by ID.
     */
    public boolean deleteFlight(int id) {
        int rowsDeleted = db.delete("Flight", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }
}
