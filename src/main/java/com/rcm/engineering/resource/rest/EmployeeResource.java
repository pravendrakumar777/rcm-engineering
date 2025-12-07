package com.rcm.engineering.resource.rest;

import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/employees/create")
    public ResponseEntity<?> saveEmployee(@RequestBody Employee employee) {
        log.info("REST Request to createEmployee: {}", employee);
        String empCode = "RCMEC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
        employee.setEmpCode(empCode);
        Employee createdEmp = employeeService.createEmployee(employee);
        return ResponseEntity.ok().body(createdEmp);
    }

    @GetMapping("/employees/list")
    public ResponseEntity<List<Employee>> getAll() {
        log.info("REST Request to getAll: {}, {}");
        List<Employee> result = employeeRepository.findAll();
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/employees/update/{empCode}")
    public ResponseEntity<?> updateEmployee(@PathVariable String empCode, @RequestBody Employee employee) {
        Optional<Employee> existingOpt = employeeRepository.findByEmpCode(empCode);
        String EMPLOYEE_CODE = existingOpt.get().getEmpCode();
        log.info("REST Request to updateEmployee: {}, {}", EMPLOYEE_CODE, employee);
        if (!existingOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee not found with empCode: " + empCode);
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
    public ResponseEntity<String> actionOnClick(@RequestParam String empCode,
                                                @RequestParam String action) {
        log.info("REST Request to actionOnClick: {}", empCode);
        Employee emp = employeeRepository.findByEmpCode(empCode).orElse(null);
        if (emp == null) {
            return ResponseEntity.badRequest().body("Employee not found for EmpCode: " + empCode);
        }
        // Block if already ACTIVE or REJECTED
        if (emp.getStatus() == EmployeeStatus.ACTIVE || emp.getStatus() == EmployeeStatus.CANCEL) {
            return ResponseEntity.badRequest().body("Already taken the action on this EmpCode: " + empCode);
        }

        if (emp.getStatus() != EmployeeStatus.PENDING) {
            return ResponseEntity.badRequest().body("Action not allowed. Current status: " + emp.getStatus());
        }

        // Decide new status based on action
        switch (action.toLowerCase()) {
            case "approve":
                employeeService.updateStatus(empCode, EmployeeStatus.ACTIVE);
                return ResponseEntity.ok("Employee status changed to ACTIVE");
            case "reject":
                employeeService.updateStatus(empCode, EmployeeStatus.CANCEL);
                return ResponseEntity.ok("Employee status changed to CANCEL");
            default:
                return ResponseEntity.badRequest().body("Invalid action: use approve/reject");
        }
    }
}
