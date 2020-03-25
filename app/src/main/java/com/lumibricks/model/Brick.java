package com.lumibricks.model;

import java.util.ArrayList;
import java.util.List;

public class Brick {
    private int id;             //Brick_ID
    private String name;
    private String sellType;
    private int gb_m2;
    private double m2_InPallet;
    private double gb_InPallet;
    private int gbRinda;
    private int sizeG;
    private int sizeA;
    private int sizeB;
    private double price;
    private double priceLumi;
    private double priceGray;
    private double priceGreen;
    private double priceYellow;
    private double priceOrange;
    private double priceRed;
    private double priceBlack;
    private double priceBrown;
    private double priceWhite;
//    TODO crete price for every color


    public Brick(){}

    public Brick(String name, String sellType, int gb_m2, double m2_InPallet, double gb_InPallet, int gbRinda, int sizeG, int sizeA, int sizeB, double price, double priceLumi, double priceGray, double priceGreen, double priceYellow, double priceOrange, double priceRed, double priceBlack, double priceBrown, double priceWhite) {
        this.name = name;
        this.sellType = sellType;
        this.gb_m2 = gb_m2;
        this.m2_InPallet = m2_InPallet;
        this.gb_InPallet = gb_InPallet;
        this.gbRinda = gbRinda;
        this.sizeG = sizeG;
        this.sizeA = sizeA;
        this.sizeB = sizeB;
        this.price = price;
        this.priceLumi = priceLumi;
        this.priceGray = priceGray;
        this.priceGreen = priceGreen;
        this.priceYellow = priceYellow;
        this.priceOrange = priceOrange;
        this.priceRed = priceRed;
        this.priceBlack = priceBlack;
        this.priceBrown = priceBrown;
        this.priceWhite = priceWhite;
    }

    //    public Brick(String name, String sellType, int gb_m2, double m2_InPallet, double gb_InPallet, int sizeG, int sizeA, int sizeB, double price, double priceLumi, double priceGray, double priceGreen, double priceYellow, double priceOrange, double priceRed, double priceBlack, double priceBrown, double priceWhite) {
//        this.name = name;
//        this.sellType = sellType;
//        this.gb_m2 = gb_m2;
//        this.m2_InPallet = m2_InPallet;
//        this.gb_InPallet = gb_InPallet;
//        this.sizeG = sizeG;
//        this.sizeA = sizeA;
//        this.sizeB = sizeB;
//        this.price = price;
//        this.priceLumi = priceLumi;
//        this.priceGray = priceGray;
//        this.priceGreen = priceGreen;
//        this.priceYellow = priceYellow;
//        this.priceOrange = priceOrange;
//        this.priceRed = priceRed;
//        this.priceBlack = priceBlack;
//        this.priceBrown = priceBrown;
//        this.priceWhite = priceWhite;
//    }

//    public Brick(String name, String sellType, int gb_m2, double m2_InPallet, double gb_InPallet, int sizeG, int sizeA, int sizeB, double price, double priceColor) {
//        this.name = name;
//        this.sellType = sellType;
//        this.gb_m2 = gb_m2;
//        this.m2_InPallet = m2_InPallet;
//        this.gb_InPallet = gb_InPallet;
//        this.sizeG = sizeG;
//        this.sizeA = sizeA;
//        this.sizeB = sizeB;
//        this.price = price;
//    }

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

    public int getGbRinda() {
        return gbRinda;
    }

    public void setGbRinda(int gbRinda) {
        this.gbRinda = gbRinda;
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

    public double getPriceLumi() {
        return priceLumi;
    }

    public void setPriceLumi(double priceLumi) {
        this.priceLumi = priceLumi;
    }

    public double getPriceGray() {
        return priceGray;
    }

    public void setPriceGray(double priceGray) {
        this.priceGray = priceGray;
    }

    public double getPriceGreen() {
        return priceGreen;
    }

    public void setPriceGreen(double priceGreen) {
        this.priceGreen = priceGreen;
    }

    public double getPriceYellow() {
        return priceYellow;
    }

    public void setPriceYellow(double priceYellow) {
        this.priceYellow = priceYellow;
    }

    public double getPriceOrange() {
        return priceOrange;
    }

    public void setPriceOrange(double priceOrange) {
        this.priceOrange = priceOrange;
    }

    public double getPriceRed() {
        return priceRed;
    }

    public void setPriceRed(double priceRed) {
        this.priceRed = priceRed;
    }

    public double getPriceBlack() {
        return priceBlack;
    }

    public void setPriceBlack(double priceBlack) {
        this.priceBlack = priceBlack;
    }

    public double getPriceBrown() {
        return priceBrown;
    }

    public void setPriceBrown(double priceBrown) {
        this.priceBrown = priceBrown;
    }

    public double getPriceWhite() {
        return priceWhite;
    }

    public void setPriceWhite(double priceWhite) {
        this.priceWhite = priceWhite;
    }
}

