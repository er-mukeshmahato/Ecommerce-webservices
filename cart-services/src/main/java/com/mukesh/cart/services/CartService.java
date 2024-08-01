package com.mukesh.cart.services;

import com.mukesh.cart.services.DBManager.DatabaseService;
import com.mukesh.cart.services.model.CartModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/cart")
public class CartService {

    private static final MongoCollection<Document> cartCollection = DatabaseService.getDatabase().getCollection("carts");
    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToCart(CartModel cartItem) {
        try {
            // Validate input
            if (cartItem.getUserId() == null || cartItem.getProductId() == null || cartItem.getQuantity() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("{\"message\": \"Invalid cart item data\"}")
                               .build();
            }

            Document query = new Document("userId", cartItem.getUserId())
                                 .append("productId", cartItem.getProductId());

            Document existingCartItem = cartCollection.find(query).first();

            if (existingCartItem != null) {
                // Update existing item
                int newQuantity = existingCartItem.getInteger("quantity") + cartItem.getQuantity();
                Document updateFields = new Document("quantity", newQuantity)
                                        .append("price", cartItem.getPrice())
                                        .append("imageUrl", cartItem.getImageUrl())
                                        .append("productName", cartItem.getProductName())
                                        .append("emailId", cartItem.getEmailId());
                cartCollection.updateOne(query, new Document("$set", updateFields));
            } else {
                // Add new item to cart
                Document newCartItem = new Document("userId", cartItem.getUserId())
                                        .append("productId", cartItem.getProductId())
                                        .append("quantity", cartItem.getQuantity())
                                        .append("price", cartItem.getPrice())
                                        .append("imageUrl", cartItem.getImageUrl())
                                        .append("productName", cartItem.getProductName())
                                        .append("emailId", cartItem.getEmailId());
                cartCollection.insertOne(newCartItem);
            }

            return Response.ok("{\"message\": \"Item added to cart\"}")
                           .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding item to cart", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"message\": \"An error occurred while processing the request\"}")
                           .build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartItems() {
        try {
            List<CartModel> cartList = new ArrayList<>();
            
            try (MongoCursor<Document> cursor = cartCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();              
                    CartModel cart = new CartModel(
                        doc.getString("userId"),
                        doc.getString("productId"),
                        doc.getInteger("quantity"),
                        doc.getString("price"),
                        doc.getString("imageUrl"),
                        doc.getString("productName"),
                        doc.getString("emailId")
                    );
                    cartList.add(cart);
                }
            }

            return Response.ok(cartList).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching cart items", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"message\": \"An error occurred while fetching the cart items\"}")
                           .build();
        }
    }
}
