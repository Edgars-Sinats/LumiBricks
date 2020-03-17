package com.lumibricks.model;

import java.util.ArrayList;

public class Brick {
    private int id;             //Brick_ID
    private String name;
    private String sellType;
    private int gb_m2;
    private double m2_InPallet;
    private double gb_InPallet;
    private int sizeG;
    private int sizeA;
    private int sizeB;
    private double price;
    private double priceColor;

    public Brick(){}

    public Brick(String name, String sellType, int gb_m2, double m2_InPallet, double gb_InPallet, int sizeG, int sizeA, int sizeB, double price, double priceColor) {
        this.name = name;
        this.sellType = sellType;
        this.gb_m2 = gb_m2;
        this.m2_InPallet = m2_InPallet;
        this.gb_InPallet = gb_InPallet;
        this.sizeG = sizeG;
        this.sizeA = sizeA;
        this.sizeB = sizeB;
        this.price = price;
        this.priceColor = priceColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGb_m2() {
        return gb_m2;
    }

    public void setGb_m2(int gb_m2) {
        this.gb_m2 = gb_m2;
    }

    public double getM2_InPallet() {
        return m2_InPallet;
    }

    public void setM2_InPallet(double m2_InPallet) {
        this.m2_InPallet = m2_InPallet;
    }

    public double getGb_InPallet() {
        return gb_InPallet;
    }

    public void setGb_InPallet(double gb_InPallet) {
        this.gb_InPallet = gb_InPallet;
    }

    public int getSizeG() {
        return sizeG;
    }

    public void setSizeG(int sizeG) {
        this.sizeG = sizeG;
    }

    public int getSizeA() {
        return sizeA;
    }

    public void setSizeA(int sizeA) {
        this.sizeA = sizeA;
    }

    public int getSizeB() {
        return sizeB;
    }

    public void setSizeB(int sizeB) {
        this.sizeB = sizeB;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceColor() {
        return priceColor;
    }

    public void setPriceColor(double priceColor) {
        this.priceColor = priceColor;
    }
}
