package com.example.project_shopping.Util;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeInitializer {
    @Value("${STRIPE_SECRET_KEY}")
    private String sk_key;

    @PostConstruct
    public void init() {
        Stripe.apiKey = sk_key;
    }
}
