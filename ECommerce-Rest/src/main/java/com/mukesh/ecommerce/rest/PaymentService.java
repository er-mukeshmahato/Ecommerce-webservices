package com.mukesh.ecommerce.rest;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import spark.Request;
import spark.Response;

public class PaymentService {

    private static final String STRIPE_SECRET_KEY = "sk_test_51PbjF8RtI4hY7Ggt7bkJa0fueWF3mlQ1mqGFG1NMew7hhpvwQW9Sxy5VlqyudvUTCbf7KEakQyQMMLyh2igjHVjv00NyBSAozm";

    public static void initialize() {
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    public static String createCheckoutSession(Request request, Response response) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:4242/success")
                    .setCancelUrl("http://localhost:4242/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(2000L)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("T-shirt")
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();

            Session session = Session.create(params);
            response.redirect(session.getUrl(), 303);
            return "";
        } catch (StripeException e) {
            response.status(500);
            return "Error creating checkout session: " + e.getMessage();
        }
    }
}
