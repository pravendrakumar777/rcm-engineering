package com.rcm.engineering.service.impl;

import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.resource.utils.FtlToPdfUtil;
import com.rcm.engineering.service.EmployeeService;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final Configuration freemarkerConfig;
    private final FtlToPdfUtil ftlToPdfUtil;
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(Configuration freemarkerConfig, FtlToPdfUtil ftlToPdfUtil, EmployeeRepository employeeRepository) {
        this.freemarkerConfig = freemarkerConfig;
        this.ftlToPdfUtil = ftlToPdfUtil;
        this.employeeRepository = employeeRepository;
    }

    public byte[] generateEmployeeProfilePdf(String empCode) {
        return ftlToPdfUtil.generateEmployeeProfile(empCode, freemarkerConfig);
    }


    @Override
    public Employee createEmployee(Employee employee) {
        log.info("Service Request to createEmployee: {}", employee);
        employee.setStatus(EmployeeStatus.PENDING);
        Employee saveEmp = employeeRepository.save(employee);
        return saveEmp;
    }

    @Override
    public void updateStatus(String empCode, EmployeeStatus newStatus) {
        Employee emp = employeeRepository.findByEmpCode(empCode).orElse(null);
        if (emp != null) {
            if(emp.getStatus() == EmployeeStatus.PENDING) {
                emp.setStatus(newStatus);
                employeeRepository.save(emp);
            }
        }
    }

    @Override
    public List<Employee> getAllActive() {
        return employeeRepository.findAllActiveEmployees();
    }
}
