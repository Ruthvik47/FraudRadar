package com.whatever.ruthvikreddy.bankfraud.Model;

public class Bankdetails {
    private String fraudname;
    private String bankname;
    private String accountnumber;
    private String ifscnumber;

    public Bankdetails() {
    }

    public Bankdetails(String fraudname, String bankname, String accountnumber, String ifscnumber) {
        this.fraudname = fraudname;
        this.bankname = bankname;
        this.accountnumber = accountnumber;
        this.ifscnumber = ifscnumber;

    }

    public String getFraudname() {
        return fraudname;
    }

    public void setFraudname(String fraudname) {
        this.fraudname = fraudname;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getIfscnumber() {
        return ifscnumber;
    }

    public void setIfscnumber(String ifscnumber) {
        this.ifscnumber = ifscnumber;
    }


}
