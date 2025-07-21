package com.example.bookingapp.data.model;

import java.io.Serializable;

public class Bill implements Serializable {
    private int id;
    private int flightBillId;
    private int hotelBillId;
    private int placeBillId;
    private int userId;
    private float totalPrice;
    private int status;

    public Bill(){}
    // create new
    public Bill(int flightBillId, int hotelBillId, int placeBillId, float totalPrice, int userId) {
        this.flightBillId = flightBillId;
        this.hotelBillId = hotelBillId;
        this.placeBillId = placeBillId;
        this.totalPrice = totalPrice;
        this.userId = userId;
        status =0;
    }
    // database
    public Bill(int flightBillId, int hotelBillId, int id, int placeBillId, float totalPrice, int userId,int status) {
        this.flightBillId = flightBillId;
        this.hotelBillId = hotelBillId;
        this.id = id;
        this.placeBillId = placeBillId;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.status =status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFlightBillId() {
        return flightBillId;
    }

    public void setFlightBillId(int flightBillId) {
        this.flightBillId = flightBillId;
    }

    public int getHotelBillId() {
        return hotelBillId;
    }

    public void setHotelBillId(int hotelBillId) {
        this.hotelBillId = hotelBillId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaceBillId() {
        return placeBillId;
    }

    public void setPlaceBillId(int placeBillId) {
        this.placeBillId = placeBillId;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Bill{" +
                ", id='" + id + '\'' +
                ", flightBillId='" + flightBillId + '\'' +
                ", hotelBillId='" + hotelBillId + '\'' +
                ", placeBillId='" + placeBillId + '\'' +
                ", userId='" + userId + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
