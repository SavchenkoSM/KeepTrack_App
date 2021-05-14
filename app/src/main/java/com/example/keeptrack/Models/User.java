package com.example.keeptrack.Models;

public class User {
    private String email, pass, name, phone, image, longitude, latitude, uid, cover;

    public  User() {

        this.email = email;
        this.name = name;
        this.pass = pass;
        this.phone = phone;

        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;

        this.cover = cover;
        this.uid = uid;
    }

    public  String getEmail(){
        return email;
    }
    public void  setEmail(String email){
        this.email = email;
    }

    public  String getName(){
        return name;
    }
    public void  setName(String name){
        this.name = name;
    }

    public  String getPass(){ return pass; }
    public void  setPass(String pass){
        this.pass = pass;
    }

    public  String getPhone(){
        return phone;
    }
    public void  setPhone(String phone){
        this.phone = phone;
    }

    public  String getImage(){ return image; }
    public void  setImage(String image){ this.image = image; }

    public  String getLongitude(){ return longitude; }
    public void  setLongitude(String longitude){ this.longitude = longitude; }
    public  String getLatitude(){ return latitude; }
    public void  setLatitude(String latitude){ this.latitude = latitude; }

    public  String getCover(){
        return cover;
    }
    public void  setCover(String cover){
        this.cover = cover;
    }

    public  String getUid(){ return uid; }
    public void  setUid(String uid){
        this.uid = uid;
    }
}
