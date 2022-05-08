package com.example.infinityweddings;

public class ReadWriteUserDetails {
 public String fullName, doB, mobile ;

    //constructor
    public  ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String fullName, String textDob, String textMobile ) {
        this.fullName = fullName;
        this.doB = textDob;
        this.mobile = textMobile;


    }
}
