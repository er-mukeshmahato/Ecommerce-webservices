/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mukesh.login.helper;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public String passwordToHash(String password) {
        return get_SHA_256_SecurePassword(password);
    }

    public String hashPassword(String password) {
        return passwordToHash(password);
    }

    public boolean authenticatePassword(String enteredPassword, String storedHashedPassword) {
        String hashedEnteredPassword = passwordToHash(enteredPassword);
        return storedHashedPassword.equals(hashedEnteredPassword);
    }

    private static String get_SHA_256_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02x", aByte));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
        return generatedPassword;
    }
}
