package com.example.mocktickets.service;

import com.example.mocktickets.api.common.PageResponse;
import com.example.mocktickets.api.ticket.request.TicketCreateRequest;
import com.example.mocktickets.api.ticket.response.TicketResponse;
import com.example.mocktickets.config.MockBehavior;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Profile("mock")
public class MockTicketService implements TicketService {

    private final Map<String, TicketResponse> store = new ConcurrentHashMap<>();
    private final MockBehavior behavior;

    public MockTicketService(MockBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public TicketResponse create(TicketCreateRequest req) {
        behavior.maybeDelay();
        behavior.maybeRandomError();

        String id = UUID.randomUUID().toString();
        TicketResponse resp = new TicketResponse(
                id,
                req.company(),
                req.category(),
                req.subject(),
                req.local(),
                req.ticket(),
                req.attachment(),
                "OPEN",
                Instant.now()
        );
        store.put(id, resp);
        return resp;
    }

    @Override
    public TicketResponse get(String id) {
        behavior.maybeDelay();
        behavior.maybeRandomError();

        TicketResponse r = store.get(id);
        if (r == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        return r;
    }

    @Override
    public PageResponse<TicketResponse> list(int page, int size, String q) {
        behavior.maybeDelay();
        behavior.maybeRandomError();

        List<TicketResponse> all = new ArrayList<>(store.values());
        if (q != null && !q.isBlank()) {
            String qq = q.toLowerCase();
            all = all.stream().filter(t ->
                    (t.company() != null && t.company().toLowerCase().contains(qq)) ||
                    (t.subject() != null && t.subject().toLowerCase().contains(qq)) ||
                    (t.category() != null && t.category().toLowerCase().contains(qq)) ||
                    (t.local() != null && t.local().toLowerCase().contains(qq)) ||
                    (t.ticket() != null && t.ticket().toLowerCase().contains(qq))
            ).collect(Collectors.toList());
        }
        int total = all.size();
        int from = Math.min(page * size, total);
        int to = Math.min(from + size, total);
        List<TicketResponse> slice = all.subList(from, to);
        return new PageResponse<>(slice, page, size, total);
    }

     
    @Override
    public TicketResponse updateStatus(String id, String status) {
        behavior.maybeDelay();
        behavior.maybeRandomError();

        TicketResponse existing = store.get(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }

        // validação simples de status permitido
        Set<String> allowed = Set.of("OPEN", "IN_PROGRESS", "CLOSED");
        if (!allowed.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
        }

        TicketResponse updated = new TicketResponse(
                existing.id(),
                existing.company(),
                existing.category(),
                existing.subject(),
                existing.local(),
                existing.ticket(),
                existing.attachment(),
                status,
                existing.createdAt()
        );
        store.put(id, updated);
        return updated;
    }
}
