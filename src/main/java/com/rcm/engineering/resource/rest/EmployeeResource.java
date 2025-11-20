package com.rcm.engineering.resource.rest;

import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final EmployeeRepository employeeRepository;
    public EmployeeResource(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/employees/create")
    public ResponseEntity<?> saveEmployee(@RequestBody Employee employee) {
        Employee result = employeeRepository.save(employee);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employees/list")
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> result = employeeRepository.findAll();
        return ResponseEntity.ok().body(result);
    }
}
