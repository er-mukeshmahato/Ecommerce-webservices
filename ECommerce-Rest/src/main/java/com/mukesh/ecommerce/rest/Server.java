/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mukesh.ecommerce.rest;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.HashMap;
import java.util.Map;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import com.stripe.StripeClient;

public class Server {

    public static void main(String[] args) {
        port(9090);
        Stripe.apiKey = "sk_test_51PbjF8RtI4hY7Ggt7bkJa0fueWF3mlQ1mqGFG1NMew7hhpvwQW9Sxy5VlqyudvUTCbf7KEakQyQMMLyh2igjHVjv00NyBSAozm";

        // Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
        StripeClient client = new StripeClient("sk_test_51PbjF8RtI4hY7Ggt7bkJa0fueWF3mlQ1mqGFG1NMew7hhpvwQW9Sxy5VlqyudvUTCbf7KEakQyQMMLyh2igjHVjv00NyBSAozm");

        SessionCreateParams params
                = SessionCreateParams.builder()
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("T-shirt")
                                                                        .build()
                                                        )
                                                        .setUnitAmount(2000L)
                                                        .build()
                                        )
                                        .setQuantity(1L)
                                        .build()
                        )
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:9090/ECommerce-Rest/success.html")
                        .setCancelUrl("http://localhost:9090/ECommerce-Rest/cancel.html")
                        .build();

    }
}
