/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mukesh.ecommerce.rest;

import com.mongodb.client.MongoCollection;
import com.mukesh.ecommerce.DBManager.DatabaseService;
import com.mukesh.ecommerce.helper.PasswordHasher;
import com.mukesh.ecommerce.model.UserModel;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.bson.Document;

/**
 *
 * @author ermuk
 */
@Path("/authentication")
public class AuthenticationService {

    private static final MongoCollection<Document> usersCollection = DatabaseService.getDatabase().getCollection("users");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String AuthMessege() {
        return "{\"message\": \"Authentication Page\"}";
    }

    /**
     * Registers a new user.
     *
     * @param user UserModel object representing the new user.
     * @return JSON representation of the newly registered user.
     */
    /**
     * Logs in a user.
     *
     * @param loginUser UserModel object representing the user trying to log in.
     * @return JSON response indicating if login was successful or not.
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(UserModel loginUser) {
        Document query = new Document("email", loginUser.getEmail());
        Document userDocument = usersCollection.find(query).first();

        if (userDocument != null) {
            String storedPassword = userDocument.getString("password");
            PasswordHasher passwordHasher = new PasswordHasher();
            String hashedPassword = passwordHasher.hashPassword(loginUser.getPassword());

            if (hashedPassword == null ? storedPassword == null : hashedPassword.equals(storedPassword)) {
               
                
                
                return "{\"message\": \"Login successful\"}";
            } else {
                return "{\"message\": \"Invalid password\"}";
            }
        } else {
            return "{\"message\": \"User not found\"}";
        }

    }

}
