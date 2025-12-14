package com.rcm.engineering.service;

import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(Employee employee);   // Step-1
    void updateStatus(String empCode, EmployeeStatus newStatus);
    List<Employee>  getAllActive();
}
