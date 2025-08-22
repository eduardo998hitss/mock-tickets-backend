package com.example.mocktickets.api.ticket.request;

import jakarta.validation.constraints.NotBlank;

public record TicketStatusUpdateRequest(
    @NotBlank String status // OPEN, IN_PROGRESS, CLOSED
) {}