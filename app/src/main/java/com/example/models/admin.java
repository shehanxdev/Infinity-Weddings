package com.example.models;

public class admin {
    public String email;
    public String password;

    //constructor
    public admin(){

    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public admin(String username, String password){
        this.password=password;
        this.email=username;
    }
}
