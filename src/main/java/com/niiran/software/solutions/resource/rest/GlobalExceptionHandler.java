package com.niiran.software.solutions.resource.rest;

import com.niiran.software.solutions.exceptions.EmployeeCreationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmployeeCreationException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeCreationException(EmployeeCreationException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Employee creation failed");
        error.put("message", ex.getMessage());
        error.put("traceId", MDC.get("traceId"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
