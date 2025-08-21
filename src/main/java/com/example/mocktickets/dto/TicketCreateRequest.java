package com.example.mocktickets.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketCreateRequest(
    @NotBlank String company,
    @NotBlank String category,
    @NotBlank String subject,
    @NotBlank String local,
    @NotBlank String ticket,
    String attachment
) {}