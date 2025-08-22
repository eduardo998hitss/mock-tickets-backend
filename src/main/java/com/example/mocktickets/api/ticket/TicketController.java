package com.example.mocktickets.api.ticket;

import com.example.mocktickets.api.common.PageResponse;
import com.example.mocktickets.api.ticket.request.TicketCreateRequest;
import com.example.mocktickets.api.ticket.request.TicketStatusUpdateRequest;
import com.example.mocktickets.api.ticket.response.TicketResponse;
import com.example.mocktickets.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;
    
    private final ObjectMapper objectMapper = new ObjectMapper(); // usa Jackson do Spring Boot


    public TicketController(TicketService service) {
        this.service = service;
    }

    // POST JSON
    @PostMapping
    public ResponseEntity<TicketResponse> create(@RequestBody @Valid TicketCreateRequest req) {
        TicketResponse created = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /{id}
    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable("id") String id) {
        return service.get(id);
    }

    // GET list
    @GetMapping
    public PageResponse<TicketResponse> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "q", required = false) String q
    ) {
        return service.list(page, size, q);
    }

    // PATCH /{id}/status
    @PatchMapping("/{id}/status")
    public TicketResponse updateStatus(
            @PathVariable("id") String id,
            @RequestBody @Valid TicketStatusUpdateRequest req
    ) {
        return service.updateStatus(id, req.status());
    }

    // POST multipart /upload
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketResponse> createWithUpload(
            @RequestPart("payload") String payload, // <- aceita qualquer content-type
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        final TicketCreateRequest req;
        try {
            req = objectMapper.readValue(payload, TicketCreateRequest.class);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "payload inválido (JSON)", e);
        }

        var adjusted = new TicketCreateRequest(
                req.company(),
                req.category(),
                req.subject(),
                req.local(),
                req.ticket(),
                (file != null ? file.getOriginalFilename() : req.attachment())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(adjusted));
    }

}
