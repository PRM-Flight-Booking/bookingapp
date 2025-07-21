package com.example.bookingapp.data.repository;

import static com.example.bookingapp.utils.Constant.DATE_TIME_FORMAT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookingapp.data.database.DatabaseHelper;
import com.example.bookingapp.data.model.FlightBill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FlightBillRepository {
    private final SQLiteDatabase db;

    public FlightBillRepository(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Create a new FlightBill in the database.
     * Returns the generated ID on success, -1 on failure.
     */
    public long createFlightBill(FlightBill flightBill) {
        if (flightBill == null) return -1;

        ContentValues values = new ContentValues();
        values.put("idFlight", flightBill.getIdFlight());
        values.put("idUser", flightBill.getIdUser());
        values.put("price", flightBill.getPrice());
        values.put("ticketNumber", flightBill.getTicketNumber());

        long newId = db.insert("FlightBill", null, values);

        if (newId != -1) {
            flightBill.setId((int) newId);
        }

        return newId;
    }


    /**
     * Convert Cursor to FlightBill object.
     */
    private FlightBill convertCursorToFlightBill(Cursor cursor) {
        if (cursor == null) return null;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());

        return new FlightBill(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Auto-incremented int ID
                cursor.getInt(cursor.getColumnIndexOrThrow("idFlight")),
                cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                cursor.getInt(cursor.getColumnIndexOrThrow("ticketNumber")),
                cursor.getInt(cursor.getColumnIndexOrThrow("idUser"))
        );
    }

    /**
     * Get all FlightBills from the database.
     */
    public List<FlightBill> getAllFlightBills() {
        List<FlightBill> flightBills = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM FlightBill", null)) {
            while (cursor.moveToNext()) {
                FlightBill flightBill = convertCursorToFlightBill(cursor);
                if (flightBill != null) {
                    flightBills.add(flightBill);
                }
            }
        }
        return flightBills;
    }

    /**
     * Get a single FlightBill by its ID.
     */
    public FlightBill getFlightBillById(int id) {
        try (Cursor cursor = db.rawQuery("SELECT * FROM FlightBill WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                return convertCursorToFlightBill(cursor);
            }
        }
        return null;
    }

    /**
     * Delete a FlightBill by its ID.
     */
    public boolean deleteFlightBill(int id) {
        int rowsDeleted = db.delete("FlightBill", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }


    public List<FlightBill> getFlightBillsByUserId(int userId) {
        List<FlightBill> bills = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM FlightBill WHERE idUser = ?", new String[]{String.valueOf(userId)})) {
            while (cursor.moveToNext()) {
                FlightBill bill = convertCursorToFlightBill(cursor);
                if (bill != null) bills.add(bill);
            }
        }
        return bills;
    }


}
