package com.whatever.ruthvikreddy.bankfraud.Model;

public class PhoneDetails {

    private String fraudname;
    private String walletname;
    private String phonenumber;
    private String transactionid;
    private String description;
    private String anonymous;

    public PhoneDetails() {
    }

    public PhoneDetails(String fraudname, String walletname, String phonenumber, String transactionid, String description, String anonymous) {

        this.fraudname = fraudname;
        this.walletname = walletname;
        this.phonenumber = phonenumber;
        this.transactionid = transactionid;
        this.description = description;
        this.anonymous = anonymous;
    }

    public String getFraudname() {
        return fraudname;
    }

    public void setFraudname(String fraudname) {
        this.fraudname = fraudname;
    }

    public String getWalletname() {
        return walletname;
    }

    public void setWalletname(String walletname) {
        this.walletname = walletname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }
}
