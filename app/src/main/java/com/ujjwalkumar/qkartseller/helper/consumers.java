package com.ujjwalkumar.qkartseller.helper;

public class consumers {
    private String address;
    private String contact;
    private String email;
    private String img;
    private String lat;
    private String lng;
    private String name;
    private String uid;
    
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
