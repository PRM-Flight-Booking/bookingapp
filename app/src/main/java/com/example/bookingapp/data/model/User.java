package com.example.bookingapp.data.model;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.bookingapp.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class User implements Serializable {
    private int id;
    private String name;
    private String email;
    private String location;
    private String password;
    private boolean isAdmin;
    private int avatar;
    private String address; // not in database
    public User(){

    }
    /*register*/
    public User(String email, String location, String name, String password)
    {
        this.isAdmin = false;
        this.email = email;
        this.location = location;
        this.name = name;
        this.password = password;
        this.avatar = R.drawable.img_1;
    }
    /*database*/
    public User(String email, String password, int id, String location, String name, boolean isAdmin,int avatar) {
        this.email = email;
        this.id = id;
        this.location = location;
        this.name = name;
        this.isAdmin = isAdmin;
        this.password = password;
        this.avatar = avatar;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getAddress(Context context) {
        if (location == null || !location.contains(",")) {
            return "Invalid Location";
        }
        try {
            // Split location string into latitude and longitude
            String[] parts = location.split(",");
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());

            // Use Geocoder to fetch address
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Get city & country
                String city = address.getLocality();   // City name (e.g., New York)
                String country = address.getCountryCode();  // Country name (e.g., USA)

                // Return formatted location
                if (city != null && country != null) {
                    return city + ", " + country; // Example: "New York, USA"
                } else if (country != null) {
                    return country; // If city is missing, return only country
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return "Unknown Location";
    }



    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

}
