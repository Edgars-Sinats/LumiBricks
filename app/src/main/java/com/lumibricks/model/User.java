package com.lumibricks.model;

public class User {
    private int userID;
    private String userName;
    private String name;
    private String surname;
    private String eMail;
    private String mobile;
    private String bankNr;

    public User(){}

    public User(int userID, String userName, String name, String surname, String eMail, String bankNr, String mobile) {
        this.userID = userID;
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.eMail = eMail;
        this.bankNr = bankNr;
        this.mobile = mobile;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getBankNr() {
        return bankNr;
    }

    public void setBankNr(String bankNr) {
        this.bankNr = bankNr;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

