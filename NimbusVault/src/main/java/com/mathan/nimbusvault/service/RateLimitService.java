package com.mathan.nimbusvault.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitService {

    private final Bucket loginBucket;
    private final Bucket forgotPasswordBucket;

    public RateLimitService() {

        Bandwidth loginLimit = Bandwidth.builder()
                .capacity(5)
                .refillGreedy(5, Duration.ofMinutes(1))
                .build();

        loginBucket = Bucket.builder()
                .addLimit(loginLimit)
                .build();

        Bandwidth forgotPasswordLimit = Bandwidth.builder()
                .capacity(3)
                .refillGreedy(3, Duration.ofMinutes(5))
                .build();

        forgotPasswordBucket = Bucket.builder()
                .addLimit(forgotPasswordLimit)
                .build();
    }

    public boolean allowLoginAttempt() {
        return loginBucket.tryConsume(1);
    }

    public boolean allowForgotPasswordAttempt() {
        return forgotPasswordBucket.tryConsume(1);
    }
}