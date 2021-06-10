package com.example.project.greetingswisher;

public class SMSDB {

    String mobileNo;
    String Message;
    String date;

    public SMSDB(String mobileNo, String message, String date) {
        this.mobileNo = mobileNo;
        this.Message = message;
        this.date = date;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
