package com.example.bookingapp.data.model;

import java.util.Date;

public class HotelBill {
    private int id;
    private Date date;
    private float price;
    private int time;
    private int idHotel;
    private int ticketNumber;


    private int idUser;
    public HotelBill(){}
    public HotelBill( Date date, float price, int time, int idHotel, int idUser, int ticketNumber) {
        this.date = date;
        this.price = price;
        this.time = time;
        this.idHotel = idHotel;
        this.idUser = idUser;
        this.ticketNumber = ticketNumber;
    }
    public HotelBill(int id, Date date, float price, int time, int idHotel,int idUser, int ticketNumber) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.time = time;
        this.idHotel = idHotel;
        this.idUser = idUser;
        this.ticketNumber = ticketNumber;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
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


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public Integer getIdHotel() {
        return idHotel;
    }
    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    @Override
    public String toString() {
        return "HotelBill{" +
                "date=" + date +
                ", id='" + id + '\'' +
                ", price=" + price +
                ", time=" + time +
                '}';
    }
}
