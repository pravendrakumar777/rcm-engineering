package com.niiran.software.solutions.repository;

import com.niiran.software.solutions.domain.SupportTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

    Page<SupportTicket> findByStatus(String status, Pageable pageable);
    Page<SupportTicket> findByAssignee(String assignee, Pageable pageable);
    Page<SupportTicket> findByStatusAndAssignee(String status, String assignee, Pageable pageable);
    Page<SupportTicket> findAll(Pageable pageable);

    Optional<SupportTicket> findByTicketNo(String ticketNo);
}
