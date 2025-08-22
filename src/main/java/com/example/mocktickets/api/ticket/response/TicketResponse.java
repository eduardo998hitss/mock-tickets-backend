package com.example.mocktickets.api.ticket.response;

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