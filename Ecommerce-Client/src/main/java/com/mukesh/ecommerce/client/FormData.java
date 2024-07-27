package com.mukesh.ecommerce.client;

import jakarta.ws.rs.FormParam;

public class FormData {

    @FormParam("itemName")
    private String itemName;

    @FormParam("itemPrice")
    private double itemPrice;

    // Default constructor (required for Jersey Client)
    public FormData() {
    }

    // Constructor with parameters
    public FormData(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    // Getters and setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
