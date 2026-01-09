package com.rcm.engineering.resource.rest;

import com.rcm.engineering.domain.SupportTicket;
import com.rcm.engineering.domain.dto.SupportTicketDTO;
import com.rcm.engineering.service.SupportTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/supports")
public class SupportTicketController {
    private static final Logger log = LoggerFactory.getLogger(SupportTicketController.class);
    private final SupportTicketService supportTicketService;
    public SupportTicketController(SupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @PostMapping("/create")
    public ResponseEntity<SupportTicketDTO> create(@Valid @RequestBody SupportTicketDTO dto) {
        try {
            String actor = "SYSTEM";
            log.info("Supports | Create | title:{} | requester:{} | priority:{} | actor:{}",
                    dto.getTitle(), dto.getRequester(), dto.getPriority(), actor);

            SupportTicketDTO created = supportTicketService.toDto(supportTicketService.create(dto, actor));
            log.info("Supports | Create | ticketNo:{} | id:{} | status:{} | actor:{}", created.getTicketNo(), created.getId(), created.getStatus(), actor);
            return ResponseEntity.ok(created);
        } catch (Exception ex) {
            log.error("Supports | Create | ERROR | title:{} | actor:{} | message:{}",
                    dto.getTitle(), "SYSTEM", ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Page<SupportTicketDTO>> list(@RequestParam(required = false) String status,
                                                       @RequestParam(required = false) String assignee,
                                                       @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            log.info("Supports | List | status: {} |assignee: {} | page: {} | size: {}",
                    status, assignee, pageable.getPageNumber(), pageable.getPageSize());
            Page<SupportTicketDTO> result = supportTicketService.list(status, assignee, pageable)
                    .map(supportTicketService::toDto);
            log.info("Supports | List | resultCount: {} | status: {} | assignee: {}", result.getTotalElements(), status, assignee);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Supports | List | ERROR | status: {} | assignee: {} | message: {}", status, assignee, ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/fetch/{ticketNo}")
    public ResponseEntity<SupportTicketDTO> get(@PathVariable String ticketNo) {
        log.info("Supports | Fetch | INIT | ticketNo: {}", ticketNo);
        try {
            SupportTicket ticket = supportTicketService.get(ticketNo);
            SupportTicketDTO dto = supportTicketService.toDto(ticket);

            log.info("Supports | Fetch | SUCCESS | ticketNo: {} | id: {} | title: {} | status: {}",
                    ticketNo, dto.getId(), dto.getTitle(), dto.getStatus());

            return ResponseEntity.ok(dto);

        } catch (javax.persistence.EntityNotFoundException nf) {
            log.warn("Supports | Fetch | NOT_FOUND | ticketNo: {} | message: {}", ticketNo, nf.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception ex) {
            log.error("Supports | Fetch | ERROR | ticketNo: {} | message: {}", ticketNo, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SupportTicketDTO> update(@PathVariable String ticketNo,
                                                   @Valid @RequestBody SupportTicketDTO dto,
                                                   Principal principal) {
        try {
            log.info("Supports | Update | ticketNo: {} | title: {} | priority: {} | actor: {}", ticketNo, dto.getTitle(), dto.getPriority(), principal.getName());
            SupportTicketDTO updated = supportTicketService.toDto(supportTicketService.update(ticketNo, dto, principal.getName()));
            log.info("Supports | Update | ticketNo: {} | newTitle: {} | newStatus: {} | actor: {}", ticketNo, updated.getTitle(), updated.getStatus(), principal.getName());
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            log.error("Supports | Update | ERROR | ticketNo: {} | actor: {} | message: {}", ticketNo, principal.getName(), ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<SupportTicketDTO> transition(@PathVariable String ticketNo,
                                                       @RequestParam String status,
                                                       Principal principal) {
        try {
            log.info("Supports | StatusTransition | ticketNo: {} | newStatus: {} | actor: {}", ticketNo, status, principal.getName());
            SupportTicketDTO transitioned = supportTicketService.toDto(supportTicketService.transitionStatus(ticketNo, status, principal.getName()));
            log.info("Supports | StatusTransition | ticketNo: {} | finalStatus: {} | actor: {}", ticketNo, transitioned.getStatus(), principal.getName());
            return ResponseEntity.ok(transitioned);
        } catch (Exception ex) {
            log.error("Supports | StatusTransition | ERROR | ticketNo: {} | actor: {} | message: {}", ticketNo, principal.getName(), ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/comments/{ticketNo}")
    public ResponseEntity<Void> addComment(
            @PathVariable String ticketNo,
            @RequestBody Map<String, String> payload) {
        String comment = payload.getOrDefault("comment", "");
        String visibility = payload.getOrDefault("visibility", "INTERNAL");
        try {
            log.info("Supports | AddComment | ticketNo: {} | visibility: {} | comment: {}",
                    ticketNo, visibility, comment);

            supportTicketService.addComment(ticketNo, comment, visibility, "SYSTEM");
            log.info("Supports | AddComment | ticketNo: {} | status: SUCCESS", ticketNo);
            return ResponseEntity.accepted().build();

        } catch (Exception ex) {
            log.error("Supports | AddComment | ERROR | ticketNo: {} | message: {}",
                    ticketNo, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
