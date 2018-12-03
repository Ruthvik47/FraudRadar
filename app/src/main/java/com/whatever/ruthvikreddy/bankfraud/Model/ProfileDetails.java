package com.whatever.ruthvikreddy.bankfraud.Model;

public class ProfileDetails {
    private String firstname;
    private String lastname;
    private String email;
    private String photourl;

    public ProfileDetails() {
    }

    public ProfileDetails(String firstname, String lastname, String email, String photourl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.photourl = photourl;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
