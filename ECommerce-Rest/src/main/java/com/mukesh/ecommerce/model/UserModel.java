/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mukesh.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author ermuk
 */
public class UserModel {

    private final String username;
    private final String password;
    private final String email;
    private final String userid;

    @JsonCreator
    public UserModel(@JsonProperty(value = "username") String username, @JsonProperty(value = "password") String password, @JsonProperty(value = "userid") String userid, @JsonProperty(value = "email") String email) {
        this.username = username;
        this.password = password;
        this.userid = userid;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userid;
    }
}
