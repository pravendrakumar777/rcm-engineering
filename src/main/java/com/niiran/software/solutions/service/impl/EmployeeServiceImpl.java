package com.niiran.software.solutions.service.impl;

import com.niiran.software.solutions.domain.Employee;
import com.niiran.software.solutions.domain.enumerations.EmployeeStatus;
import com.niiran.software.solutions.exceptions.EmployeeCreationException;
import com.niiran.software.solutions.repository.EmployeeRepository;
import com.niiran.software.solutions.resource.utils.FtlToPdfUtil;
import com.niiran.software.solutions.service.EmployeeService;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataAccessException;
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

    public byte[] generateEmployeeProfilePdf(String ohr) {
        return ftlToPdfUtil.generateEmployeeProfile(ohr, freemarkerConfig);
    }


    @Override
    public Employee createEmployee(Employee employee) {
        String traceId = MDC.get("traceId");
        final String endpoint = "/employees/create";
        try {
            log.info("traceId:{} | Source:APK | RequestType:REST | Endpoint:{} | Action:createEmployee | Step:START | Payload:{}", traceId, endpoint, employee);

            employee.setStatus(EmployeeStatus.PENDING);
            log.debug("traceId:{} | Source:APK | RequestType:REST | Endpoint:{} | Action:createEmployee | Step:SET_STATUS | Status:{}", traceId, endpoint, employee.getStatus());

            Employee saveEmp = employeeRepository.save(employee);
            log.info("traceId:{} | Source:APK | RequestType:REST | Endpoint:{} | Action:createEmployee | Step:REPOSITORY_SAVE | Result:SUCCESS | EmployeeId:{}", traceId, endpoint, saveEmp.getId());

            log.info("traceId:{} | Source:APK | RequestType:REST | Endpoint:{} | Action:createEmployee | Step:END | Status:RETURN | EmployeeId:{}", traceId, endpoint, saveEmp.getId());
            return saveEmp;

        } catch (DataAccessException dae) {
            log.error("traceId:{} | Source:APK | RequestType:REST | Endpoint:{} | Action:createEmployee | Step:ERROR | Type:DataAccessException | Message:{} | Payload:{}",
                    traceId, endpoint, dae.getMessage(), employee, dae);
            throw new EmployeeCreationException("Database error while creating employee", dae);

        } catch (Exception ex) {
            log.error("traceId:{} | Source:APK | RequestType:REST | Endpoint:{} | Action:createEmployee | Step:ERROR | Type:Exception | Message:{} | Payload:{}",
                    traceId, endpoint, ex.getMessage(), employee, ex);
            throw new EmployeeCreationException("Unexpected error while creating employee", ex);

        } finally {

            MDC.clear();
        }
    }

    @Override
    public void updateStatus(String ohr, EmployeeStatus newStatus) {
        Employee emp = employeeRepository.findByOhr(ohr).orElse(null);
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
