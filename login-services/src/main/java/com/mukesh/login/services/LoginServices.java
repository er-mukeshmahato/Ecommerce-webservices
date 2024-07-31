import com.mukesh.login.helper.JwtTokenUtil;
import com.mukesh.login.helper.PasswordHasher;
import com.mukesh.login.model.UserModel;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mukesh.login.DBManager.DatabaseService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/authentication")
public class LoginServices {

    private static final MongoCollection<Document> usersCollection = DatabaseService.getDatabase().getCollection("users");
    

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(UserModel loginUser) {
        if (loginUser.getEmail() == null || loginUser.getPassword() == null) {
            return "{\"message\": \"Email and password are required\"}";
        }

        Document query = new Document("email", loginUser.getEmail());
        Document userDocument = usersCollection.find(query).first();

        if (userDocument != null) {
            String storedPassword = userDocument.getString("password");
            PasswordHasher passwordHasher = new PasswordHasher();
            String hashedPassword = passwordHasher.hashPassword(loginUser.getPassword());

            if (hashedPassword != null && hashedPassword.equals(storedPassword)) {
               
                // Return the token as a JSON string
               return "{\"message\": \"login successful\"}";
            } else {
                return "{\"message\": \"Invalid password\"}";
            }
        } else {
            return "{\"message\": \"User not found\"}";
        }
    }
}
