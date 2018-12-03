package com.whatever.ruthvikreddy.bankfraud.Model;

import com.google.firebase.Timestamp;

public class Bankcomments {
    private String comment;
    private String postedby;
    private Timestamp timestamp;

    public Bankcomments() {
    }

    public Bankcomments(String comment, String postedby, Timestamp timestamp) {
        this.comment = comment;
        this.postedby = postedby;
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
