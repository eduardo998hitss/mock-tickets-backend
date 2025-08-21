package com.example.mocktickets.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketStatusUpdateRequest(
    @NotBlank String status // OPEN, IN_PROGRESS, CLOSED
) {}