/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mukesh.login.helper;import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Date;

public class TokenUtil {
    private static final String SECRET_KEY = "firstservices"; // Ensure this key is kept secure

    /**
     * Generates a JWT token for the given email.
     *
     * @param email The email to be included in the token
     * @return The generated JWT token
     */
    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Extracts the claims from the JWT token.
     *
     * @param token The JWT token
     * @return The claims extracted from the token
     * @throws SignatureException If the token is invalid
     */
    public static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    /**
     * Extracts the email from the JWT token.
     *
     * @param token The JWT token
     * @return The email extracted from the token
     */
    public static String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Checks if the token is expired.
     *
     * @param token The JWT token
     * @return True if the token is expired, otherwise false
     */
    public static boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().before(new Date());
    }

    /**
     * Validates the token by checking its expiration and subject.
     *
     * @param token  The JWT token
     * @param email  The email to validate against the token
     * @return True if the token is valid, otherwise false
     */
    public static boolean validateToken(String token, String email) {
        return (email.equals(getEmailFromToken(token)) && !isTokenExpired(token));
    }
}
