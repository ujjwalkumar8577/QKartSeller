package com.ujjwalkumar.qkartseller.helper;
// this class keeps details of a customer
public class consumers {
    private String address;     // to store address
    private String contact;     // to store contact number
    private String email;       // to store email address
    private String img;         // to store image download link
    private String lat;         // to store latitude
    private String lng;         // to store longitude
    private String name;        // to store name of shop
    private String uid;         // to store unique id of seller

    // constructor of the consumers class
    public consumers(String address, String contact, String email, String img, String lat, String lng, String name, String uid){
        this.address=address;
        this.contact=contact;
        this.email=email;
        this.img=img;
        this.lat=lat;
        this.lng=lng;
        this.name=name;
        this.uid=uid;
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

    // setter function for email
    public void setEmail(String email) {
        this.email = email;
    }

    // getter function for image
    public String getImg() {
        return img;
    }

    // setter function for image
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

    // getter function for langitude
    public String getLng() {
        return lng;
    }

    // setter function for lagitude
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

    // getter function for user id
    public String getUid() {
        return uid;
    }

    // setter function for user id
    public void setUid(String uid) {
        this.uid = uid;
    }
}
