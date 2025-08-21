package com.example.mocktickets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Profile("mock")
public class MockBehavior {

    @Value("${mock.latencyMs:0}")
    long latencyMs;

    @Value("${mock.errorRate:0}")
    int errorRate;

    private final HttpServletRequest request;

    public MockBehavior(HttpServletRequest request) {
        this.request = request;
    }

    public void maybeDelay() {
        long extra = 0L;
        String header = request.getHeader("x-latency");
        if (header != null) {
            try { extra = Long.parseLong(header); } catch (NumberFormatException ignored) {}
        }
        try { Thread.sleep(latencyMs + extra); } catch (InterruptedException ignored) {}
    }

    public void maybeRandomError() {
        if (errorRate > 0 && ThreadLocalRandom.current().nextInt(100) < errorRate) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "simulated outage");
        }
        if (request.getHeader("x-force-error") != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "forced error via header");
        }
    }
}