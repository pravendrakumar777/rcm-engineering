package com.niiran.software.solutions.service;

import com.niiran.software.solutions.domain.Employee;
import com.niiran.software.solutions.domain.enumerations.EmployeeStatus;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(Employee employee);
    void updateStatus(String ohr, EmployeeStatus newStatus);
    List<Employee>  getAllActive();
}
