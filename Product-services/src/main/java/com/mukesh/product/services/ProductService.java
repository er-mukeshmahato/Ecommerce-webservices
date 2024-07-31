package com.mukesh.product.services;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/product")
public class ProductService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProduct() {
        // Create a list of products
        List<JSONObject> productList = new ArrayList<>();

        // Men's Items
        // Populate the productList with products
        productList.add(createProduct("Denim Jeans", "¥5000", "Clothing", "men", "Oxford-Shirt.jpg", 1));
        productList.add(createProduct("Hooded Jacket", "¥4000", "Clothing", "men", "Oxford-Shirt.jpg", 2));
        productList.add(createProduct("Crewneck Sweater", "¥3500", "Clothing", "men", "Crewneck-Sweater.jpg", 3));
        productList.add(createProduct("Oxford Shirt", "¥3000", "Clothing", "men", "Oxford-Shirt.jpg", 4));
        productList.add(createProduct("Smartwatch", "¥6000", "Electronics", "men", "Smartwatch.jpg", 5));

        productList.add(createProduct("Yoga Leggings", "¥2000", "Clothing", "women", "Yoga-Leggings.jpg", 6));
        productList.add(createProduct("Hooded Coat", "¥4500", "Clothing", "women", "Hooded-Coat.jpg", 7));
        productList.add(createProduct("Knit Sweater", "¥3800", "Clothing", "women", "Knit-Sweater.jpg", 8));
        productList.add(createProduct("Dress Shoes", "¥5500", "Footwear", "women", "Yoga-Leggings.jpg", 9));
        productList.add(createProduct("Hair Dryer", "¥3000", "Electronics", "women", "Knit-Sweater.jpg", 10));

        productList.add(createProduct("Bluetooth Earbuds", "¥3000", "Electronics", "", "Bluetooth-earbud.jpg", 11));
        productList.add(createProduct("Power Bank", "¥2500", "Electronics", "", "Bluetooth-earbud.jpg", 12));
        productList.add(createProduct("Backpack", "¥4000", "Accessories", "", "Bluetooth-earbud.jpg", 13));
        productList.add(createProduct("Casual Sneakers", "¥3800", "Footwear", "", "Bluetooth-earbud.jpg", 14));
        productList.add(createProduct("Water Bottle", "¥1500", "Accessories", "", "Water-Bottle.jpg", 15));

        // Convert list to JSON array
        JSONArray jsonArray = new JSONArray(productList);

        // Return JSON array as string
        return jsonArray.toString();
    }

    // Helper method to create JSON objects for products
    private JSONObject createProduct(String name, String price, String category, String gender, String imageUrl,int productId) {
        JSONObject product = new JSONObject();
        product.put("productId", productId);
        product.put("productname", name);
        product.put("price", price);
        product.put("category", category);
        product.put("gender", gender);
        product.put("imageUrl", imageUrl);
        return product;
    }
}
