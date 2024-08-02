package com.mukesh.login.services;

import com.mongodb.client.MongoCollection;
import com.mukesh.login.DBManager.DatabaseService;
import com.mukesh.login.helper.PasswordHasher;
import com.mukesh.login.helper.TokenUtil;
import com.mukesh.login.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.bson.Document;

@Path("/authentication")
public class LoginServices {

    private static final MongoCollection<Document> usersCollection = DatabaseService.getDatabase().getCollection("users");

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserModel loginUser, @Context HttpServletRequest request) {
        try {
            // Validate input
            if (loginUser.getEmail() == null || loginUser.getPassword() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Email and password are required\"}")
                        .build();
            }

            // Find user in the database
            Document query = new Document("email", loginUser.getEmail());
            Document userDocument = usersCollection.find(query).first();

            if (userDocument != null) {
                String storedPassword = userDocument.getString("password");
                PasswordHasher passwordHasher = new PasswordHasher();
                String hashedPassword = passwordHasher.hashPassword(loginUser.getPassword());

                if (hashedPassword != null && hashedPassword.equals(storedPassword)) {
                    try {
                        // Generate token
                        String token = TokenUtil.generateToken(loginUser.getEmail());

                        if (token == null || token.isEmpty()) {
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                    .entity("{\"message\": \"Error generating token\"}")
                                    .build();
                        }
                        HttpSession session = request.getSession();
                        session.setAttribute("username", loginUser.getEmail());
                        // Return success response with token
                        return Response.ok("{\"message\": \"Login successful\", \"token\": \"" + token + "\"}")
                                .build();
                    } catch (Exception e) {
                        // Handle token generation exceptions
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("{\"message\": \"Error generating token: " + e.getMessage() + "\"}")
                                .build();
                    }
                } else {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"message\": \"Invalid credentials\"}")
                            .build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"User not found\"}")
                        .build();
            }
        } catch (Exception e) {

// Log the exception (use a logging framework for real-world applications)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"An unexpected error occurred: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    private Response fetchUserDetailsFromUserService(String email) {
        // Define the base URL for the UserService
        final String USER_SERVICE_BASE_URL = "http://localhost:9090/user-services/resources/users/user/details/";

        // Create an HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        // Build the HttpRequest with the target URL
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USER_SERVICE_BASE_URL + email))
                .header("Accept", "application/json")
                .build();

        try {
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Return the response as a JAX-RS Response
            return Response.status(response.statusCode())
                    .entity(response.body())
                    .build();
        } catch (IOException | InterruptedException e) {
            // Handle any exceptions and return an error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Error calling UserService: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/current_user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUser(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            String email = (String) session.getAttribute("username");

            // Call UserService to get user details by email
            return fetchUserDetailsFromUserService(email);
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":\"error\",\"message\":\"User not logged in\"}")
                    .build();
        }
    }
}
