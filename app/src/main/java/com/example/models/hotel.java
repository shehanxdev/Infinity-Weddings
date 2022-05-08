package com.example.models;

import java.io.Serializable;

public class hotel implements Serializable {
    public String hotelName;
    public String address;
    public int ratings;
    public String imageUrl;
    public String about;

    public  hotel(){

    }
    public hotel(String hotelName, String address, int ratings, String imageUrl,String about) {
        this.hotelName = hotelName;
        this.address = address;
        this.ratings = ratings;
        this.imageUrl = imageUrl;
        this.about=about;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbout(){
        return this.about;
    }
    public void setAbout(String about){
        this.about=about;
    }
}
