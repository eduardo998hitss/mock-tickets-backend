package com.example.mocktickets.api;

import com.example.mocktickets.dto.PageResponse;
import com.example.mocktickets.dto.TicketCreateRequest;
import com.example.mocktickets.dto.TicketResponse;
import com.example.mocktickets.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.mocktickets.dto.TicketStatusUpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketResponse> createWithUpload(
        @RequestPart("payload") @Valid TicketCreateRequest req,
        @RequestPart("file") MultipartFile file
    ) {
      var adjusted = new TicketCreateRequest(
          req.company(), req.category(), req.subject(), req.local(), req.ticket(),
          file != null ? file.getOriginalFilename() : req.attachment()
      );
      return ResponseEntity.status(HttpStatus.CREATED).body(service.create(adjusted));
    }
    
    @PostMapping
    public ResponseEntity<TicketResponse> create(
            @RequestBody @Valid TicketCreateRequest req
    ) {
        TicketResponse created = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable("id") String id) {
        return service.get(id);
    }

    @GetMapping
    public PageResponse<TicketResponse> list(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "q", required = false) String q
    ) {
      return service.list(page, size, q);
    }
    
    @PatchMapping("/{id}/status")
    public TicketResponse updateStatus(
        @PathVariable String id,
        @RequestBody @Valid TicketStatusUpdateRequest req
    ) {
      return service.updateStatus(id, req.status());
    }

}