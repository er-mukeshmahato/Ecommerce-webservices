package com.mukesh.ecommerce.helper;


import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = HttpHeaders.AUTHORIZATION;
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    private static final String SECURED_URL_PREFIX = "secured";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Check if the requested path contains the secured prefix
        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {
            // Get the Authorization header
            List<String> authHeaders = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);

            // Check if Authorization header is present
            if (authHeaders != null && !authHeaders.isEmpty()) {
                String authToken = authHeaders.get(0);

                // Extract username and password from the Authorization header
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String decodedString = new String(Base64.getDecoder().decode(authToken));
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String username = tokenizer.nextToken();
                String password = tokenizer.nextToken();

                // Dummy authentication check (replace with your actual authentication logic)
                if ("user".equals(username) && "password".equals(password)) {
                    // Create a SecurityContext and set it on the request context
                    final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
                    requestContext.setSecurityContext(new SecurityContext() {
                        @Override
                        public Principal getUserPrincipal() {
                            return () -> username;
                        }

                        @Override
                        public boolean isUserInRole(String role) {
                            // Optional: Implement role-based access control
                            return true; // For simplicity, assume all users have access
                        }

                        @Override
                        public boolean isSecure() {
                            return currentSecurityContext.isSecure();
                        }

                        @Override
                        public String getAuthenticationScheme() {
                            return SecurityContext.BASIC_AUTH;
                        }
                    });
                    return; // Authentication success, allow request to proceed
                }
            }

            // If authentication fails, abort the request with unauthorized status
            Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED)
                    .entity("User cannot access the resource.")
                    .build();
            requestContext.abortWith(unauthorizedStatus);
        }
    }
}
