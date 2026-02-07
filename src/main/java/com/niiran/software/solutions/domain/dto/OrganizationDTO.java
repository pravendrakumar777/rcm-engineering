package com.niiran.software.solutions.domain.dto;

public class OrganizationDTO {

    private Long id;
    private String name;
    private String createdDateFormatted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDateFormatted() {
        return createdDateFormatted;
    }

    public void setCreatedDateFormatted(String createdDateFormatted) {
        this.createdDateFormatted = createdDateFormatted;
    }
}
