package com.rcm.engineering.domain.dto;

public class EmployeeDisplayDTO {

    private Long employeeId;
    private String customId;
    private String name;
    private String mobile;

    // Constructor

    public EmployeeDisplayDTO(Long employeeId, String customId, String name, String mobile) {
        this.employeeId = employeeId;
        this.customId = customId;
        this.name = name;
        this.mobile = mobile;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
