package com.example.bookingapp.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookingapp.data.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SavedRepository {
    private final SQLiteDatabase db;

    public SavedRepository(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Saves a place to the user's favorites list.
     * Prevents duplicate entries.
     * @param userId ID of the user
     * @param placeId ID of the place
     * @return true if the place was successfully added, false otherwise
     */
    public boolean savePlace(int userId, int placeId) {
        ContentValues values = new ContentValues();
        values.put("idUser", userId);
        values.put("idPlace", placeId);

        // Avoids duplicate saves
        long result = db.insertWithOnConflict("Saved", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1;
    }

    /**
     * Removes a saved place from the user's favorites list.
     * @param userId ID of the user
     * @param placeId ID of the place
     * @return true if the place was successfully removed, false otherwise
     */
    public boolean removeSavedPlace(int userId, int placeId) {
        int result = db.delete("Saved", "idUser = ? AND idPlace = ?",
                new String[]{String.valueOf(userId), String.valueOf(placeId)});
        return result > 0;
    }

    /**
     * Retrieves a list of place IDs that the user has saved.
     * @param userId ID of the user
     * @return List of saved place IDs
     */
    public List<Integer> getSavedPlacesByUser(int userId) {
        List<Integer> savedPlaces = new ArrayList<>();
        Cursor cursor = db.query("Saved", new String[]{"idPlace"}, "idUser = ?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                savedPlaces.add(cursor.getInt(0));
            }
            cursor.close();
        }
        return savedPlaces;
    }

    /**
     * Checks if a place is already saved by the user.
     * @param userId ID of the user
     * @param placeId ID of the place
     * @return true if the place is already saved, false otherwise
     */
    public boolean isPlaceSaved(int userId, int placeId) {
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM Saved WHERE idUser = ? AND idPlace = ? LIMIT 1",
                new String[]{String.valueOf(userId), String.valueOf(placeId)}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    /**
     * Retrieves a list of saved place IDs for a specific user.
     * @param userId ID of the user
     * @return List of saved place IDs
     */
    public List<Integer> getSavedPlaceIdsByUser(int userId) {
        List<Integer> savedPlaceIds = new ArrayList<>();
        Cursor cursor = db.query("Saved", new String[]{"idPlace"}, "idUser = ?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                savedPlaceIds.add(cursor.getInt(0));
            }
            cursor.close();
        }
        return savedPlaceIds;
    }
}
