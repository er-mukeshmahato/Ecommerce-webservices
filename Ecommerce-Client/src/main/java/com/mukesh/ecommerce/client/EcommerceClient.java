package com.mukesh.ecommerce.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class EcommerceClient {

    private static final String BASE_URL = "http://localhost:9090/ECommerce-Rest/resources";

    public static void main(String[] args) {
        try {
            Client client = ClientBuilder.newClient();

            performCheckout(client);
            getProducts(client);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void performCheckout(Client client) {
        String checkoutEndpoint = BASE_URL + "/checkout/create-payment-intent";

        FormData formData = new FormData("Sample Item", 100.0);

        Form form = new Form();
        form.param("itemName", formData.getItemName());
        form.param("itemPrice", String.valueOf(formData.getItemPrice()));

        Response response = client.target(checkoutEndpoint)
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .post(Entity.form(form));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String jsonResponse = response.readEntity(String.class);
            System.out.println("Checkout response: " + jsonResponse);
        } else {
            System.err.println("Error processing checkout: " + response.getStatusInfo());
        }
    }

    private static void getProducts(Client client) {
        String productsEndpoint = BASE_URL + "/products";

        String jsonResponse = client.target(productsEndpoint)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        System.out.println("Products: " + jsonResponse);
    }
}
