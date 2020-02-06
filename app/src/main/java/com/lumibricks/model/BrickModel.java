package com.lumibricks.model;

public class BrickModel {
    private int mImageResource;
    private String brickName;
    private String amount;

    public BrickModel(){}

    public BrickModel(int imageResource, String brickName, String amount) {
        mImageResource = imageResource;
        this.brickName = brickName;
        this.amount = amount;
    }

    public void changeImage(int imageResource){
        mImageResource = imageResource;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public void setImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getBrickName() {
        return brickName;
    }

    public void setBrickName(String brickName) {
        this.brickName = brickName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
