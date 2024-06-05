package com.auction.springvproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.Date;

@Entity
public class CompletedAuction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createdUser;
    private String buyerUser;
    private String name;
    private String description;
    private double price;
    private Date dateClosed;

    public CompletedAuction() {
    }

    public CompletedAuction(String createdUser, String name, String description, double price, Date dateClosed) {
        this.createdUser = createdUser;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dateClosed=dateClosed;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getName() {
        return name;
    }

    public String getBuyerUser() {
        return buyerUser;
    }

    public void setBuyerUser(String buyerUser) {
        this.buyerUser = buyerUser;
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Date dateCreated) {
        this.dateClosed = dateCreated;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
