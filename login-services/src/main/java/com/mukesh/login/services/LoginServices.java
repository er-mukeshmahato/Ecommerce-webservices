import com.mukesh.login.helper.JwtTokenUtil;
import com.mukesh.login.helper.PasswordHasher;
import com.mukesh.login.model.UserModel;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mukesh.login.DBManager.DatabaseService;
import com.mukesh.login.helper.TokenUtil;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/authentication")
public class LoginServices {

    private static final MongoCollection<Document> usersCollection = DatabaseService.getDatabase().getCollection("users");

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserModel loginUser) {
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
            e.printStackTrace();
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"An unexpected error occurred: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
