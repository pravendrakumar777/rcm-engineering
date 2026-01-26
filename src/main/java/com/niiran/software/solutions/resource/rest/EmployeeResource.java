package com.niiran.software.solutions.resource.rest;

import com.niiran.software.solutions.constants.ApplicationConstants;
import com.niiran.software.solutions.domain.Employee;
import com.niiran.software.solutions.domain.enumerations.EmployeeStatus;
import com.niiran.software.solutions.repository.EmployeeRepository;
import com.niiran.software.solutions.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class EmployeeResource {
    private static final Logger log = LoggerFactory.getLogger(EmployeeResource.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    public EmployeeResource(EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    @GetMapping("/employees/{ohr}")
    public ResponseEntity<Employee> getEmpByOhr(@PathVariable String ohr) {
        log.info("REST Request to getEmpByOhr: {}", ohr);
        return employeeRepository.findByOhr(ohr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/employees/create")
    public ResponseEntity<?> saveEmployee(@RequestBody Employee employee) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        final String endpoint = "/employees/create";

        try {
            log.info("traceId: {} | Source: APK | RequestType: REST | Endpoint: {} | Action: createEmployee | Step: START | Payload: {}", traceId, endpoint, employee);

            String ohrCode = "RCMEC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
            employee.setOhr(ohrCode);
            log.debug("traceId: {} | Source: APK | RequestType: REST | Endpoint: {} | Action: createEmployee | Step: GENERATE_CODE | OHR: {}", traceId, endpoint, ohrCode);

            Employee createdEmp = employeeService.createEmployee(employee);
            log.info("traceId: {} | Source: APK | RequestType: REST | Endpoint: {} | Action: createEmployee | Step: SERVICE_CALL | Result: SUCCESS | EmployeeId: {}", traceId, endpoint, createdEmp.getId());

            log.info("traceId: {} | Source: APK | RequestType: REST | Endpoint: {} | Action: createEmployee | Step: END | Status: 200 OK | EmployeeId: {}", traceId, endpoint, createdEmp.getId());
            return ResponseEntity.ok().body(createdEmp);

        } catch (Exception ex) {
            log.error("traceId: {} | Source: APK | RequestType: REST | Endpoint: {} | Action: createEmployee | Step: ERROR | Message: {} | Payload: {}", traceId, endpoint, ex.getMessage(), employee, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating employee");
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/employees/list")
    public ResponseEntity<List<Employee>> getAll() {
        log.info("REST Request to getAll: {}, {}");
        List<Employee> result = employeeService.getAllActive();
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/employees/update/{ohr}")
    public ResponseEntity<?> updateEmployee(@PathVariable String ohr, @RequestBody Employee employee) {
        Optional<Employee> existingOpt = employeeRepository.findByOhr(ohr);
        String EMPLOYEE_OHR = existingOpt.get().getOhr();
        log.info("REST Request to updateEmployee: {}, {}", EMPLOYEE_OHR, employee);
        if (!existingOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee not found with empCode: " + ohr);
        }
        Employee existing = existingOpt.get();
        existing.setName(employee.getName());
        existing.setEmail(employee.getEmail());
        existing.setMobile(employee.getMobile());
        existing.setGender(employee.getGender());
        existing.setManager(employee.getManager());
        existing.setDateOfBirth(employee.getDateOfBirth());
        existing.setAddress(employee.getAddress());
        existing.setCity(employee.getCity());
        existing.setState(employee.getState());
        existing.setPostalCode(employee.getPostalCode());
        existing.setCountry(employee.getCountry());
        existing.setDepartment(employee.getDepartment());
        existing.setDesignation(employee.getDesignation());
        existing.setPanNumber(employee.getPanNumber());
        existing.setAadhaarNumber(employee.getAadhaarNumber());
        existing.setBankName(employee.getBankName());
        existing.setBankAccountNumber(employee.getBankAccountNumber());
        existing.setIfscCode(employee.getIfscCode());
        existing.setSalary(employee.getSalary());
        Employee updated = employeeRepository.save(existing);
        log.info("UPDATED EMPLOYEE OBJECT: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/employees/actions")
    public ResponseEntity<String> actionOnClick(@RequestParam String ohr,
                                                @RequestParam String action) {
        log.info("REST Request to actionOnClick: {}", ohr);
        Employee emp = employeeRepository.findByOhr(ohr).orElse(null);
        if (emp == null) {
            return ResponseEntity.badRequest().body(ApplicationConstants.EMPLOYEE_NOT_FOUND);
        }

        if (emp.getCreatedAt() != null) {
            LocalDateTime cutoff = emp.getCreatedAt().plusDays(5);
            if (LocalDateTime.now().isAfter(cutoff)) {
                emp.setStatus(EmployeeStatus.CANCEL);
                employeeRepository.save(emp);
                return ResponseEntity.badRequest()
                        .body(ApplicationConstants.PRE_ONBOARDING_EXPIRED);
            }
        }

        // Block if already ACTIVE or REJECTED
        if (emp.getStatus() == EmployeeStatus.ACTIVE || emp.getStatus() == EmployeeStatus.CANCEL) {
            return ResponseEntity.badRequest().body(ApplicationConstants.ACTION_ALREADY_TAKEN);
        }

        if (emp.getStatus() != EmployeeStatus.PENDING) {
            return ResponseEntity.badRequest().body(ApplicationConstants.ACTION_NOT_ALLOWED + emp.getStatus());
        }

        switch (action.toLowerCase()) {
            case "approve":
                employeeService.updateStatus(ohr, EmployeeStatus.ACTIVE);
                return ResponseEntity.ok(ApplicationConstants.STATUS_ACTIVE);
            case "reject":
                employeeService.updateStatus(ohr, EmployeeStatus.CANCEL);
                return ResponseEntity.ok(ApplicationConstants.STATUS_CANCELLED);
            default:
                return ResponseEntity.badRequest().body(ApplicationConstants.INVALID_ACTION);
        }
    }

    @GetMapping("/employees/search")
    public ResponseEntity<?> searchEmployee(@RequestParam("query") String query) {
        log.info("SEARCH_EMPLOYEE | Request received | OHR: {}", query);
        try {
            Optional<Employee> result = employeeRepository.findByNameIgnoreCaseOrOhrIgnoreCase(query, query);

            if (result.isPresent()) {
                log.info("SEARCH_EMPLOYEE | Success | OHR: {} | : {}", query, result.get());
                return ResponseEntity.ok(result.get());
            } else {
                log.warn("SEARCH_EMPLOYEE | Not Found | query: {}", query);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No employee found with given query");
            }

        } catch (Exception ex) {
            log.error("SEARCH_EMPLOYEE | Error | query: {} | exception: {}", query, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error occurred");
        }
    }
}
