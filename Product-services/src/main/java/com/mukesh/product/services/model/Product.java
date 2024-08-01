/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mukesh.product.services.model;

/**
 *
 * @author ermuk
 */
public class Product {
    private String id; // MongoDB _id field
    private String productName;
    private String price;
    private String category;
    private String gender;
    private String imageUrl;

    // Constructors
    public Product() { }

    public Product(String id, String productName, String price, String category, String gender, String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
