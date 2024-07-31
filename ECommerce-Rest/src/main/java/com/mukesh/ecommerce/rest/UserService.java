package com.mukesh.ecommerce.rest;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mukesh.ecommerce.DBManager.DatabaseService;
import com.mukesh.ecommerce.helper.PasswordHasher;
import jakarta.ws.rs.Consumes;
import com.mukesh.ecommerce.model.UserModel;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.bson.Document;
import org.bson.types.ObjectId;

@Path("/users")
public class UserService {

    private static final MongoCollection<Document> usersCollection = DatabaseService.getDatabase().getCollection("users");

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public String UserTest() {
        return "{\"message\": \"User detail page\"}";
    }

    /**
     * Registers a new user.
     *
     * @param user UserModel object representing the new user.
     * @return JSON representation of the newly registered user.
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String registerUser(UserModel user) {
        ObjectId userId = new ObjectId();
        PasswordHasher passwordHasher = new PasswordHasher();
        String hashedPassword = passwordHasher.hashPassword(user.getPassword());

        Document newUser = new Document("_id", userId)
                .append("username", user.getUsername())
                .append("email", user.getEmail())
                .append("password", hashedPassword); // Store hashed passwords in production

        try {
            usersCollection.insertOne(newUser);
            return "success";
        } catch (MongoException e) {
            // Handle MongoDB exception
            return "Failed to create user: " + e.getMessage();
        }
    }

}
