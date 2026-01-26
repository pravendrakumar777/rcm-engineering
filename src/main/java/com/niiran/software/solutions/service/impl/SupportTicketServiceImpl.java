package com.niiran.software.solutions.service.impl;

import com.niiran.software.solutions.domain.SupportTicket;
import com.niiran.software.solutions.domain.dto.SupportTicketDTO;
import com.niiran.software.solutions.repository.SupportTicketRepository;
import com.niiran.software.solutions.service.SupportTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {
    private static final Logger log = LoggerFactory.getLogger(SupportTicketServiceImpl.class);
    private final SupportTicketRepository supportTicketRepository;

    public SupportTicketServiceImpl(SupportTicketRepository supportTicketRepository) {
        this.supportTicketRepository = supportTicketRepository;
    }

    @Override
    public Page<SupportTicket> list(String status, String assignee, Pageable pageable) {
        log.info("Supports | List | INIT | status: {} | assignee: {} | page: {} | size: {} | sort: {}",
                status, assignee, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<SupportTicket> result;

        if (status != null && assignee != null) {
            log.debug("Supports | List | Executing query: findByStatusAndAssignee | status: {} | assignee: {}",
                    status, assignee);
            result = supportTicketRepository.findByStatusAndAssignee(status, assignee, pageable);
        } else if (status != null) {
            log.debug("Supports | List | Executing query: findByStatus | status: {}", status);
            result = supportTicketRepository.findByStatus(status, pageable);
        } else if (assignee != null) {
            log.debug("Supports | List | Executing query: findByAssignee | assignee: {}", assignee);
            result = supportTicketRepository.findByAssignee(assignee, pageable);
        } else {
            log.debug("Supports | List | Executing query: findAll");
            result = supportTicketRepository.findAll(pageable);
        }
        log.info("Supports | List | RESULT | totalElements: {} | totalPages: {} | status: {} | assignee: {}",
                result.getTotalElements(), result.getTotalPages(), status, assignee);
        return result;
    }

    @Override
    public SupportTicket get(String ticketNo) {
        log.info("Supports | Get | ticketNo: {}", ticketNo);
        return supportTicketRepository.findByTicketNo(ticketNo)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
    }

    @Override
    public SupportTicket create(SupportTicketDTO dto, String actor) {
        log.info("Supports | Create | title: {} | actor: {}", dto.getTitle(), actor);
        SupportTicket ticket = new SupportTicket();
        String uniqueId = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1000000));
        ticket.setTicketNo("RCM" + uniqueId);
        log.info("Ticket Number : {}", uniqueId);
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setRequester(dto.getRequester());
        ticket.setPriority(dto.getPriority());
        ticket.setCategory(dto.getCategory());
        ticket.setCreatedBy(actor);
        return supportTicketRepository.save(ticket);
    }


    @Override
    public SupportTicket update(String ticketNo, SupportTicketDTO dto, String actor) {
        log.info("Supports | Update | ticketNo: {} | actor: {}", ticketNo, actor);
        SupportTicket ticket = get(ticketNo);
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(dto.getPriority());
        ticket.setCategory(dto.getCategory());
        ticket.setModifiedBy(actor);
        return supportTicketRepository.save(ticket);
    }

    @Override
    public SupportTicket transitionStatus(String ticketNo, String status, String actor) {
        log.info("Supports | Transition | ticketNo: {} | status: {} | actor: {}", ticketNo, status, actor);
        SupportTicket ticket = get(ticketNo);
        ticket.setStatus(status);
        ticket.setModifiedBy(actor);
        return supportTicketRepository.save(ticket);
    }

    @Override
    public void addComment(String ticketNo, String comment, String visibility, String actor) {
        log.info("Supports | Comment | ticketNo: {} | visibility: {} | actor: {}", ticketNo, visibility, actor);
        SupportTicket ticket = get(ticketNo);
        String notes = (ticket.getResolutionNotes() == null ? "" : ticket.getResolutionNotes() + "\n");
        ticket.setResolutionNotes(notes + "[" + visibility + "] " + actor + ": " + comment);
        supportTicketRepository.save(ticket);
    }

    public SupportTicketDTO toDto(SupportTicket ticket) {
        return new SupportTicketDTO(
                ticket.getId(), ticket.getTicketNo(), ticket.getTitle(), ticket.getDescription(),
                ticket.getRequester(), ticket.getAssignee(), ticket.getStatus(), ticket.getPriority(),
                ticket.getCategory(), ticket.getResolutionNotes()
        );
    }
}
