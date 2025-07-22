package com.example.bookingapp.data.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookingapp.R;
import com.example.bookingapp.data.database.DatabaseHelper;
import com.example.bookingapp.data.model.User;
import com.example.bookingapp.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final SQLiteDatabase db;

    public UserRepository(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Create a new User in the database.
     * Returns the generated ID on success, -1 on failure.
     */
    public long createUser(User user) {

        if (user == null) return -1;

        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());
        values.put("location", user.getLocation());
        values.put("isAdmin", user.isAdmin() ? 1 : 0); // Store boolean as 0/1
        values.put("avatar", user.getAvatar());

        return db.insert("User", null, values); // Returns generated row ID
    }

    /**
     * Convert Cursor to User object.
     */
    private User convertCursorToUser(Cursor cursor) {
        if (cursor == null) return null;

        return new User(
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")),
                cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Auto-incremented int ID
                cursor.getString(cursor.getColumnIndexOrThrow("location")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1 ,// Convert 0/1 to boolean,
                cursor.getInt(cursor.getColumnIndexOrThrow("avatar"))
        );
    }

    /**
     * Get User by Email and Password (Login Authentication).
     */
    public User getUserByAuth(String email, String password) {
        try (Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ? AND password = ?", new String[]{email, password})) {
            if (cursor.moveToFirst()) {
                return convertCursorToUser(cursor);
            }
        }
        return null;
    }
    /*
    * Get Password by Email
    * */
    @SuppressLint("Range")
    public String getPasswordByEmail(String email) {
        String password = null;
        try (Cursor cursor = db.rawQuery("SELECT password FROM User WHERE email = ?", new String[]{email})) {
            if (cursor.moveToFirst()) {
                password = cursor.getString(cursor.getColumnIndex("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }


    /**
     * Get all Users from the database.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM User", null)) {
            while (cursor.moveToNext()) {
                User user = convertCursorToUser(cursor);
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    /**
     * Get a User by ID.
     */
    public User getUserById(int id) {
        try (Cursor cursor = db.rawQuery("SELECT * FROM User WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                return convertCursorToUser(cursor);
            }
        }
        return null;
    }

    /**
     * Update User details.
     */
    public boolean updateUser(User user) {
        if (user == null) return false;

        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());
        values.put("location", user.getLocation());
        values.put("isAdmin", user.isAdmin() ? 1 : 0);
        values.put("avatar", user.getAvatar());

        int rows = db.update("User", values, "id = ?", new String[]{String.valueOf(user.getId())});
        return rows > 0;
    }

    /**
     * Delete a User by ID.
     */
    public boolean deleteUser(int id) {
        int rowsDeleted = db.delete("User", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }
}
