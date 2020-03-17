package com.lumibricks.model;
import java.util.Comparator;

import android.view.View;

public class BrickModel {
    private int mImageResource;
    private String brickName;
    private String amount;
    private View brickBackView;

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


    //Getter and setter methods same as the above examples

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<BrickModel> StuNameComparator = new Comparator<BrickModel>() {

        public int compare(BrickModel s1, BrickModel s2) {
            String BrickName1 = s1.getBrickName().toUpperCase();
            String BrickName2 = s2.getBrickName().toUpperCase();

            //ascending order
            return BrickName1.compareTo(BrickName2);

            //descending order
            //return BrickName2.compareTo(BrickName1);
        }};
}
