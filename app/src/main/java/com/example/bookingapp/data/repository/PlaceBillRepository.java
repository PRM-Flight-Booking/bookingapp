package com.example.bookingapp.data.repository;

import static com.example.bookingapp.utils.Constant.DATE_TIME_FORMAT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookingapp.data.database.DatabaseHelper;
import com.example.bookingapp.data.model.PlaceBill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlaceBillRepository {
    private final SQLiteDatabase db;
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
    public PlaceBillRepository(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Create a new PlaceBill in the database.
     * Returns the generated ID on success, -1 on failure.
     */
    public long createPlaceBill(PlaceBill placeBill) {
        if (placeBill == null) return -1;

        ContentValues values = new ContentValues();
        values.put("idUser", placeBill.getIdUser());
        values.put("price", placeBill.getPrice());
        values.put("idPlace", placeBill.getIdPlace());
        values.put("ticketNumber", placeBill.getTicketNumber());
        values.put("date", sdf.format(placeBill.getDate()));

        long newId = db.insert("PlaceBill", null, values);

        if (newId != -1) {
            placeBill.setId((int) newId);
        }

        return newId;
    }


    /**
     * Convert Cursor to PlaceBill object.
     */
    private PlaceBill convertCursorToPlaceBill(Cursor cursor) throws ParseException {
        if (cursor == null) return null;

        return new PlaceBill(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Auto-incremented int ID
                cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                cursor.getInt(cursor.getColumnIndexOrThrow("ticketNumber")),
                cursor.getInt(cursor.getColumnIndexOrThrow("idPlace")),
                cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("date")))
        );
    }

    /**
     * Get all PlaceBills from the database.
     */
    public List<PlaceBill> getAllPlaceBills() throws ParseException {
        List<PlaceBill> placeBills = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM PlaceBill", null)) {
            while (cursor.moveToNext()) {
                PlaceBill placeBill = convertCursorToPlaceBill(cursor);
                if (placeBill != null) {
                    placeBills.add(placeBill);
                }
            }
        }
        return placeBills;
    }

    /**
     * Get a PlaceBill by ID.
     */
    public PlaceBill getPlaceBillById(int id) {
        try (Cursor cursor = db.rawQuery("SELECT * FROM PlaceBill WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                return convertCursorToPlaceBill(cursor);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Delete a PlaceBill by ID.
     */
    public boolean deletePlaceBill(int id) {
        int rowsDeleted = db.delete("PlaceBill", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // Phương thức mới: Lấy danh sách id của PlaceBill theo idUser
    public List<Integer> getPlaceBillIdsByUserId(int userId) {
        List<Integer> placeBillIds = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT id FROM PlaceBill WHERE idUser = ?", new String[]{String.valueOf(userId)})) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                placeBillIds.add(id);
            }
        }
        return placeBillIds;
    }


}
