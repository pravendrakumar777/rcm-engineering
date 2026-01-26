package com.niiran.software.solutions.constants;

public class ApplicationConstants {

    private ApplicationConstants() {
        // prevent instantiation
    }

    public static final String EMPLOYEE_NOT_FOUND = "Employee not found.";
    public static final String ACTION_ALREADY_TAKEN = "Action already taken. Current status: ";
    public static final String ACTION_NOT_ALLOWED = "Action not allowed. Current status: ";
    public static final String INVALID_ACTION = "Invalid action: use approve/reject";
    public static final String PRE_ONBOARDING_EXPIRED = "Your Pre‑Onboarding has been cancelled because 5 days are complete.";
    public static final String STATUS_ACTIVE = "Employee status changed to ACTIVE";
    public static final String STATUS_CANCELLED = "Employee status changed to CANCEL";
    public static final String INVALID_CREDENTIALS = "Invalid username or password length";
    public static final String USER_REGISTERED_SUCCESSFULLY = "User registered successfully";
    public static final String LOGIN_SUCCESS = "Login Successfully";
    public static final String EMPLOYEE_FOUND_SUCCESS = "Employee found successfully";
    public static final String SERVER_ERROR = "Internal server error occurred";
    public static final String RECORDS_FOUND = "Data fetch successfully";
    public static final String RECORDS_NOT_FOUND = "No records found";
    public static final String ERROR_OCCURRED = "Internal server error occurred";
    public static final String SUCCESS_STATUS_CODE = "RCM_200";
    public static final String NOT_FOUND_STATUS_CODE = "RCM_404";
    public static final String INTERNAL_SERVER_ERROR = "RCM_500";
}
