package com.mukesh.ecommerce.rest;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/checkout")
public class CheckoutService {

    private final String stripeSecretKey = "sk_test_51PbjF8RtI4hY7Ggt7bkJa0fueWF3mlQ1mqGFG1NMew7hhpvwQW9Sxy5VlqyudvUTCbf7KEakQyQMMLyh2igjHVjv00NyBSAozm";
    private final String stripePublicKey = "pk_test_51PbjF8RtI4hY7GgtXxd2m8KOI4j2nDg4fKfkZcMvpuOCeXlzF5umSerKxyOkisaF0L5bVvBsj23lWuw6ImSmcHH900RxvwVknX";

    private static final Gson gson = new Gson();

    @POST
    @Path("/create-payment-intent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CreatePaymentResponse createPaymentIntent(CreatePayment createPayment) {
        Stripe.apiKey = stripeSecretKey;
        String email="er.mukeshmahatomm@gmail.com";

        try {
            // Calculate the total order amount based on items
            int orderAmount = calculateOrderAmount(createPayment.getItems());

            // Create PaymentIntent parameters
            PaymentIntentCreateParams.Builder builder = new PaymentIntentCreateParams.Builder()
                    .setCurrency("usd")
                    .setAmount((long) orderAmount * 100);  // Amount should be in cents

            // Optionally, add receipt email
            builder.putMetadata("receipt_email", email);

            // Create PaymentIntent
             PaymentIntent paymentIntent = PaymentIntent.create(builder.build());
             Map<String,Object> customerParameter=new HashMap<String,Object>();
             customerParameter.put("email", email);
             Customer newCustomer=Customer.create(customerParameter);
            // Prepare response data
            CreatePaymentResponse responseData = new CreatePaymentResponse(paymentIntent.getClientSecret(), stripePublicKey);

            return responseData;
        } catch (StripeException e) {
            throw new WebApplicationException("Error creating payment intent: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Class representing the structure of items sent in the request
    static class CreatePayment {
        @SerializedName("items")
        CreatePaymentItem[] items;

        public CreatePaymentItem[] getItems() {
            return items;
        }
    }

    // Class representing individual items in the request
    static class CreatePaymentItem {
        @SerializedName("id")
        String id;

        public String getId() {
            return id;
        }
    }

    // Class representing the response to be sent back to the client
    static class CreatePaymentResponse {
        @SerializedName("clientSecret")
        private String clientSecret;

        @SerializedName("stripePublicKey")
        private String stripePublicKey;

        public CreatePaymentResponse(String clientSecret, String stripePublicKey) {
            this.clientSecret = clientSecret;
            this.stripePublicKey = stripePublicKey;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public String getStripePublicKey() {
            return stripePublicKey;
        }
    }

    // Calculate order amount (dummy implementation)
    static int calculateOrderAmount(CreatePaymentItem[] items) {
        // Replace this with your actual calculation logic based on items
        // For demo purposes, just summing up some fixed amount per item
        int totalAmount = 0;
        for (CreatePaymentItem item : items) {
            // Example: Assume each item costs $10
            totalAmount += 1000; // $10 in cents
        }
        return totalAmount;
    }
}
