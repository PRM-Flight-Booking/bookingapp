package com.example.bookingapp.data.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Place implements Serializable {
    private int id;
    private String name;
    private String overview;
    private String location;
    private float price;
    private List<String> image;
    private double distance;
    private float rating;
    private int popular;
    private boolean isSaved;

    public Place() {
    }
    /*create*/
    public Place(String name, String overview, String location, float price, List<String> image, float rating,int popular) {
        this.name = name;
        this.overview = overview;
        this.location = location;
        this.price = price;
        this.image = image;
        this.rating = rating;
        this.popular = popular;
    }
    /*get from database*/
    public Place(int id, List<String> image, String location, String name, String overview, float price,float rating,int popular) {
        this.id = id;
        this.image = image;
        this.location = location;
        this.name = name;
        this.overview = overview;
        this.price = price;
        this.rating = rating;
        this.popular = popular;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public int getPopular() {
        return popular;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAddress(Context context) {
        if (location == null || !location.contains(",")) {
            return "Invalid Location";
        }
        try {
            // Tách tọa độ
            String[] parts = location.split(",");
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());

            // Sử dụng Geocoder để lấy địa chỉ
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Lấy thành phố & quốc gia
                String city = address.getLocality();
                String country = address.getCountryName();

                // Trả về địa chỉ đầy đủ
                if (city != null && country != null) {
                    return city + ", " + country; // Ví dụ: "Hà Nội, Việt Nam"
                } else if (country != null) {
                    return country; // Nếu thiếu thành phố, chỉ hiển thị quốc gia
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return "Unknown Location";
    }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", image=" + image +
                '}';
    }
}
