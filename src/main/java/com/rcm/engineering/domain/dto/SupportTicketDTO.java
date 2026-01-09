package com.rcm.engineering.domain.dto;

import javax.validation.constraints.NotBlank;

public class SupportTicketDTO {
    Long id;
    String ticketNo;
    @NotBlank(message = "Title is required")
    String title;
    @NotBlank(message = "Description is required")
    String description;
    @NotBlank(message = "Requester is required")
    String requester;
    String assignee;
    String status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    @NotBlank(message = "Priority is required")
    String priority; // LOW, MEDIUM, HIGH, CRITICAL
    String category;
    String resolutionNote;

    public SupportTicketDTO() {}

    public SupportTicketDTO(Long id, String ticketNo, String title, String description, String requester, String assignee, String status, String priority, String category, String resolutionNote) {
        this.id = id;
        this.ticketNo = ticketNo;
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.assignee = assignee;
        this.status = status;
        this.priority = priority;
        this.category = category;
        this.resolutionNote = resolutionNote;
    }

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

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResolutionNote() {
        return resolutionNote;
    }

    public void setResolutionNote(String resolutionNote) {
        this.resolutionNote = resolutionNote;
    }
}
