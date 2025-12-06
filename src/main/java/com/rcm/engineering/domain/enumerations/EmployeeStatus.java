package com.rcm.engineering.domain.enumerations;

public enum EmployeeStatus {
    PENDING,   // Pre-onboarding → waiting for CM
    APPROVED,           // Approved by CM (will internally activate)
    REJECTED,           // Rejected by CM
    ACTIVE;             // Final status for employees visible in listing
}
