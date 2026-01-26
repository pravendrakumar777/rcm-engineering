package com.niiran.software.solutions.domain;

import javax.persistence.*;

@Entity
@Table(name = "support_tickets")
public class SupportTicket extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ticket_no", nullable = false, unique = true, length = 64)
    private String ticketNo;
    @Column(name = "title", nullable = false, length = 256)
    private String title;
    @Lob
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "status", nullable = false, length = 32)
    private String status = "OPEN"; // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    @Column(name = "priority", nullable = false, length = 16)
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, CRITICAL
    @Column(name = "assignee", length = 128)
    private String assignee;
    @Column(name = "requester", nullable = false, length = 128)
    private String requester;
    @Column(name = "category", length = 64)
    private String category;
    @Lob
    @Column(name = "resolution_notes")
    private String resolutionNotes;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }
}
