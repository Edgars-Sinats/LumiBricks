package com.lumibricks.model;

public class Order {
    private int userID;
    private double palettes;
    private double orderPrice;
    private String timeStamp;
    private int addressID;

    public Order(){}

    public Order(int orderID, int userID, double palettes, double orderPrice, String timeStamp, int addressID) {
//        this.orderID = orderID;
        this.userID = userID;
        this.palettes = palettes;
        this.orderPrice = orderPrice;
        this.timeStamp = timeStamp;
        this.addressID = addressID;
    }

//    public int getOrderID() {
//        return orderID;
//    }
//
//    public void setOrderID(int orderID) {
//        this.orderID = orderID;
//    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getPalettes() {
        return palettes;
    }

    public void setPalettes(double palettes) {
        this.palettes = palettes;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }
}

