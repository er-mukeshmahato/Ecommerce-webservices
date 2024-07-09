/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mukesh.ecommerce.client;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
/**
 *
 * @author ermuk
 */
public class EcommerceClient {

    public static void main(String[] args) {
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/ECommerce-Rest");

        String response = webResource.get(String.class);
        System.out.println("Response from server: " + response);
    }
}
