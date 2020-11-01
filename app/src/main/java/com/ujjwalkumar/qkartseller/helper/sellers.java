package com.ujjwalkumar.qkartseller.helper;
// this class keeps details of a seller
public class sellers {
    private String address;     // to store address
    private String contact;     // to store contact number
    private String email;       // to store email address
    private String img;         // to store image download link
    private String lat;         // to store latitude
    private String lng;         // to store longitude
    private String name;        // to store name of shop
    private String uid;         // to store unique id of seller
    private String range;       // to store delivery range of selller

    // constructor of the sellers class
    public sellers(String address, String contact, String email, String img, String lat, String lng, String name, String uid, String range){
        this.address=address;
        this.contact=contact;
        this.email=email;
        this.img=img;
        this.lat=lat;
        this.lng=lng;
        this.name=name;
        this.uid=uid;
        this.range=range;
    }

    // getter function for address
    public String getAddress() {
        return address;
    }

    // setter function for address
    public void setAddress(String address) {
        this.address = address;
    }

    // getter function for contact
    public String getContact() {
        return contact;
    }

    // setter function for contact
    public void setContact(String contact) {
        this.contact = contact;
    }

    // getter function for email
    public String getEmail() {
        return email;
    }

    // setter function for contact
    public void setEmail(String email) {
        this.email = email;
    }

    // getter function for image link
    public String getImg() {
        return img;
    }

    // setter function for image link
    public void setImg(String img) {
        this.img = img;
    }

    // getter function for latitude
    public String getLat() {
        return lat;
    }

    // setter function for latitude
    public void setLat(String lat) {
        this.lat = lat;
    }

    // getter function for longitude
    public String getLng() {
        return lng;
    }

    // setter function for longitude
    public void setLng(String lng) {
        this.lng = lng;
    }

    // getter function for name
    public String getName() {
        return name;
    }

    // setter function for name
    public void setName(String name) {
        this.name = name;
    }

    // getter function for uid
    public String getUid() {
        return uid;
    }

    // setter function for uid
    public void setUid(String uid) {
        this.uid = uid;
    }

    // getter function for range
    public String getRange() {
        return range;
    }

    // setter function for range
    public void setRange(String range) {
        this.range = range;
    }
}