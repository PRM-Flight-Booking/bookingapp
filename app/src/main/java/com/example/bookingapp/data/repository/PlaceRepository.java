package com.example.bookingapp.data.repository;

import static com.example.bookingapp.utils.Constant.ALL_PLACE;
import static com.example.bookingapp.utils.Constant.POPULAR_PLACE;
import static com.example.bookingapp.utils.Constant.TOP_PLACE;
import static com.example.bookingapp.utils.Tool.calculateDistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookingapp.data.database.DatabaseHelper;
import com.example.bookingapp.data.model.Place;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaceRepository {
    private final SQLiteDatabase db;

    public PlaceRepository(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Create a new Place in the database.
     * Returns the generated ID on success, -1 on failure.
     */
    public long createPlace(Place place) {
        if (place == null) return -1;

        ContentValues values = new ContentValues();
        values.put("name", place.getName());
        values.put("overview", place.getOverview());
        values.put("location", place.getLocation());
        values.put("price", place.getPrice());
        values.put("rating", place.getRating());
        values.put("popular", place.getPopular());
        values.put("images", place.getImage() != null ? String.join(",", place.getImage()) : null);
    return db.insert("Place", null, values); // Returns generated row ID
    }

    /**
     * Convert Cursor to Place object.
     */
    private Place convertCursorToPlace(Cursor cursor) {
        if (cursor == null) return null;

        String imagesString = cursor.getString(cursor.getColumnIndexOrThrow("images"));
        List<String> images = imagesString != null ? List.of(imagesString.split(",")) : new ArrayList<>();

        return new Place(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                images,
                cursor.getString(cursor.getColumnIndexOrThrow("location")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("overview")),
                cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                cursor.getFloat(cursor.getColumnIndexOrThrow("rating")),
                cursor.getInt(cursor.getColumnIndexOrThrow("popular")));
    }

    /**
     * Get all Places from the database.
     */
    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Place", null)) {
            while (cursor.moveToNext()) {
                Place place = convertCursorToPlace(cursor);
                if (place != null) {
                    places.add(place);
                }
            }
        }
        return places;
    }
    /**
     * Update an existing Place in the database.
     * Returns true if the update is successful, false otherwise.
     */
    public boolean updatePlace(Place place) {
        if (place == null || place.getId() <= 0) return false;

        ContentValues values = new ContentValues();
        values.put("name", place.getName());
        values.put("overview", place.getOverview());
        values.put("location", place.getLocation());
        values.put("price", place.getPrice());
        values.put("rating", place.getRating());
        values.put("popular", place.getPopular());
        values.put("images", place.getImage() != null ? String.join(",", place.getImage()) : null); // Convert List to CSV

        int rowsAffected = db.update("Place", values, "id = ?", new String[]{String.valueOf(place.getId())});
        return rowsAffected > 0;
    }

    /**
     * Get a single Place by its ID.
     */
    public Place getPlaceById(int id) {
        try (Cursor cursor = db.rawQuery("SELECT * FROM Place WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                return convertCursorToPlace(cursor);
            }
        }
        return null;
    }

    /**
     * Find Places near a given location within a radius.
     * Results are sorted by **distance** (ascending).
     */
    public List<Place> getPlacesByLocation(String location, int radius) {
        List<Place> places = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Place", null)) {
            while (cursor.moveToNext()) {
                String dbLocation = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                double distance = calculateDistance(location, dbLocation);

                if (distance <= radius) {
                    Place place = convertCursorToPlace(cursor);
                    if (place != null) {
                        place.setDistance(distance); // Add distance for sorting
                        places.add(place);
                    }
                }
            }
        }

        // Sort by distance (ascending)
        places.sort(Comparator.comparingDouble(Place::getDistance));

        return places;
    }

    /**
     * Get the most popular places, sorted by **popularity** in descending order.
     */
    public List<Place> getMostPopularPlaces(int limit) {
        List<Place> places = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Place ORDER BY popular DESC LIMIT ?", new String[]{String.valueOf(limit)})) {
            while (cursor.moveToNext()) {
                Place place = convertCursorToPlace(cursor);
                if (place != null) {
                    places.add(place);
                }
            }
        }
        return places;
    }

    /**
     * Get all places sorted by **rating** in descending order.
     */
    public List<Place> getPlacesByRatingDesc() {
        List<Place> places = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Place ORDER BY rating DESC", null)) {
            while (cursor.moveToNext()) {
                Place place = convertCursorToPlace(cursor);
                if (place != null) {
                    places.add(place);
                }
            }
        }
        return places;
    }

    /**
     * Delete a Place by its ID.
     */
    public boolean deletePlace(int id) {
        int rowsDeleted = db.delete("Place", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    /*
    * Get list place by category
    * */

    public List<Place> getPlacesByCategory(int categoryId){
        switch (categoryId){
            case ALL_PLACE:
                return getAllPlaces();
            case POPULAR_PLACE:
               return getMostPopularPlaces(10);
            case TOP_PLACE:
                return getPlacesByRatingDesc();
            default:
                return new ArrayList<>();
        }
    }

    /*
    * Get all place saved
    * */
    public  List<Place> getPlacesSaved(){
        List<Place> places = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Place WHERE saved = 1", null)) {
            while (cursor.moveToNext()) {
                Place place = convertCursorToPlace(cursor);
                if (place != null) {
                    places.add(place);
                }
            }
            return places;
        }
    }
}
