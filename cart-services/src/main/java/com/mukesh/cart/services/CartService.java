package com.mukesh.cart.services;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.mongodb.client.MongoCollection;
import com.mukesh.cart.services.DBManager.DatabaseService;
import com.mukesh.cart.services.model.CartModel;
import org.bson.Document;

@Path("/cart")
public class CartService {

    private static final MongoCollection<Document> cartCollection = DatabaseService.getDatabase().getCollection("carts");

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToCart(CartModel cartItem) {
        if (cartItem.getUserId() == null || cartItem.getProductId() == null || cartItem.getQuantity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"message\": \"Invalid cart item data\"}")
                           .build();
        }

        Document query = new Document("userId", cartItem.getUserId())
                             .append("productId", cartItem.getProductId());

        Document existingCartItem = cartCollection.find(query).first();

        if (existingCartItem != null) {
            // Update quantity and other details if item already in cart
            int newQuantity = existingCartItem.getInteger("quantity") + cartItem.getQuantity();
            cartCollection.updateOne(query, new Document("$set", new Document("quantity", newQuantity)
                                                     .append("price", cartItem.getPrice())
                                                     .append("imageUrl", cartItem.getImageUrl())
                                                     .append("productName", cartItem.getProductName())
                                                     .append("emailId", cartItem.getEmailId())));
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
    }
}
