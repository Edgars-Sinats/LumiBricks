package com.lumibricks.model;

public class ItemInOrder {
    private int itemInOrderID;
    private int orderID;
    private String itemName;
    private double itemAmount;
    private double itemPrice;
    private double itemPalettes;
    private int inStock;

    public ItemInOrder(){}

    public ItemInOrder(int itemInOrderID, int orderID, String itemName, double itemAmount, double itemPrice, double itemPalettes, int inStock) {
        this.itemInOrderID = itemInOrderID;
        this.orderID = orderID;
        this.itemName = itemName;
        this.itemAmount = itemAmount;
        this.itemPrice = itemPrice;
        this.itemPalettes = itemPalettes;
        this.inStock = inStock;
    }

    public int getItemInOrderID() {
        return itemInOrderID;
    }

    public void setItemInOrderID(int itemInOrderID) {
        this.itemInOrderID = itemInOrderID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(double itemAmount) {
        this.itemAmount = itemAmount;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getItemPalettes() {
        return itemPalettes;
    }

    public void setItemPalettes(double itemPalettes) {
        this.itemPalettes = itemPalettes;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
}


