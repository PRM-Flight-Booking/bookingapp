package com.example.bookingapp.data.model;

import java.util.Date;

public class PlaceBill {
    private int id;
    private float price;
    private int ticketNumber;
    private int idPlace;
    private int idUser;
    private Date date;

    public PlaceBill(float price, int ticketNumber,int idPlace,int idUser,Date date) {
        this.price = price;
        this.ticketNumber = ticketNumber;
        this.idPlace = idPlace;
        this.idUser = idUser;
        this.date =date;
    }
    public PlaceBill(int id, float price, int ticketNumber,int idPlace,int idUser,Date date) {
        this.id = id;
        this.price = price;
        this.ticketNumber = ticketNumber;
        this.idPlace = idPlace;
        this.idUser = idUser;
        this.date =date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public PlaceBill() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Integer getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(int idPlace) {
        this.idPlace = idPlace;
    }

    @Override
    public String toString() {
        return "PlaceBill{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", ticketNumber=" + ticketNumber +
                '}';
    }
}
