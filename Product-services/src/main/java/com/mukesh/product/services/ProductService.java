package com.mukesh.product.services;

import com.mukesh.product.services.model.Product;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mukesh.user.DBManager.DatabaseService;
import org.bson.Document;
import org.bson.types.ObjectId;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/product")
public class ProductService {

    private static final MongoDatabase database = DatabaseService.getDatabase();
    private static final MongoCollection<Document> productCollection = database.getCollection("products");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        List<Product> productList = new ArrayList<>();
        
        try {
            try (MongoCursor<Document> cursor = productCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    Product product = new Product(
                        doc.getObjectId("_id").toHexString(),
                        doc.getString("productName"),
                        doc.getString("price"),
                        doc.getString("category"),
                        doc.getString("gender"),
                        doc.getString("imageUrl")
                    );
                    productList.add(product);
                }
            }
            return Response.ok(productList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"message\": \"Error retrieving products\"}")
                           .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            Document query = new Document("_id", objectId);
            Document doc = productCollection.find(query).first();

            if (doc != null) {
                Product product = new Product(
                    doc.getObjectId("_id").toHexString(),
                    doc.getString("productName"),
                    doc.getString("price"),
                    doc.getString("category"),
                    doc.getString("gender"),
                    doc.getString("imageUrl")
                );
                return Response.ok(product).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("{\"message\": \"Product not found\"}")
                               .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"message\": \"Invalid ID format\"}")
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"message\": \"Error retrieving product\"}")
                           .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(Product product) {
        try {
            ObjectId id = new ObjectId();
            Document doc = new Document("_id", id)
                    .append("productName", product.getProductName())
                    .append("price", product.getPrice())
                    .append("category", product.getCategory())
                    .append("gender", product.getGender())
                    .append("imageUrl", product.getImageUrl());

            productCollection.insertOne(doc);

            return Response.status(Response.Status.CREATED)
                           .entity("{\"message\": \"Product added successfully\"}")
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"message\": \"Error adding product\"}")
                           .build();
        }
    }
}
