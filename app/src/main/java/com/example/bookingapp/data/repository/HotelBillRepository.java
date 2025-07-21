package com.example.bookingapp.data.repository;

import static com.example.bookingapp.utils.Constant.DATE_TIME_FORMAT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookingapp.data.database.DatabaseHelper;
import com.example.bookingapp.data.model.HotelBill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HotelBillRepository {
    private final SQLiteDatabase db;

    public HotelBillRepository(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Create a new HotelBill in the database.
     * Returns the generated ID on success, -1 on failure.
     */
    public long createHotelBill(HotelBill hotelBill) {
        if (hotelBill == null) return -1;

        ContentValues values = new ContentValues();
        values.put("idUser", hotelBill.getIdUser());
        values.put("idHotel", hotelBill.getIdHotel());
        values.put("date", new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(hotelBill.getDate()));
        values.put("price", hotelBill.getPrice());
        values.put("time", hotelBill.getTime());
        values.put("ticketNumber", hotelBill.getTicketNumber());

        long newId = db.insert("HotelBill", null, values);

        if (newId != -1) {
            hotelBill.setId((int) newId);
        }

        return newId;
    }


    /**
     * Convert Cursor to HotelBill object.
     */
    private HotelBill convertCursorToHotelBill(Cursor cursor) {
        if (cursor == null) return null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());

            return new HotelBill(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Auto-incremented int ID
                    sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("date"))),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("time")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("idHotel")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("idUser")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("ticketNumber"))
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all HotelBills from the database.
     */
    public List<HotelBill> getAllHotelBills() {
        List<HotelBill> hotelBills = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM HotelBill", null)) {
            while (cursor.moveToNext()) {
                HotelBill hotelBill = convertCursorToHotelBill(cursor);
                if (hotelBill != null) {
                    hotelBills.add(hotelBill);
                }
            }
        }
        return hotelBills;
    }

    /**
     * Get a HotelBill by ID.
     */
    public HotelBill getHotelBillById(int id) {
        try (Cursor cursor = db.rawQuery("SELECT * FROM HotelBill WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                return convertCursorToHotelBill(cursor);
            }
        }
        return null;
    }

    /**
     * Delete a HotelBill by ID.
     */
    public boolean deleteHotelBill(int id) {
        int rowsDeleted = db.delete("HotelBill", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }
}
