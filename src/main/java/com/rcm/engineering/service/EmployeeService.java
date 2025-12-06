package com.rcm.engineering.service;

import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.domain.events.EmployeeEvent;

public interface EmployeeService {

    Employee createEmployee(Employee employee);   // Step-1
    void updateStatusFromCM(EmployeeEvent event);
    void updateStatus(String empCode, EmployeeStatus newStatus);
}
