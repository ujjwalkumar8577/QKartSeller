package com.ujjwalkumar.qkartseller.helper;
// this class keeps details of a item
public class items {
    private String detail;
    private String id;
    private String mrp;
    private String name;
    private String price;
    private String sellerid;
    private String status;

    // constructor of the items class
    public items(String detail, String id, String mrp, String name, String price, String sellerid, String status){
        this.detail=detail;
        this.id=id;
        this.mrp=mrp;
        this.name=name;
        this.price=price;
        this.sellerid=sellerid;
        this.status=status;
    }

    // getter function for detail
    public String getDetail() {
        return detail;
    }

    // setter function for detail
    public void setDetail(String detail) {
        this.detail = detail;
    }

    // getter function for id
    public String getId() {
        return id;
    }

    // setter function for id
    public void setId(String id) {
        this.id = id;
    }

    // getter function for mrp
    public String getMrp() {
        return mrp;
    }

    // setter function for mrp
    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    // getter function for name
    public String getName() {
        return name;
    }

    // setter function for name
    public void setName(String name) {
        this.name = name;
    }

    // getter function for price
    public String getPrice() {
        return price;
    }

    // setter function for price
    public void setPrice(String price) {
        this.price = price;
    }

    // getter function for sellerid
    public String getSellerid() {
        return sellerid;
    }

    // setter function for sellerid
    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    // getter function for status
    public String getStatus() {
        return status;
    }

    // setter function for status
    public void setStatus(String status) {
        this.status = status;
    }
}
