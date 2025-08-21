package com.example.mocktickets.service;

import com.example.mocktickets.dto.PageResponse;
import com.example.mocktickets.dto.TicketCreateRequest;
import com.example.mocktickets.dto.TicketResponse;

public interface TicketService {
    TicketResponse create(TicketCreateRequest req);
    TicketResponse get(String id);
    PageResponse<TicketResponse> list(int page, int size, String q);

 
    TicketResponse updateStatus(String id, String status);
}
