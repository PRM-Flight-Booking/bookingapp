package com.example.bookingapp.data.model;

import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private String location;
    private String overview;
    private List<String> images;
    private float price;
    private float rate;
    private double distance;

    public Hotel() {
    }
    public Hotel( List<String> images, String location, String name, float price, float rate,String overview) {
        this.images = images;
        this.location = location;
        this.name = name;
        this.price = price;
        this.rate = rate;
        this.overview = overview;
    }
    public Hotel(int id, List<String> images, String location, String name, float price, float rate,String overview) {
        this.id = id;
        this.images = images;
        this.location = location;
        this.name = name;
        this.price = price;
        this.rate = rate;
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", images=" + images +
                ", price=" + price +
                ", rate=" + rate +
                ", ov ="+overview+
                '}';
    }
}
