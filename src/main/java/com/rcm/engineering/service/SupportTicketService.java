package com.rcm.engineering.service;

import com.rcm.engineering.domain.SupportTicket;
import com.rcm.engineering.domain.dto.SupportTicketDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupportTicketService {

    Page<SupportTicket> list(String status, String assignee, Pageable pageable);
    SupportTicket get(String ticketNo);
    SupportTicket create(SupportTicketDTO dto, String actor);
    SupportTicket update(String ticketNo, SupportTicketDTO dto, String actor);
    SupportTicket transitionStatus(String ticketNo, String status, String actor);
    void addComment(String ticketNo, String comment, String visibility, String actor);
    SupportTicketDTO toDto(SupportTicket ticket);
}
