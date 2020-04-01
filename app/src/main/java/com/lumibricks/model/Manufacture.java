package com.lumibricks.model;

import android.widget.EditText;
import android.widget.Spinner;

import com.lumibricks.R;

public class Manufacture {

    private String userID;
    private String userName;

    private String brick;
    private String height;
    private String color;
    private String lumi;
    private String quality;
    private Double amount;
    private Double price;
    private String sellType;
    private Double palletes;

    public Manufacture() {    }

    public Manufacture(String brick, String height, String color, String lumi, String quality, Double amount, Double price, String sellType, Double palletes) {
        this.brick = brick;
        this.height = height;
        this.color = color;
        this.lumi = lumi;
        this.quality = quality;
        this.amount = amount;
        this.price = price;
        this.sellType = sellType;
        this.palletes = palletes;
    }

    public static Manufacture getDefault(){
        Manufacture manufacture = new Manufacture();
        return manufacture;
    }

    public String getBrick() {
        return brick;
    }

    public void setBrick(String brick) {
        this.brick = brick;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLumi() {
        return lumi;
    }

    public void setLumi(String lumi) {
        this.lumi = lumi;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public Double getPalletes() {
        return palletes;
    }

    public void setPalletes(Double palletes) {
        this.palletes = palletes;
    }
}
