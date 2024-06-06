package com.auction.springvproject.dtopayload.request;

public class BuyRequest {
    private Long id;
    private double price;

    private BuyRequest() {
    }

    public BuyRequest(Long id, double price) {
        this.id = id;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
