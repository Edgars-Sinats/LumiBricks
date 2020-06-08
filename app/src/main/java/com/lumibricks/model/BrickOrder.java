package com.lumibricks.model;
//Change sellPrice depending on our order by your choose.

public class BrickOrder {
    private String name;
    private String color;
    private String lumi;
    private String sellType;
    private Double sellPrice;
    private String inputSellType;
    private Double inputSellPrice;
    private Double amount;
    private Double orginAmount;
    private Double palletes;

    public BrickOrder() { }

//    public BrickOrder(String name, String sellType, Double sellPrice, String inputSellType, Double inputSellPrice, Double amount, Double orginAmount, Double palletes) {
//        this.name = name;
//        this.sellType = sellType;
//        this.sellPrice = sellPrice;
//        this.inputSellType = inputSellType;
//        this.inputSellPrice = inputSellPrice;
//        this.amount = amount;
//        this.orginAmount = orginAmount;
//        this.palletes = palletes;
//    }

    public BrickOrder(String name, String color, String lumi, String sellType, Double sellPrice, String inputSellType, Double inputSellPrice, Double amount, Double orginAmount, Double palletes) {
        this.name = name;
        this.color = color;
        this.lumi = lumi;
        this.sellType = sellType;
        this.sellPrice = sellPrice;
        this.inputSellType = inputSellType;
        this.inputSellPrice = inputSellPrice;
        this.amount = amount;
        this.orginAmount = orginAmount;
        this.palletes = palletes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getInputSellType() {
        return inputSellType;
    }

    public void setInputSellType(String inputSellType) {
        this.inputSellType = inputSellType;
    }

    public Double getInputSellPrice() {
        return inputSellPrice;
    }

    public void setInputSellPrice(Double inputSellPrice) {
        this.inputSellPrice = inputSellPrice;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getOrginAmount() {
        return orginAmount;
    }

    public void setOrginAmount(Double orginAmount) {
        this.orginAmount = orginAmount;
    }

    public Double getPalletes() {
        return palletes;
    }

    public void setPalletes(Double palletes) {
        this.palletes = palletes;
    }
}
