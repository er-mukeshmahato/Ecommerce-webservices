/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mukesh.cart.services.DBManager;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/database")
public class DatabaseService {

    private static final String DATABASE_NAME = "Ecommerce"; // Replace with your MongoDB database name
    private static final MongoClient mongoClient;
    private static final MongoDatabase database;

    static {
        // Initialize MongoDB connection
        mongoClient = MongoClients.create(); // Use default connection settings (localhost, port 27017)
        database = mongoClient.getDatabase(DATABASE_NAME);
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    // Close MongoDB connection when application shuts down
    public static void close() {
        mongoClient.close();
    }
    
   
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
     // Check if MongoDB is connected and return JSON response
    public static String checkConnection() {
        JSONObject response = new JSONObject();
        try {
            response.put("status", "connected");
            response.put("message", "MongoDB connected successfully");
        } catch (JSONException e) {
            response.put("status", "error");
            response.put("message", "Failed to connect to MongoDB: " + e.getMessage());
        }
        return response.toString();
    }
}
