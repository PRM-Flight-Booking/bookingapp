package com.example.bookingapp.data.model;

import java.io.Serializable;
import java.util.Date;

public class Flight implements Serializable {
    private int id;
    private String name;
    private float price;
    private String departureLocation;
    private String arrivalLocation;
    private Date departureTime;
    private Date arrivalTime;

    public Flight() {
    }
    public Flight(Date arrivalTime, String departureLocation, Date departureTime, String arrivalLocation,String name, float price) {
        this.arrivalTime = arrivalTime;
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.name = name;
        this.price = price;
    }


    public Flight(Date arrivalTime, String departureLocation, Date departureTime, String arrivalLocation, int id, String name, float price) {
        this.arrivalTime = arrivalTime;
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.arrivalLocation = destinationLocation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Flight{" +
                "arrivalTime='" + arrivalTime + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", departureLocation='" + departureLocation + '\'' +
                ", arrivalLocation='" + arrivalLocation + '\'' +
                ", departureTime='" + departureTime + '\'' +
                '}';
    }
}

