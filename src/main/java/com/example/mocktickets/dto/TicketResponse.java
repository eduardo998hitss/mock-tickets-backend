package com.example.mocktickets.dto;

import java.time.Instant;

public record TicketResponse(
    String id,
    String company,
    String category,
    String subject,
    String local,
    String ticket,
    String attachment,
    String status,
    Instant createdAt
) {}