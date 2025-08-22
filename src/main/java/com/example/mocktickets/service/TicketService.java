package com.example.mocktickets.service;

import com.example.mocktickets.api.common.PageResponse;
import com.example.mocktickets.api.ticket.request.TicketCreateRequest;
import com.example.mocktickets.api.ticket.response.TicketResponse;

public interface TicketService {
    TicketResponse create(TicketCreateRequest req);
    TicketResponse get(String id);
    PageResponse<TicketResponse> list(int page, int size, String q);

 
    TicketResponse updateStatus(String id, String status);
}
