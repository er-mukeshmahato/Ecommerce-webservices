package com.mukesh.ecommerce.rest;

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
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("milk", "tasty"));
        productList.add(new Product("bread", "fresh"));
        productList.add(new Product("cheese", "creamy"));
        productList.add(new Product("coffee", "aromatic"));
        productList.add(new Product("tea", "refreshing"));

        // Convert list to JSON array
        JSONArray jsonArray = new JSONArray();
        for (Product product : productList) {
            JSONObject jsonProduct = new JSONObject();
            jsonProduct.put("productname", product.getName());
            jsonProduct.put("description", product.getDescription());
            jsonArray.put(jsonProduct);
        }

        // Return JSON array as string
        return jsonArray.toString();
    }

    // Product class representing individual products
    private static class Product {
        private String name;
        private String description;

        public Product(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
