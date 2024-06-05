package com.auction.springvproject.payload.request;

import javax.xml.crypto.Data;
import java.util.Date;

public class ProductRequest {

    private String name;
    private String description;
    private double price;
    private Date dateClosed;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description, double price, Date dateClosed) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.dateClosed=dateClosed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}