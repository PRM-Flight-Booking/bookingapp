package com.example.bookingapp.data.repository;

import static com.example.bookingapp.utils.Tool.calculateDistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookingapp.data.database.DatabaseHelper;
import com.example.bookingapp.data.model.Hotel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HotelRepository {
    private final SQLiteDatabase db;

    public HotelRepository(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Create a new Hotel in the database.
     * Returns the generated ID on success, -1 on failure.
     */
    public long createHotel(Hotel hotel) {
        if (hotel == null) return -1;

        ContentValues values = new ContentValues();
        values.put("name", hotel.getName());
        values.put("location", hotel.getLocation());
        values.put("images", hotel.getImages() != null ? String.join(",", hotel.getImages()) : null); // Convert List to CSV
        values.put("rate", hotel.getRate());
        values.put("price", hotel.getPrice());
        values.put("overview", hotel.getOverview());

        return db.insert("Hotel", null, values); // Returns generated row ID
    }

    /**
     * Convert Cursor to Hotel object.
     */
    private Hotel convertCursorToHotel(Cursor cursor) {
        if (cursor == null) return null;

        String imagesString = cursor.getString(cursor.getColumnIndexOrThrow("images"));
        List<String> images = imagesString != null ? List.of(imagesString.split(",")) : new ArrayList<>();

        return new Hotel(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Auto-incremented int ID
                images,
                cursor.getString(cursor.getColumnIndexOrThrow("location")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                cursor.getFloat(cursor.getColumnIndexOrThrow("rate")),
                cursor.getString(cursor.getColumnIndexOrThrow("overview"))
        );
    }

    /**
     * Get all Hotels from the database.
     */
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Hotel", null)) {
            while (cursor.moveToNext()) {
                Hotel hotel = convertCursorToHotel(cursor);
                if (hotel != null) {
                    hotels.add(hotel);
                }
            }
        }
        return hotels;
    }

    /**
     * Get a single Hotel by its ID.
     */
    public Hotel getHotelById(int id) {
        try (Cursor cursor = db.rawQuery("SELECT * FROM Hotel WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                return convertCursorToHotel(cursor);
            }
        }
        return null;
    }

    /**
     * Find Hotels near a given location within a radius.
     * Results are sorted by **distance** (ascending) and **rating** (descending).
     */
    public List<Hotel> getHotelsByLocation(String location, int radius) {
        List<Hotel> hotels = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Hotel", null)) {
            while (cursor.moveToNext()) {
                String dbLocation = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                double distance = calculateDistance(location, dbLocation);

                if (distance <= radius) {
                    Hotel hotel = convertCursorToHotel(cursor);
                    if (hotel != null) {
                        hotel.setDistance(distance); // Add distance for sorting
                        hotels.add(hotel);
                    }
                }
            }
        }

        // Sort by distance first (ascending), then by rating (descending)
        hotels.sort(Comparator.comparingDouble(Hotel::getDistance).thenComparingDouble(h -> -h.getRate()));

        return hotels;
    }
    /**
     * Update an existing Hotel in the database.
     * Returns true if the update is successful, false otherwise.
     */
    public boolean updateHotel(Hotel hotel) {
        if (hotel == null || hotel.getId() <= 0) return false;

        ContentValues values = new ContentValues();
        values.put("name", hotel.getName());
        values.put("location", hotel.getLocation());
        values.put("images", hotel.getImages() != null ? String.join(",", hotel.getImages()) : null); // Convert List to CSV
        values.put("rate", hotel.getRate());
        values.put("price", hotel.getPrice());
        values.put("overview", hotel.getOverview());

        int rowsAffected = db.update("Hotel", values, "id = ?", new String[]{String.valueOf(hotel.getId())});
        return rowsAffected > 0;
    }

    /**
     * Delete a Hotel by its ID.
     */
    public boolean deleteHotel(int id) {
        int rowsDeleted = db.delete("Hotel", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }
}
