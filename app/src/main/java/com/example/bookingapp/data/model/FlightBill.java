package com.example.bookingapp.data.model;

public class FlightBill {
    private int id;
    private float price;
    private int idFlight;
    private int ticketNumber;
    private int idUser;


    public FlightBill() {
    }

    public FlightBill(int idFlight, float price, int ticketNumber,int idUser) {
        this.idFlight = idFlight;
        this.price = price;
        this.ticketNumber = ticketNumber;
        this.idUser = idUser;
    }
    public FlightBill(int id, int idFlight, float price, int ticketNumber,int idUser) {

        this.id = id;
        this.idFlight = idFlight;
        this.price = price;
        this.ticketNumber = ticketNumber;
        this.idUser = idUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
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
}
