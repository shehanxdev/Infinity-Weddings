package com.example.models;

import java.io.Serializable;

public class admin implements Serializable {
    public String email;
    public String password;
    public String fname;
    public  String lname;

    //constructor
    public admin(){

    }

    public admin(String username, String password){
        this.password=password;
        this.email=username;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public String getFname() {
        return this.fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPassword() {
        return password;
    }
}
